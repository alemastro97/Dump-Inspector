package it.polimi.tiw.dump_inspector.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.apache.tomcat.util.json.*;
import org.json.JSONArray;

import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;
import it.polimi.tiw.dump_inspector.bean.*;

@MultipartConfig
@WebServlet("/NewLocalitaImmagine")
public class NewLocalitaImmagine extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url + "?useTimezone=true&serverTimezone=UTC", user, password);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    public NewLocalitaImmagine() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    	String loginpath = getServletContext().getContextPath() + "/GetLogin";
		System.out.println("Sono in GetManagerPage");
		Utente u = null;
		HttpSession s = request.getSession();
		if (s.isNew() || s.getAttribute("account") == null) {
			System.out.println("Sessione nuova o account nullo, redirect a GetLogin");
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (!u.isManager()) {
				System.out.println("Account lavoratore, redirect a GetLogin");
				response.sendRedirect(loginpath);
				return;
			}
		}
		
    	
    	Boolean newLocalita = Boolean.parseBoolean(request.getParameter("newLocalita"));
        LocalitaDAO ldao = new LocalitaDAO(connection);
        ImmagineDAO idao = new ImmagineDAO(connection);
        CampagnaDAO cdao = new CampagnaDAO(connection);

        String risoluzione = request.getParameter("risoluzione");
        String provenienza = request.getParameter("provenienza");
        String data_acquisizione = request.getParameter("data_acquisizione");
        int idcampagna = Integer.parseInt(request.getParameter("idcampagna"));
        
        if (!cdao.findCampagna(idcampagna).getStato().equals("creato")) {
            response.sendError(505, "Campagna gi� avviata");
            return;
        }
        
        if (newLocalita == null || risoluzione.equals("") || risoluzione == null || provenienza.equals("") || provenienza == null || data_acquisizione.equals("") || data_acquisizione == null) {
            response.sendError(505, "Parameters incomplete");
            return;
        }

        int newKey = -1;
        if (newLocalita) {
            Double latitudine = Double.parseDouble(request.getParameter("latitudine"));
            Double longitudine = Double.parseDouble(request.getParameter("longitudine"));
            String nome = request.getParameter("nome");
            String comune = request.getParameter("comune");
            String regione = request.getParameter("regione");
            
            
            Boolean validLat = true;
            if (latitudine < -90 || latitudine > +90) {
                validLat = false;
            }
            Boolean validLon = true;
            if (longitudine < -180 || longitudine > +180) {
                validLat = false;
            }
            
            if (nome.equals("") || nome == null || comune.equals("") || comune == null || regione.equals("") || regione == null || !validLat || !validLon) {
                response.sendError(505, "Parameters incomplete");
                return;
            }

            newKey = ldao.createLocalita(latitudine, longitudine, nome, comune, regione, idcampagna);

        } else {
            newKey = Integer.parseInt(request.getParameter("idlocalita"));
        }

//--------------------------------------------------------->> RECUPERO IMMAGINE <<-----------//	    
        Part imagePart = request.getPart("image");

        InputStream imageStream = null;
        String mimeType = null;
        if (imagePart != null) {
            imageStream = imagePart.getInputStream();
            String filename = imagePart.getSubmittedFileName();
            mimeType = getServletContext().getMimeType(filename);
        }

        if (imageStream == null || (imageStream.available()==0) || !mimeType.startsWith("image/")) {
            response.sendError(505, "Parameters incomplete");
            return;
        }
        
        idao.insertImmagine(provenienza, data_acquisizione, imageStream, risoluzione, newKey);

        String wizardpage = getServletContext().getContextPath() + "/GetDettaglioCampagna" + "?idCampagna="
                + idcampagna;
        response.sendRedirect(wizardpage);
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
        }
    }

}

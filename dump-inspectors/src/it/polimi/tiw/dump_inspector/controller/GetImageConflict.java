package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import it.polimi.tiw.dump_inspector.bean.Annotazione;
import it.polimi.tiw.dump_inspector.bean.Localita;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.AnnotazioneDAO;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;
import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class GetImageConflict
 */
@WebServlet("/GetImageConflict")
public class GetImageConflict extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private Connection connection = null;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);

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
	
    public GetImageConflict() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente u = null;
		HttpSession s = request.getSession();
	    String pathHome = getServletContext().getContextPath()+"/GetLogin";
		if (s.isNew() || s.getAttribute("account") == null) {
	    	response.sendRedirect(pathHome);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (!u.isManager()) {
				response.sendRedirect(pathHome);
		        return;
			}
		}
		int immagineId =Integer.parseInt(request.getParameter("idImmagine"));
	    AnnotazioneDAO aDAO = new AnnotazioneDAO(connection);
	    List<Annotazione> annotazioni = new ArrayList<Annotazione>();
	    
	    annotazioni = aDAO.findAllImmagineAnnotazione(immagineId);
	    
	    List<String> utenti = new ArrayList<String>();
	    
	    for(int i = 0; i < annotazioni.size(); i++)
	    {
	    		utenti.add(aDAO.findUserByAnnotazione(annotazioni.get(i).getIdAnnotazione()).get(0));
	    }
	    
	    JsonObject Obj1 = new JsonObject();
	    Gson gson = new Gson();
	    String json = gson.toJson(annotazioni);
	    String json2 = gson.toJson(utenti);
	    Obj1.addProperty("annotazioni", json);
	    Obj1.addProperty("utenti", json2);
	    response.setContentType("text/html");
	    response.getWriter().write(Obj1.toString());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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

package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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

import it.polimi.tiw.dump_inspector.bean.Annotazione;
import it.polimi.tiw.dump_inspector.bean.Immagine;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.AnnotazioneDAO;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class GetAnnotation
 */
@WebServlet("/GetAnnotation")
public class GetAnnotation extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAnnotation() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException{
    	servletContext = getServletContext();
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
            url = url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            // throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            // throw new UnavailableException("Couldn't get db connection");
        }
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente u = null;
		HttpSession s = request.getSession();
	    String pathHome = getServletContext().getContextPath()+"/GetLogin";
	    final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		
	    if (s.isNew() || s.getAttribute("account") == null) {
	    	response.sendRedirect(pathHome);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
		/*	if (u.isManager()) {
		        templateEngine.process(pathHome, context, response.getWriter());
		        return;
			}*/
		}
	    String username = request.getParameter("Username");
	    int idUtente;
	    if(username == null) {idUtente = u.getIdUtente();
	    }else{UtenteDAO uDao = new UtenteDAO(connection);idUtente = uDao.getIdByUsername(username);}
	    String id = request.getParameter("idImmagine");
	    int idImmagine = Integer.parseInt(id);
	    AnnotazioneDAO aDao = new AnnotazioneDAO(connection);
	    Annotazione a = aDao.findUserAnnotation(idImmagine,idUtente);
	    Gson gson = new Gson();
		String jsonString = gson.toJson(a);
		response.setContentType("text/html");
	    //response.setCharacterEncoding("UTF-8");

		response.getWriter().write(jsonString);
	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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

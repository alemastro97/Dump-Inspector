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

import it.polimi.tiw.dump_inspector.bean.Campagna;
import it.polimi.tiw.dump_inspector.bean.Immagine;
import it.polimi.tiw.dump_inspector.bean.Localita;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.AnnotazioneDAO;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;
import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class GetWorkerMap
 */
@WebServlet("/GetWorkerMap")
public class GetWorkerMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  
    
    public GetWorkerMap() {
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
	    final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
			String pathHome = getServletContext().getContextPath()+"/GetLogin";
		if (s.isNew() || s.getAttribute("account") == null) {
			response.sendRedirect(pathHome);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (u.isManager()) {
				response.sendRedirect(pathHome);
		        return;
			}
		}
		UtenteDAO uDao = new UtenteDAO(connection);
		String id = request.getParameter("idCampagna");
		int idCampagna = Integer.parseInt(id);
		String new_user = request.getParameter("new"); 
		
		if(new_user.equals("1"))
		uDao.joinCampagna(idCampagna,((Utente)s.getAttribute("account")).getIdUtente());
		
		
		CampagnaDAO cDao = new CampagnaDAO(connection);
		LocalitaDAO lDao = new LocalitaDAO(connection);

		//Settaggio dati campagna in variabile campagna
		context.setVariable("Campagna",(Campagna)cDao.findCampagna(idCampagna));
		
		List <Localita> ls = lDao.searchLocalitaCampagna(idCampagna);


		//Settaggio lista localita inerenti alla campagna
		
		context.setVariable("Localita",ls);
		templateEngine.process("/WEB-INF/WorkerMap.html", context, response.getWriter());
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

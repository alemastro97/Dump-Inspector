package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dump_inspector.bean.Campagna;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;

/**
 * Servlet implementation class CloseCampagna
 */
@WebServlet("/CloseCampagna")
public class CloseCampagna extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CloseCampagna() {
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
		// TODO Auto-generated method stub
		String loginpath = getServletContext().getContextPath() + "/GetLogin";
		Utente u = null;
		HttpSession s = request.getSession();
		if (s.isNew() || s.getAttribute("account") == null) {
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (!u.isManager()) {
				response.sendRedirect(loginpath);
				return;
			}
		}
		
		String idCampagna = request.getParameter("idCampagna");
		
		if(idCampagna == null){
			response.sendError(500, "Missing parameter in campaign starting");
			return;
			}
		
		CampagnaDAO cDao = new CampagnaDAO(connection);
		int id = Integer.parseInt(idCampagna);
		Campagna c = cDao.findCampagna(id);
		if(c.getStato().equals("avviato")) {cDao.closeCampagna(id);}else {
			response.sendError(500, "Campagna non in stato avviato");
		return;}
		
		
		String path = getServletContext().getContextPath() + "/GetDettaglioCampagna?idCampagna="+idCampagna;
		response.sendRedirect(path);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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

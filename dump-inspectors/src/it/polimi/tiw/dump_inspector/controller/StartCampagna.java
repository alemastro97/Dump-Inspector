package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import it.polimi.tiw.dump_inspector.bean.Localita;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;

/**
 * Servlet implementation class StartCampagna
 */
@WebServlet("/StartCampagna")
public class StartCampagna extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartCampagna() {
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
		
		LocalitaDAO ldao = new LocalitaDAO(connection);
		
		if (ldao.searchLocalitaCampagna(Integer.parseInt(idCampagna)).isEmpty()) {
		    
		    String path = "/WEB-INF/GestioneCampagna.html";
		    final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		    
		    Boolean ll = false;
		    
	        int idC = Integer.parseInt(idCampagna);
	        CampagnaDAO cDao = new CampagnaDAO(connection);
	        Campagna c = cDao.findCampagna(idC);
	        LocalitaDAO lDao = new LocalitaDAO(connection);
	        List<Localita> l = lDao.searchLocalitaCampagna(idC);
	        ctx.setVariable("localitaPresente", ll);
	        ctx.setVariable("campagna", c);
	        ctx.setVariable("localita", l);
	        templateEngine.process(path, ctx, response.getWriter());
		    //response.sendError(500, "Missing parameter in campaign starting");
            return;
		}
		
		CampagnaDAO cDao = new CampagnaDAO(connection);
		if(cDao.findCampagna(Integer.parseInt(idCampagna)).getStato().equals("creato")) {
		cDao.startCampagna(Integer.parseInt(idCampagna));}else {response.sendError(500, "La campagna non è nello stato creato");}
		
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

package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.AnnotazioneDAO;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;
import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;

/**
 * Servlet implementation class GetStats
 */
@WebServlet("/GetStats")
public class GetStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	ServletContext servletContext;

    public void init() throws ServletException {
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
    
    public GetStats() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		int idCampagna = Integer.parseInt(request.getParameter("idCampagna"));
		
		LocalitaDAO lDAO = new LocalitaDAO(this.connection);
		ImmagineDAO mDAO = new ImmagineDAO(this.connection);
		AnnotazioneDAO aDAO = new AnnotazioneDAO(this.connection);
		CampagnaDAO cDAO = new CampagnaDAO(this.connection);
		
	    int totLoc = lDAO.getTotalLocalitaByCampagna(idCampagna);
	    int totImm = mDAO.getTotalImmaginiByCampagna(idCampagna);
	    int totAnn = aDAO.getTotalAnnotazioniByCampagna(idCampagna);
	    float avgImmPerLoc = mDAO.getAverageImmaginiPerLocalitaByCampagna(idCampagna);
	    float avgAnnPerImm = aDAO.getAverageAnnotazioniPerImmagineByCampagna(idCampagna);
	    ArrayList<Integer> idconflitti = mDAO.getImmaginiConConflitti(idCampagna);
	    int numConflitti = idconflitti.size();
	    Campagna c = cDAO.findCampagna(idCampagna);
	    ArrayList<Immagine> conflitti = new ArrayList<Immagine>();
	    for(int i = 0; i < numConflitti; i++)
	    {
	    	conflitti.add(mDAO.getImmagineByID(idconflitti.get(i)));
	    
	    }
    	String pathHome = "/WEB-INF/Statistics.html";
        final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
        //context.setVariable("idCampagna",idCampagna);
        context.setVariable("nomeCampagna",c.getNome());
        context.setVariable("totLoc",totLoc);
        context.setVariable("totImm",totImm);
        context.setVariable("totAnn",totAnn);
        context.setVariable("avgImmPerLoc",avgImmPerLoc);
        context.setVariable("avgAnnPerImm",avgAnnPerImm);
        context.setVariable("conflitti",conflitti);
        context.setVariable("numConflitti",numConflitti);
        templateEngine.process(pathHome, context, response.getWriter());
	        
		
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

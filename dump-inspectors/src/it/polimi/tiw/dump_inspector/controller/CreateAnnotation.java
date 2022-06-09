package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.AnnotazioneDAO;

/**
 * Servlet implementation class CreateAnnotation
 */
@WebServlet("/CreateAnnotation")
public class CreateAnnotation extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  
      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAnnotation() {
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
			if (u.isManager()) {
				response.sendRedirect(pathHome);
		        return;
			}
		}
	    String nota = request.getParameter("Nota");
		String id  = request.getParameter("hidden_image");
		String radio = request.getParameter("radio");
		
		boolean valida;
		
		int idImmagine = Integer.parseInt(id);
		if(radio.equals("valida"))
			valida = true;
		else
			valida = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");
		
		AnnotazioneDAO aDao = new AnnotazioneDAO(connection);
		if(aDao.findUserAnnotation(idImmagine,u.getIdUtente())==null) {	aDao.createAnnotazione(LocalDate.now().format(formatter),valida,u.getEsperienza(),nota,u.getIdUtente(),idImmagine);}
		else {	response.sendError(500, "Annotazione già inserita dall'utente");}
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

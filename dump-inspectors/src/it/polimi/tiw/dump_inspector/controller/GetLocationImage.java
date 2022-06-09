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
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import it.polimi.tiw.dump_inspector.bean.Immagine;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.ImmagineDAO;

/**
 * Servlet implementation class GetLocationImage
 */
@WebServlet("/GetLocationImage")
public class GetLocationImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	ServletContext servletContext;

	  public void init() throws ServletException{
	    	servletContext = getServletContext();
			ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
			templateResolver.setTemplateMode(TemplateMode.HTML);
			this.templateEngine = new TemplateEngine();
			this.templateEngine.setTemplateResolver(templateResolver);
			//templateResolver.setSuffix(".html");
			
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
	    	
    public GetLocationImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
		
		}
	    ImmagineDAO iDao = new ImmagineDAO (connection);
	    List<Immagine> i_list = new ArrayList<Immagine>();
	    String id = request.getParameter("idLocalita");
	    int id_localita = Integer.parseInt(id);
	    i_list = iDao.findAllImmagineLocalita(id_localita);
	    Gson gson = new Gson();
		String jsonString = gson.toJson(i_list);
		response.setContentType("text/html");
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

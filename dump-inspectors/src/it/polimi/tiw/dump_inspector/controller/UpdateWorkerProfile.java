package it.polimi.tiw.dump_inspector.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.gson.Gson;

import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class UpdateWorkerPage
 */
@MultipartConfig
@WebServlet("/UpdateWorkerProfile")
public class UpdateWorkerProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	ServletContext servletContext;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateWorkerProfile() {
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
		doPost(request,response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utente u = null;
		String username = request.getParameter("username"), 
				oldpwd = request.getParameter("oldpwd"),
				newpwd = request.getParameter("newpwd"),
				newpwd_confirm = request.getParameter("newpwdconfirm"),
				email = request.getParameter("email");
		HttpSession s = request.getSession();
		final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		String pathHome = getServletContext().getContextPath()+"/GetLogin";
		if (s.isNew() || s.getAttribute("account") == null) {
			response.sendRedirect(pathHome);
			//response.sendRedirect(pathHome);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (u.isManager()) {
				response.sendRedirect(pathHome);
				return;
			}
		}
		String password = u.getPassword();
		InputStream imageStream = null;

		int image_controller = 1;
		Part imagePart = request.getPart("avatar");
		
		imageStream = imagePart.getInputStream();
		String mimeType = null;
		if (imageStream.available()  != 0) {

			String filename = imagePart.getSubmittedFileName();
			mimeType = getServletContext().getMimeType(filename);
		}else {
			image_controller = 0;
		}

		UtenteDAO uDao = new UtenteDAO(connection);
		if (!(u.getUsername().equals(username))) 
		{
			if(uDao.usernamePresente(username) || username.equals("")) 
			{
				response.sendError(500,"Username gia' presente");
				return;
			}
		} 
		if(!(u.getEmail().equals(email))) {
			if(uDao.emailPresente(email)|| email.equals("")) 
			{
				response.sendError(501,"Email gia' presente");
				return;
			}
		}
		if((u.getPassword().equals(oldpwd))) {
			if(!(newpwd.equals("")) && newpwd.length() > 7) {	
				if((newpwd.equals(newpwd_confirm))) {password = newpwd;}
				else{response.sendError(503,"Le due password non coincidono");return;}
			}else {response.sendError(502,"Password troppo corta");return;}
		}
		if(	image_controller==1)	uDao.setCredenzialiUtente(u.getIdUtente(),username,password,email,u.isManager(),u.getEsperienza(),imageStream);
		else {uDao.setCredenzialiUtenteWithoutImage(u.getIdUtente(),username,password,email,u.isManager(),u.getEsperienza());}
		s.setAttribute("account",uDao.getUserByUsername(username));


		String path = servletContext.getContextPath() + "/GetWorkerPage";
		response.sendRedirect(path);


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

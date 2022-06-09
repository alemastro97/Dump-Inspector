package it.polimi.tiw.dump_inspector.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class checkRegistrazione
 */
@MultipartConfig
@WebServlet("/CheckRegistrazione")
public class CheckRegistrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  

    public CheckRegistrazione() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = servletContext.getContextPath();
		InputStream imageStream = null;
		UtenteDAO uDao = new UtenteDAO(connection);
		String email = request.getParameter("Email"),
				username = request.getParameter("Username"),
				password =request.getParameter("Password"),
				userType = request.getParameter("UserType"),
				password_conferma = request.getParameter("ConfermaPassword"),
				imgName = "NULL";
	
		Part imagePart = request.getPart("Avatar");
		imageStream = null;
		String mimeType = null;
		if (imagePart  != null) {
			imageStream = imagePart .getInputStream();
			String filename = imagePart.getSubmittedFileName();
			mimeType = getServletContext().getMimeType(filename);
		}

	    boolean manager = false;
	    boolean buser = uDao.usernamePresente(username);
		boolean bmail = uDao.emailPresente(email);
		boolean bpwd = true;
		String avatar = "NULL";
		if(password.length()>7) {bpwd = false;}
		boolean checkEqualPwd = false;
		if(password.equals(password_conferma)) {checkEqualPwd = true;}
		String esperienza = "NULL";
		if(buser || bmail || bpwd || !checkEqualPwd) 
		{
			path += "/GetLogin";
			if(buser) {path+="?erroru=1";}else{path+="?erroru=0";}
			if(bmail) {path+="&errormail=1";}else{path+="&errormail=0";}
			if(bpwd) {path+="&errorpwd=1";}else{path+="&errorpwd=0";}
			if(!checkEqualPwd) {path+="&errorcpwd=1"; }else{path+="&errorcpwd=0";}
		}
		else
		{
			switch(userType) 
			{
				case "Manager":	
					path+="/GetManagerPage";
					manager = true;
					break;
				case "Lavoratore":	
					esperienza = "basso";
					avatar = imgName;
					path+="/GetWorkerPage"; 
					break;
			}	
			uDao.createUser(username,password,email,manager,esperienza,imageStream);
			Utente u = uDao.checkUser(username,password);
			
			request.getSession().setAttribute("account",u);
		}
		response.sendRedirect(path);
		
		
		
		
		
		
		
		
		
	}
	
	/*private boolean mailSyntaxCheck(String email)
	   {
	        // Create the Pattern using the regex
	        Pattern p = Pattern.compile("^.+@.+\\.[a-z]+&");
	 
	        // Match the given string with the pattern
	        Matcher m = p.matcher(email);
	 
	        // check whether match is found
	        boolean matchFound = m.matches();
	 
	        StringTokenizer st = new StringTokenizer(email, ".");
	        String lastToken = null;
	        while (st.hasMoreTokens()) {
	            lastToken = st.nextToken();
	        }
	 
	    // validate the country code
	        if (matchFound && lastToken.length() >= 2
	                && email.length() - 1 != lastToken.length()) {
	 
	            return true;
	        } else {
	            return false;
	        }
	 
	    }
	*/
}

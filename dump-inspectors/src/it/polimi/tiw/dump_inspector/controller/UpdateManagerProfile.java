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

import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class UpdateManagerProfile
 */
@WebServlet("/UpdateManagerProfile")
public class UpdateManagerProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Connection connection = null;
 private TemplateEngine templateEngine;
 ServletContext servletContext;  
 /**
  * @see HttpServlet#HttpServlet()
  */
 public UpdateManagerProfile() {
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
		String path = servletContext.getContextPath();
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
			
		String change = request.getParameter("toChange");
		//int id = ((Utente) (s.getAttribute("account"))).getIdUtente();
		int id = u.getIdUtente();
		UtenteDAO uDao = new UtenteDAO(connection);
		if(change.equals("data"))
		{
			String username = request.getParameter("username");
			String email = request.getParameter("email");
			if(username == null || email == null){
				response.sendError(500, "Missing parameter");
				return;
				}
			
			boolean buser = uDao.usernamePresente(username) && (!username.equals(u.getUsername()));
			boolean bmail = uDao.emailPresente(email) && (!email.equals(u.getEmail()));
			if(buser || bmail) 
			{
				path += "/GetModificaProfiloManager";
				if(buser) {path+="?erroru=1";}else{path+="?erroru=0";}
				if(bmail) {path+="&errormail=1";}else{path+="&errormail=0";}
			}
			else
			{
				
				uDao.updateManagerData(username, email, id);
				Utente newUser = uDao.getUserByUsername(username);
				s.setAttribute("account", newUser);
				//request.getSession().setAttribute("account",u);
				path += "/GetManagerPage";
				/*DA INSERIRE APPENA FATTA LA PAGINA IN JAVASCRIPTLavoratore u = lDao.createLavoratore(username,password);*/
				/**/
			}

			response.sendRedirect(path);
			
		}
		else if(change.equals("pwd"))
		{
			boolean bpwd;
			boolean checkEqualPwd = false;
			String opwd = request.getParameter("oldpwd");
			String npwd = request.getParameter("newpwd");
			String npwdc = request.getParameter("newpwdc");
			
		
			if(opwd == null || npwd == null || npwdc == null)
			{
				response.sendError(500, "Missing parameter");
				return;
			}
			
			bpwd = npwd.length()<8;
			
			if(npwd.equals(npwdc)) {checkEqualPwd = true;}
			
			Utente u2 = uDao.checkUser(u.getUsername(), opwd);
			   // String pathHome = path + "/GetWorkerPage";
			    //response.sendRedirect(pathHome);
		        if (u2 == null) {
		        	response.sendRedirect(path + "/GetModificaProfiloManager?errorlog=1");
		            return;
		        
		        } else {
			
		        	uDao.updateManagerPwd(npwd, id);
		        	path += "/GetManagerPage";
		        	if(bpwd) {path+="?errorpwd=1"; }else{path+="?errorpwd=0";}
		        	if(!checkEqualPwd) {path+="&errorcpwd=1"; }else{path+="&errorcpwd=0";}
		        	//?idCampagna="+idCampagna;
		        	response.sendRedirect(path);
		        }
		}

		
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

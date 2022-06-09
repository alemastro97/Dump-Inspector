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
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

/**
 * Servlet implementation class GetModificaProfiloManager
 */
@WebServlet("/GetModificaProfiloManager")
public class GetModificaProfiloManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	 private TemplateEngine templateEngine;
	 ServletContext context;  
   /**
    * @see HttpServlet#HttpServlet()
    */
   public GetModificaProfiloManager() {
       super();
       // TODO Auto-generated constructor stub
   }
   public void init() throws ServletException{
   	context = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
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
		String path = "/WEB-INF/ModificaProfiloManager.html";
		String loginpath = getServletContext().getContextPath() + "/GetLogin";
		
		String error_string = request.getParameter("errorlog");
	  String error_username_string = request.getParameter("erroru");
		String error_email_string  = request.getParameter("errormail");
		String error_pwdLength_string  = request.getParameter("errorpwd");
		String error_pwdConfirm_string = request.getParameter("errorcpwd");
		int error = 0, error_username = 0,error_email = 0, error_pwdLength = 0, error_pwdConfirm = 0;
		

	    
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
		if(error_string != null) {error = Integer.parseInt(error_string);}
		if(error_username_string != null) {error_username = Integer.parseInt(error_username_string);}
	    if(error_email_string != null) {error_email = Integer.parseInt(error_email_string);}
	    if(error_pwdLength_string != null) {error_pwdLength = Integer.parseInt(error_pwdLength_string);}
	    if(error_pwdConfirm_string != null) {error_pwdConfirm = Integer.parseInt(error_pwdConfirm_string);}
		final WebContext ctx = new WebContext(request, response, context, request.getLocale());
		/*int id = ((Utente) (s.getAttribute("account"))).getIdUtente();
		
		UtenteDAO uDao = new UtenteDAO(connection);*/
		
		ctx.setVariable("error",error);// else context.setVariable("error",0);
		ctx.setVariable("utente", u);
		ctx.setVariable("errorUsername",error_username); //else context.setVariable("errorUsername",0);
		ctx.setVariable("errorEmail",error_email); //else context.setVariable("errorEmail",0); 
		ctx.setVariable("errorPwdLength",error_pwdLength); //else context.setVariable("errorPwdLength",0);
		ctx.setVariable("errorPwdConfirm",error_pwdConfirm);// else context.setVariable("errorPwdConfirm",0);
		templateEngine.process(path, ctx, response.getWriter());
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

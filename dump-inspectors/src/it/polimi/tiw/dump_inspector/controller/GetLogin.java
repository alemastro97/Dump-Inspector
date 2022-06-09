package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;


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

import org.thymeleaf.context.WebContext;

@WebServlet("/GetLogin")
public class GetLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	ServletContext servletContext;

    public void init() throws ServletException {
        servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }
    
    public GetLogin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	   String error_string = request.getParameter("errorlog");
	   String error_username_string = request.getParameter("erroru");
	   String error_email_string  = request.getParameter("errormail");
	   String error_pwdLength_string  = request.getParameter("errorpwd");
	   String error_pwdConfirm_string = request.getParameter("errorcpwd");
	   int error = 0,error_username = 0,error_email = 0, error_pwdLength = 0, error_pwdConfirm = 0;
		
	    if(error_string != null) {error = Integer.parseInt(error_string);}
	    if(error_username_string != null) {error_username = Integer.parseInt(error_username_string);}
	    if(error_email_string != null) {error_email = Integer.parseInt(error_email_string);}
	    if(error_pwdLength_string != null) {error_pwdLength = Integer.parseInt(error_pwdLength_string);}
	    if(error_pwdConfirm_string != null) {error_pwdConfirm = Integer.parseInt(error_pwdConfirm_string);}
	    
	    request.getSession().setAttribute("account",null);
	   
	    
	    	String pathHome = "/WEB-INF/Login.html";
	        final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
	        context.setVariable("error",error);// else context.setVariable("error",0); 
	       	context.setVariable("errorUsername",error_username); //else context.setVariable("errorUsername",0);
	       	context.setVariable("errorEmail",error_email); //else context.setVariable("errorEmail",0); 
	       	context.setVariable("errorPwdLength",error_pwdLength); //else context.setVariable("errorPwdLength",0);
        	context.setVariable("errorPwdConfirm",error_pwdConfirm);// else context.setVariable("errorPwdConfirm",0);
	        templateEngine.process(pathHome, context, response.getWriter());
	        
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

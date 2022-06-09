package it.polimi.tiw.dump_inspector.controller;

import java.io.IOException;
import java.sql.SQLException;

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

/**
 * Servlet implementation class GetModificaProfiloWorker
 */
@WebServlet("/GetModificaProfiloWorker")
public class GetModificaProfiloWorker extends HttpServlet {
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
    public GetModificaProfiloWorker() {
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
		//	templateEngine.process(pathHome, context, response.getWriter());
	    	response.sendRedirect(pathHome);
	    	return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (u.isManager()) {
				response.sendRedirect(pathHome);
		        return;
			}
		}
		String path = "/WEB-INF/ModificaProfiloWorker.html";
		templateEngine.process(path, context, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}

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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.UtenteDAO;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;
    ServletContext servletContext;  

    public CheckLogin() {
        super();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");
        String path = servletContext.getContextPath();
        if (username == null || password == null) {
            response.sendRedirect(path + "/GetLogin?errorlog=1");
            return;
        }
        
        UtenteDAO uDao = new UtenteDAO(connection);
        Utente u = uDao.checkUser(username, password);
	 
        if (u == null) {

        	response.sendRedirect(path + "/GetLogin?errorlog=1");
            return;
        
        } else {

          request.getSession().setAttribute("account", u);
        }
        if(u.isManager()) {path += "/GetManagerPage";} else {path += "/GetWorkerPage";}
        response.sendRedirect(path);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

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

import it.polimi.tiw.dump_inspector.bean.Campagna;
import it.polimi.tiw.dump_inspector.bean.Localita;
import it.polimi.tiw.dump_inspector.bean.Utente;
import it.polimi.tiw.dump_inspector.dao.CampagnaDAO;
import it.polimi.tiw.dump_inspector.dao.LocalitaDAO;

/**
 * Servlet implementation class GetManagerMap
 */
@WebServlet("/GetManagerMap")
public class GetManagerMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private TemplateEngine templateEngine;
	private Connection connection = null;
	ServletContext context;

    public GetManagerMap() {
        super();
    }

    boolean isInArray(int c, ArrayList<Integer> arr)
    {
    	for(int i = 0; i < arr.size(); i++)
    	{
    		if(arr.get(i)==c)
    			return true;
    	}
    	return false;
    }
    public void init() throws ServletException {
        /* setup thymeleaf */
    	context = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		/* try to get database */
    	try {
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
		String loginpath = context.getContextPath() + "/GetLogin";
		Utente u = null;
		HttpSession s = request.getSession();
		if (s.isNew() || s.getAttribute("account") == null) {
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (Utente) s.getAttribute("account");
			if (!u.isManager()) {
				System.out.println("Account lavoratore, redirect a GetLogin");
				response.sendRedirect(loginpath);
				return;
			}
		}
		
		String id = request.getParameter("idCampagna");
		LocalitaDAO lDao = new LocalitaDAO(connection);
		List <Localita> l = new ArrayList<Localita>();
		ArrayList <Integer> codes = new ArrayList<Integer>();
		ArrayList <Integer> yellow = lDao.getLocalitaSenzaAnnotazioni(Integer.parseInt(id));
		ArrayList <Integer> reds = lDao.getLocalitaConConflitti(Integer.parseInt(id));
		
		

		l = lDao.searchLocalitaCampagna(Integer.parseInt(id));
		
		for(int i = 0; i < l.size(); i++)
		{
			if(isInArray(l.get(i).getIdLocalita(),yellow))
			{
				codes.add(1);
			}
			else if(isInArray(l.get(i).getIdLocalita(),reds))
			{
				codes.add(2);
			}
			else
			{
				codes.add(0);
			}
					
			
		}
		
		
		String path = "/WEB-INF/ManagerMap.html";
		final WebContext ctx = new WebContext(request, response, context, request.getLocale());
		ctx.setVariable("localita", l);
		ctx.setVariable("codes", codes);
		templateEngine.process(path, ctx, response.getWriter());
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

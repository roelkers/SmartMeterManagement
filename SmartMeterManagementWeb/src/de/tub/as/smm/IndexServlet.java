package de.tub.as.smm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class UserServlet
 */


@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	/**
	 * Sendet Übersichtsseite an Client
	 */
    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    
   /**
    * Trägt neuen User im HTTPSession-Objekt ein. Alle weiteren Ablesungen werden mit dessen Namen eingetragen
    */
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		String userName = request.getParameter("userName");
		
		request.getSession().setAttribute("userName",userName);
		
		System.out.println("Name" + userName);

    }
}


package de.tub.as.smm;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tub.as.smm.dao.SmartMeterDao;
import de.tub.as.smm.models.SmartMeter;

import com.google.gson.Gson;
/**
 * Servlet implementation class UserServlet
 */


@WebServlet("/meter")
public class SmartMeterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Injected DAO EJB:
    @EJB
    SmartMeterDao smartMeterDao;
    
    /**
     * Sendet Liste der SmartMeters an Client. Diese werden in das JSON-Format (Javascript Object Notation) umgewandelt,
     * damit der Browser dieses erkennt. Die SmartMeter werden vom zugehörigen Data Access Object angefragt.
     */
    @Override
    protected void doGet(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	//Erhalte alle Stromzähler von Persistence API
    	List<SmartMeter> meterList = smartMeterDao.getAllSmartMeters();
        
        System.out.println(meterList.toString());
        
        Gson gson = new Gson();
        
        //Umwandlung in JSON
        String jsonResponse = gson.toJson(meterList);
        
        response.setContentType("text/x-json;charset=UTF-8");           
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(jsonResponse);
    }
    
    /**
     * Empfängt Daten für ein neues SmartMeter und speichert dieses persistent in der Datenbank.
     * Dabei werden vom Client der Name, die erlaubte Stromstärke und das Bild des Stromzäghlers abgefragt.
     */
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	System.out.println(request.toString());
    	
        // Handle a new SmartMeter:
        String name = request.getParameter("name");
        String imgSrc = request.getParameter("imgSrc");
        double allowedCurrent = Double.parseDouble(request.getParameter("allowedCurrent"));
        
        System.out.println(name);
        System.out.println(allowedCurrent);
        
        SmartMeter smartmeter = new SmartMeter(name,imgSrc,allowedCurrent);
        
        System.out.println(smartmeter);
        
        //Speichern des SmartMeter (persistent)
        smartMeterDao.persist(smartmeter);
        
        doGet(request, response);
    }
}


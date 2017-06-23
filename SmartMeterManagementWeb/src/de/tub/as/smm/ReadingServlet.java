package de.tub.as.smm;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tub.as.smm.dao.SmartMeterDao;
import de.tub.as.smm.models.Reading;
import de.tub.as.smm.models.SmartMeter;

import com.google.gson.Gson;

/**
 * Servlet implementation class UserServlet
 */

@WebServlet("/reading")
public class ReadingServlet extends HttpServlet {
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
     * Speichert eine Ablesung eines Stromzählers persistent ab. Dafür wird der Username vom Session Bean angefragt.
     * Anschließend wird die Ablesung mit der Kilowattzahl und dem Usernamen mithilfe des Session Beans gespeichert
     * 
     */
    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	System.out.println(request.toString());
    	
    	if(request.getSession().getAttribute("userName") != null){
    		
    		System.out.println(request.getParameter("kiloWattHours"));
    		double kiloWattHours = Double.parseDouble(request.getParameter("kiloWattHours"));
			long meterId = Long.parseLong(request.getParameter("id"));
			
			System.out.println("meterId: "+meterId);
			
			String userName = (String) request.getSession().getAttribute("userName");
			
			System.out.print(userName);
			
			SmartMeter smartMeter = smartMeterDao.getSmartMeterById(meterId);
			
			//Erzeugt neues Datum
			Date readDate = new Date(System.currentTimeMillis());

			//Erstellt neue Ablesung
			Reading reading = new Reading(userName,readDate,kiloWattHours);
			
			System.out.println(reading.toString());
			
			//Fügt Ablesung hinzu
			smartMeter.addReading(reading);
			
			//Persistentes Update des Stromzählers
			smartMeterDao.update(smartMeter);
			
			System.out.println(smartMeter.toString());
			
			//Sende alle Stromzähler
			doGet(request, response);	
    		
    	}

    	else {
    		//User is NOT Authorized. Request should fail because he is not logged in.
    		response.setContentType("text;charset=UTF-8");           
            response.setHeader("Cache-Control", "no-cache");
            //HTPP Error: 401 Unauthorized Access
            response.setStatus(401);
    		response.getWriter().write("ERR: not logged in");
    	}
    }
}
         


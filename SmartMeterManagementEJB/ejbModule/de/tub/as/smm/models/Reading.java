package de.tub.as.smm.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Reading implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	Long Id;
	private String userName;
	private Date date;
	private double kiloWattHours;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getKiloWattHours() {
		return kiloWattHours;
	}

	public void setKiloWattHours(double kiloWattHours) {
		this.kiloWattHours = kiloWattHours;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Reading(){
		
	}

	public Reading(String userName, Date date, double kiloWattHours) {
		super();
		this.userName = userName;
		this.date = date;
		this.kiloWattHours = kiloWattHours;
	}

	@Override
	public String toString() {
		return "Reading [userName=" + userName + ", date=" + date + ", kiloWattHours=" + kiloWattHours + "]";
	}

	
}

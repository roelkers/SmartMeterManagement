package de.tub.as.smm.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;

@Entity
public class SmartMeter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	Long Id;
	private String name;
	private String imgSrc;
	private double allowedCurrent;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL}) //targetEntity=Reading.class, mappedBy ="SmartMeter",
	private List<Reading> readings;
	
	public SmartMeter(){
	}
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public double getAllowedCurrent() {
		return allowedCurrent;
	}

	public void setAllowedCurrent(double allowedCurrent) {
		this.allowedCurrent = allowedCurrent;
	}

	public List<Reading> getReadings() {
		return readings;
	}

	public void setReadings(List<Reading> readings) {
		this.readings = readings;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public SmartMeter(String name, String imgSrc, double allowedCurrent){
		this.name = name;
		this.imgSrc = imgSrc;
		this.allowedCurrent = allowedCurrent;
		this.readings = new ArrayList<Reading>();
	}
	
	public void addReading(Reading reading){
		readings.add(reading);
	}
	
	public String toString(){
		return name + "( allowed Current:" + allowedCurrent + ", imgSrc :" + imgSrc + "readings" + readings.toString() + ")";
	}
}

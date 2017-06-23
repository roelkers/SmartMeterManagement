package de.tub.as.smm.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.tub.as.smm.models.SmartMeter;

/**
 * Session Bean implementation class SmartMeterDao
 */

@Stateless
@LocalBean
public class SmartMeterDao {

	@PersistenceContext private EntityManager entityManager;
	
	public SmartMeterDao(){
		
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void persist(SmartMeter smartmeter){
		entityManager.persist(smartmeter);
	}
	
	public void update(SmartMeter smartMeter){
		entityManager.merge(smartMeter);
	}
	
	public SmartMeter getSmartMeterById(long meterId){
		System.out.println("smDao: "+meterId);
		
		TypedQuery<SmartMeter> query = entityManager.createQuery(
        		"SELECT sm FROM SmartMeter sm WHERE sm.Id = :Id", SmartMeter.class)
				.setParameter("Id", meterId);
        return query.getSingleResult(); 
	}
	
    public List<SmartMeter> getAllSmartMeters(){
        TypedQuery<SmartMeter> query = entityManager.createQuery(
        		"SELECT sm FROM SmartMeter sm ORDER BY sm.Id", SmartMeter.class);
        return query.getResultList();    	
    }
}

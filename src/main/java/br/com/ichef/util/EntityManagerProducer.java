package br.com.ichef.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;
	@SuppressWarnings("unused")
	private EntityManager manager;

	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("PU");
	}

	@Produces
	@Dependent
	public EntityManager createEntityManager() {
		return this.factory.createEntityManager();
	}
	
	
	public void closeEntityManager(@Disposes EntityManager manager) {
		this.manager = manager;
		manager.close();
	}
	
	
	public EntityManagerFactory getFactory() {
		return factory;
	}

}

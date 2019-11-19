package br.com.ichef.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

@ApplicationScoped
public class EntityManagerProducer {

	private  EntityManagerFactory factory;

	 
	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("PU");
		System.out.println("CONEXAO ABERTA");
	}

	@Produces
	public EntityManager createEntityManager() {
		return this.factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
		System.out.println("CONEXAO FECHADA");
	}

}

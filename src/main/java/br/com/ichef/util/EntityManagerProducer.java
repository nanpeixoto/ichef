package br.com.ichef.util;

import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;

	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("PU");
		// System.out.println("CONEXAO ABERTA - EntityManagerProducer");
	}

	@Produces
	public EntityManager createEntityManager() {
		// System.out.println("CONEXAO ABERTA - createEntityManager");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		return this.factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager manager) {
		// System.out.println("CONEXAO FECHADA");
		if (manager.isOpen())

			manager.close();

	}

}

package br.com.ichef.listener;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.com.ichef.scheduler.CronSchedulerEmail;
import br.com.ichef.service.EmailService;


@WebListener
public class CronWebListener implements ServletContextListener {

	@Inject
	private EmailService emailService;
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {

			System.out.println("Iniciando Listener JOB: emailService");
			new CronSchedulerEmail(emailService, sce);
			System.out.println("Parando Listener JOB: emailService");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Parando Listener JOB");
	}

}

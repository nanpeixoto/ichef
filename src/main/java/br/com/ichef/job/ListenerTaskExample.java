package br.com.ichef.job;

import javax.inject.Inject;
//import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.ichef.service.EmailService;



public class ListenerTaskExample implements ServletContextListener {
	private Thread t = null;
	//private ServletContext context;

	@Inject
	private EmailService service;
	
	public void contextInitialized(ServletContextEvent contextEvent) {
		t = new Thread() {
			// task
			public void run() {
				try {
					while (true) {
						System.out.println("Thread running every second");
						Thread.sleep(960000);
						service.enviarEmails();
					}
				} catch (InterruptedException e) {
				}
			}
		};
		t.start();
	//	context = contextEvent.getServletContext();
		// you can set a context variable just like this
		//context.setAttribute("TEST", "TEST_VALUE");
		
		
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		// context is destroyed interrupts the thread
		t.interrupt();
	}
}
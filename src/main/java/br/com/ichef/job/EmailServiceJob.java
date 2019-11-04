package br.com.ichef.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import br.com.ichef.service.EmailService;

public class EmailServiceJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		SchedulerContext schedulerContext = null;

		try {

			schedulerContext = arg0.getScheduler().getContext();
			EmailService service = (EmailService) schedulerContext.get("EmailService");
			service.enviarEmails();

		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package br.com.ichef.scheduler;

public class CronSchedulerEmail {

	/*private static SchedulerFactory sf = new StdSchedulerFactory();

	private static EmailService service;
	
	private static ServletContextEvent sce ;

	public CronSchedulerEmail( EmailService service, ServletContextEvent sce ) throws Exception {
		setSce( sce );
		setService( service );
		criarScheduler("Email", "EmailServiceJob", EmailServiceJob.class,"0 0/2 * * * ?");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void criarScheduler(String nomeJob, String grupoJob,
			Class classeJob, String expressao) throws Exception {

		Scheduler sche = sf.getScheduler();

		JobDetail jDetail = JobBuilder.newJob(classeJob)
				                      .withIdentity(nomeJob, grupoJob)
				                      .build();

		CronTrigger crTrigger = TriggerBuilder.newTrigger()
				                              .withIdentity(nomeJob, grupoJob)
				                              .withSchedule(CronScheduleBuilder.cronSchedule(expressao))
				                              .build();

		JobKey jobKey = new JobKey(nomeJob, grupoJob);

		sche.deleteJob(jobKey);

		sche.scheduleJob(jDetail, crTrigger);

		sche.getContext().put( "EmailService", getService() );
		sche.getContext().put( "ServletContextEvent", getSce() );
		
		sche.start();
	}

	

	public static EmailService getService() {
		return service;
	}

	public static void setService(EmailService service) {
		CronSchedulerEmail.service = service;
	}

	public static ServletContextEvent getSce() {
		return sce;
	}

	public static void setSce(ServletContextEvent sce) {
		CronSchedulerEmail.sce = sce;
	}*/
}

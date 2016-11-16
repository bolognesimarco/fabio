package com.bolo.photoshooters.quartz;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class QuartzListener extends QuartzInitializerListener {
	@Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        ServletContext ctx = sce.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        try {
            Scheduler scheduler = factory.getScheduler();
            System.out.println("Prepare Quartz..");
            
//            JobDetail testJobDetail = JobBuilder.newJob(Testjob.class).build();
//            Trigger testTrigger = TriggerBuilder.newTrigger().withIdentity("test").withSchedule(
//                    CronScheduleBuilder.cronSchedule("0 0 0/1 1/1 * ? *")).startNow().build();
            
//          controllo se ci sono utenti non attivati entro 60gg  da cancellare ogni giorno alle 00:00         
            JobDetail unaJobDetail = JobBuilder.newJob(UtentiNonAttiviJob.class).build();
            Trigger unaTrigger = TriggerBuilder.newTrigger().withIdentity("una").withSchedule(
                    CronScheduleBuilder.cronSchedule("	0 0 0 1/1 * ? *")).startNow().build();
          
//            controllo se ci sono email da inviare ogni minuto
            JobDetail emailJobDetail = JobBuilder.newJob(InvioEmailjob.class).build();
            Trigger emailTrigger = TriggerBuilder.newTrigger().withIdentity("email.").withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).startNow().build();
            
//            scheduler.scheduleJob(testJobDetail, testTrigger);
            scheduler.scheduleJob(unaJobDetail, unaTrigger);
            scheduler.scheduleJob(emailJobDetail, emailTrigger);
            
            scheduler.start();
        } catch (Exception e) {
            ctx.log("There was an error scheduling the job.", e);
        }
    }
}

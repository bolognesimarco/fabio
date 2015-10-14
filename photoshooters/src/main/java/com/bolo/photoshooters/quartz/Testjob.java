package com.bolo.photoshooters.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Testjob implements Job {

	@Override
    public void execute(final JobExecutionContext ctx)
            throws JobExecutionException {

        System.out.println("JOB IN ESECUZIONE (quartz)");

    }

}

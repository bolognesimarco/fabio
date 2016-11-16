package com.bolo.photoshooters.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;

public class InvioEmailjob implements Job{
	
	private ServiziVari serv = new ServiziVariImpl();
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			System.out.println("JOB EMAILDAINVIARE IN ESECUZIONE (quartz)");
			serv.invioEmailDaInviare();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
	
}

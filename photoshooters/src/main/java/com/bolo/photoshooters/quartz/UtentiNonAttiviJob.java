package com.bolo.photoshooters.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;

public class UtentiNonAttiviJob implements Job {
	
	private ServiziVari serv = new ServiziVariImpl();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			serv.cancellaUtentiNonAttivati();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

}

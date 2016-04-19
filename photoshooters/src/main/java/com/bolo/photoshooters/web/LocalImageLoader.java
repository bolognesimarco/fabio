package com.bolo.photoshooters.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LocalImageLoader
 */
@WebServlet("/lil")
public class LocalImageLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocalImageLoader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAll(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAll(request, response);
	}
	
	
	String pathBase = "c:/temp/";
	protected void doAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("LIL:"+request.getParameter("path"));
		
		
		OutputStream out = response.getOutputStream();
		String path = request.getParameter("path");
		
		File ilFileRichiesto = new File(pathBase+path);
		if(!ilFileRichiesto.exists()){
			
		}
		
		FileInputStream fis = new FileInputStream(ilFileRichiesto);
		byte[] buff = new byte[1024];
		while(fis.read(buff)>-1){
			out.write(buff);
		}
		fis.close();
		out.close();
	}

}

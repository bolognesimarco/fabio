package com.bolo.photoshooters.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;

public class RegistrationServlet extends HttpServlet {
	
	private ServiziVari serviziVari = new ServiziVariImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Utente u = serviziVari.activateUser(request.getParameter("activationCode"));
			if(u==null){
				response.sendRedirect("errorPage.xhtml");
			}else{
				
				ContentBean contentBean = new ContentBean();
				request.getSession(true).setAttribute("contentBean", contentBean);
				
				UtenteBean bean = new UtenteBean();
				ParametersBean pBean = new ParametersBean();
				bean.setUtente(u);
				bean.setContentBean(contentBean);
				u.setOnline(true);
				request.getSession(true).setAttribute("utenteBean", bean);
				
				MenuBean menu = new MenuBean();
				menu.setContentBean(contentBean);
				menu.setUtenteBean(bean);
				menu.messaggioAvvenutaRegistrazione();
				request.getSession(true).setAttribute("menuBean", menu);
				response.sendRedirect("/");
				pBean.fillSelectItems();

				
				InputBean inputBean = new InputBean();
				inputBean.getVotoFoto().setScore(-1);
				inputBean.setStatusMessage("");
				request.getSession(true).setAttribute("inputBean", inputBean);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}

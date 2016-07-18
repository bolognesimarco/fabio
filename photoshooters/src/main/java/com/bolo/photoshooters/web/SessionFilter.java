package com.bolo.photoshooters.web;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class SessionFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
		System.out.println("SESSION FILTER-SESSIONE ATTIVA:"+(request.getSession(false)==null?"NO":"SI"));
        if(request.getSession(false)==null){
        	HttpSession sess = request.getSession();
//        	
//        	UtenteBean utenteBean = (UtenteBean ) sess.getAttribute("utenteBean");
//        	System.out.println("utenteBean:"+utenteBean);
    		//metodo su utenteBean che setta hopme e refresh menùù
//    		utenteBean.sessioneDistrutta();
//        	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("menuutenteform");
//    		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("headerform");
//    		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("patternguestform");
//    		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
//        	response.sendRedirect(request.getContextPath()+"/error.html");
        	chain.doFilter(req, res);
        }else{

        	chain.doFilter(req, res);	
        }       	
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

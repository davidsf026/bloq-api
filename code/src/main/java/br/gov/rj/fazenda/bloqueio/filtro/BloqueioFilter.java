package br.gov.rj.fazenda.bloqueio.filtro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import ssa.control.servlet.FiltroAutorizacao;

public class BloqueioFilter extends FiltroAutorizacao {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	//if (((HttpServletRequest)request).getRequestURI().contains("inicio.jsf")) {
    		//System.out.println(((HttpServletRequest)request).getRequestURI());	
    	//}
    	
    	super.doFilter(request, response, chain);
    }
	
}



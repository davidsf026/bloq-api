package br.gov.rj.fazenda.bloqueio.exception;

import org.apache.log4j.Logger;
	
public class BloqueioException extends RuntimeException {
    
    private final Logger log = Logger.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;
    
    public BloqueioException(String message, Throwable cause) {
        super(message + "\n" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(cause), cause);     
        log.error(message + "\n" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(cause));
    }   
}

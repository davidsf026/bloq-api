package br.gov.rj.fazenda.service.bdexecute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fpeclat
 * 12/11/2014
 */
public class DBPersistenceException extends RuntimeException {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;
    
    public DBPersistenceException(String message, Throwable cause) {
        super(message + "\n" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(cause), cause);     
        log.error(message + "\n" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(cause));
    }
    

}

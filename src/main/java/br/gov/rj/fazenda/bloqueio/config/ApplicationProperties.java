package br.gov.rj.fazenda.bloqueio.config;

import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;


public class ApplicationProperties {

	public static final Logger LOG = Logger.getLogger(ApplicationProperties.class);
	
	public static final String FILE_NAME = "/bloqueio.properties";
	
	protected static InitialContext initialContext;	
	protected static Properties bundle = null;
		
	protected ApplicationProperties() {
		
	}
	
	public static String obterRecurso(String chave) {
		
		String propertie = System.getenv(chave);
		
		if(propertie == null || propertie.equals("")) {
			if(bundle == null) {
				bundle = new Properties();
				initRecurso();
			}
			
			return bundle.getProperty(chave);
		}
		
		return propertie;
	}
	
	private static void initRecurso() {
		
		try {
			bundle.load(ApplicationProperties.class.getResourceAsStream(FILE_NAME));
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível encontrar o arquivo " + FILE_NAME + ".", e);
		}
	}

}

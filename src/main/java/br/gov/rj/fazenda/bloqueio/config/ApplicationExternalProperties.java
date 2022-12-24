package br.gov.rj.fazenda.bloqueio.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;


/**
 * Responsável por buscar as configurações da aplicação, no servidor.
 * @author SUAF
 */
public class ApplicationExternalProperties {

	protected static Logger LOG = Logger.getLogger(ApplicationExternalProperties.class);
	
	protected static InitialContext initialContext;
	
	protected static Properties bundle = null;

	//public static final Path CONFIG_EXTERNAL_FILE_PATH = Paths.get(ApplicationProperties.obterRecurso("config.file.external.path"));	
	public static final Path CONFIG_EXTERNAL_FILE_PATH = Paths.get(ApplicationProperties.obterRecurso("CONFIG_EXTERNAL_FILE_PATH"));
	
	public static final boolean SEGURANCA_UTILIZAR_SENHAS_CRIPTOGRAFADAS = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("seguranca.utilizar.senhas.criptografadas"));
	
	public static final boolean BLOQUEIO_LDAP_HABILITADO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.ldap.habilitado"));
	public static final boolean BLOQUEIO_BD_HABILITADO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.bd.habilitado"));
	public static final boolean BLOQUEIO_SISPAT_HABILITADO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.sispat.habilitado"));
	public static final boolean BLOQUEIO_SIAFERIO_HABILITADO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.siaferio.habilitado"));
	public static final boolean BLOQUEIO_SSA_HABILITADO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.ssa.habilitado"));

	public static final boolean BLOQUEIO_USUARIO_TESTE_ATIVO = Boolean.valueOf(ApplicationExternalProperties.obterRecurso("bloqueio.usuario.teste.ativo"));
	public static final String BLOQUEIO_USUARIO_TESTE_LOGIN = ApplicationExternalProperties.obterRecurso("bloqueio.usuario.teste.login");
	public static final String BLOQUEIO_USUARIO_TESTE_NOME = ApplicationExternalProperties.obterRecurso("bloqueio.usuario.teste.nome");
	public static final String BLOQUEIO_USUARIO_TESTE_CPF = ApplicationExternalProperties.obterRecurso("bloqueio.usuario.teste.cpf");
	public static final String BLOQUEIO_USUARIO_TESTE_MATRICULA = ApplicationExternalProperties.obterRecurso("bloqueio.usuario.teste.matricula");

	public static final String BLOQUEIO_BD_FUNCTION_BLOQUEIO_USUARIO_BANCO = ApplicationExternalProperties.obterRecurso("bloqueio.bd.function.bloqueio.usuario");
	public static final Collection<String> KEYS_PROPERTY_BLOQUEIO_BANCOS = ApplicationExternalProperties.obterChavesPorPrefixo("bloqueio.bd.jdbc.informacoes");

	public static final String BLOQUEIO_BD_FUNCTION_BLOQUEIO_SISPAT = ApplicationExternalProperties.obterRecurso("bloqueio.sispat.function.bloqueio.usuario");
	public static final List<String> KEYS_PROPERTY_BLOQUEIO_SISPAT = ApplicationExternalProperties.obterChavesPorPrefixo("bloqueio.sispat.jdbc.informacoes");
	
	public static final String BLOQUEIO_BD_FUNCTION_BLOQUEIO_SIAFE_RIO = ApplicationExternalProperties.obterRecurso("bloqueio.siaferio.function.bloqueio.usuario");
	public static final List<String> KEYS_PROPERTY_BLOQUEIO_SIAFE_RIO = ApplicationExternalProperties.obterChavesPorPrefixo("bloqueio.siaferio.jdbc.informacoes");
	
	public static final String BLOQUEIO_BD_FUNCTION_BLOQUEIO_SSA = ApplicationExternalProperties.obterRecurso("bloqueio.ssa.function.bloqueio.usuario");
	public static final List<String> KEYS_PROPERTY_BLOQUEIO_SSA = ApplicationExternalProperties.obterChavesPorPrefixo("bloqueio.ssa.jdbc.informacoes");
	
	public static final String EMAIL_RELATORIO_APLICACAO = ApplicationExternalProperties.obterRecurso("email.relatorio.applicacao");
	public static final String EMAIL_RELATORIO_HASHSEC = ApplicationExternalProperties.obterRecurso("email.relatorio.hashsec");
	public static final String EMAIL_RELATORIO_URI = ApplicationExternalProperties.obterRecurso("email.relatorio.uri");
	public static final String EMAIL_RELATORIO_FROM = ApplicationExternalProperties.obterRecurso("email.relatorio.from");
	public static final String EMAIL_RELATORIO_TO = ApplicationExternalProperties.obterRecurso("email.relatorio.to");
	
	public static final String ACTIVE_DIRECTORY_ENDERECO = ApplicationExternalProperties.obterRecurso("adserver.endereco");
	public static final String ACTIVE_DIRECTORY_USUARIO = ApplicationExternalProperties.obterRecurso("adserver.usuario");
	public static final String ACTIVE_DIRECTORY_SENHA = ApplicationExternalProperties.obterRecurso("adserver.senha");
	
	public static final String QUERY_CONSULTA_FUNCIONARIO_INTERNO = ApplicationExternalProperties.obterRecurso("query.consulta.funcionario.interno");
	public static final String QUERY_CONSULTA_FUNCIONARIO_EXTERNO = ApplicationExternalProperties.obterRecurso("query.consulta.funcionario.externo");
	
	public static final String CONFIG_FILE_NAME="/config.properties";
	
	protected ApplicationExternalProperties() {
		
	}
	
	/**
	 * @param chave
	 * @return
	 */
	public static String obterRecurso(String chave) {
		if(bundle == null) {
			bundle = new Properties();
			try {
				initRecurso();
			} catch (MalformedURLException e) {
				LOG.error("Erro ao tentar obter Recurso: " + e);
			}
		}
		
		if (bundle.getProperty(chave) != null) {
			return bundle.getProperty(chave);
		} else {
			if (System.getProperty(chave) != null) {
				return System.getProperty(chave);
			}
			
		}
		throw new RuntimeException("Propriedade '" + chave + "' não encontrada!", new Exception("Propriedade não encontrada"));
	}
	
	
	public static List<String> obterChavesPorPrefixo(String prefixoChave) {
		if(bundle == null) {
			bundle = new Properties();
			try {
				initRecurso();
			} catch (MalformedURLException e) {
				LOG.error("Erro ao tentar obter Recurso: " + e);
			}
		}
		
		List<String> keys = new ArrayList<String>();
		
		Hashtable<Object,Object> bundleCast = ((java.util.Hashtable<Object,Object>) bundle);
		Collection<Object> col = bundleCast.keySet();
		
		
		Iterator<Object> bundleIt = col.iterator();
		
		while (bundleIt.hasNext()) {
			String keyFound = (String)bundleIt.next();
			if (keyFound.startsWith(prefixoChave)) {
				keys.add(keyFound);
			}
		}
		
		Collections.sort(keys);
		
		Collections.sort(keys, new Comparator<String>() {

			public int obterNumeroAmbiente(String rotulo) {
				String sufixo = rotulo.substring(rotulo.length() - 1, rotulo.length()).toUpperCase();
				return (sufixo.equals("D") ? 1
						: (sufixo.equals("H") ? 2 : (sufixo.equals("B") ? 3 : (sufixo.equals("P") ? 4 : 0))));
			}

			public int obterNumero(String rotulo) {
				String sufixo = rotulo.substring(rotulo.length() - 3, rotulo.length() - 1).toUpperCase();
				return (sufixo.equals("D") ? 1
						: (sufixo.equals("H") ? 2 : (sufixo.equals("B") ? 3 : (sufixo.equals("P") ? 4 : 0))));
			}

			public int compare(String o1, String o2) {
				
				try {
				
					if (obterNumeroAmbiente(o1) < obterNumeroAmbiente(o2)) {
						return -1;
					} else if (obterNumeroAmbiente(o1) > obterNumeroAmbiente(o2)) {
						return 1;
					} else {
						if (obterNumero(o1) < obterNumero(o2)) {
							return -1;
						} else if (obterNumero(o1) > obterNumero(o2)) {
							return 1;
						} else {
							return 0;
						}
					}
				} catch (Exception e) {
					return 0;
				}
			}
		});

		
		return keys;
		
		//throw new RuntimeException("Propriedade '" + prefixoChave + "' não encontrada!", new Exception("Propriedade não encontrada"));
	}
	
	
	/**
	 * Responsável por iniciar os recursos
	 * @throws MalformedURLException 
	 */
	private static void initRecurso() throws MalformedURLException {
		
		Path path = Paths.get(CONFIG_EXTERNAL_FILE_PATH + CONFIG_FILE_NAME);
		
		LOG.warn("O path do arquivo externo é '" + path.toUri().toURL() + "'.");
		
		URL resource = null;
		
		try {
			
			resource = path.toUri().toURL();
			bundle.load(new java.io.FileInputStream(resource.getPath()));
			
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível encontrar o arquivo " + resource.getPath(), e);
		}
	}
	
}

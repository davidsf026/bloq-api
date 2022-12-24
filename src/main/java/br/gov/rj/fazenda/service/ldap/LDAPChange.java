/*
 * Classe de implementacao de funcionalidades do LDAP para auxilio de administacao do ambiente
 * 
 */
package br.gov.rj.fazenda.service.ldap;

import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.security.CriptografiaUtil;
import br.gov.rj.fazenda.service.ldap.LDAPUsuario.StatusUsuarioAD;

/**
 * Class LDAPChange.
 */
public class LDAPChange {

	public static Logger log = Logger.getLogger(LDAPChange.class);
	
	/** The dir context. */
	static InitialDirContext dirContext = null;

	/** The Constant ldapAdServer. */
	private final static String ldapAdServer = ApplicationExternalProperties.ACTIVE_DIRECTORY_ENDERECO;
	
	//exoneracao.dev
	// D#347@$qw2
	//distinguishedName = CN=exoneracao.DEV,OU=EXONERAR,OU=CONTAS_SERVICOS,OU=SEFAZ,DC=sefnet,DC=rj

	/** The Constant distinguishedName. */
	private final static String distinguishedName = "CN=" + ApplicationExternalProperties.ACTIVE_DIRECTORY_USUARIO + ",OU=EXONERAR,OU=CONTAS_SERVICOS,OU=SEFAZ,DC=sefnet,DC=rj";
	
	/** The Constant ldapPassword. */
	private final static String ldapPassword =  (ApplicationExternalProperties.SEGURANCA_UTILIZAR_SENHAS_CRIPTOGRAFADAS ? CriptografiaUtil.decrypt(ApplicationExternalProperties.ACTIVE_DIRECTORY_SENHA, CriptografiaUtil.secretKey) : ApplicationExternalProperties.ACTIVE_DIRECTORY_SENHA);
	
	/** The Constant ldapSearchBase. */
	private final static String ldapSearchBase = "DC=sefnet,DC=rj";
	
	/** The sr ldap users. */
	private static NamingEnumeration<SearchResult> srLdapUsers = null;
	
	
	/** The account enable. */
	private static int ACCOUNT_ENABLE  = 0x0200;
	
	/** The account disable. */
	private static int ACCOUNT_DISABLE = 0x0202;

	/**
	 * Gets the account enable.
	 *
	 * @return the aCCOUNT_ENABLE
	 */
	public static int getACCOUNT_ENABLE() {
		return ACCOUNT_ENABLE;
	}

	/**
	 * Gets the account disable.
	 *
	 * @return the aCCOUNT_DISABLE
	 */
	public static int getACCOUNT_DISABLE() {
		return ACCOUNT_DISABLE;
	}



	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {

		
	}

	/**
	 * Alterar office.
	 *
	 * @param user the user
	 * @param office the office
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String alterarOffice(String user, String office) throws Exception {
		String retorno = "0";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		//properties.save(out, comments);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}

		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser = (SearchResult) srLdapUsers.nextElement();

			// Attribute atr = (Attribute)
			// srLdapUser.getAttributes().get("physicalDeliveryOfficeName");
			String dn = (String) srLdapUser.getAttributes().get("distinguishedName").toString();

			ModificationItem[] mods = new ModificationItem[1];

			// replace the physicalDeliveryOfficeName
			Attribute A0 = new BasicAttribute("physicalDeliveryOfficeName", office);
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, A0);

			dirContext.modifyAttributes((dn.split(":")[1]).trim(), mods);
		} else {
			retorno = "1";
		}

		return retorno;
	}

	/**
	 * Desb hab usuario.
	 *
	 * @param user the user
	 * @param oper the oper
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String desbHabUsuario(String user , int oper) throws Exception {
		String retorno = "0";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		
		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}

		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);


		
		
		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser = (SearchResult) srLdapUsers.nextElement();
			
			
			
			
			
			// Attribute atr = (Attribute)
			 if ( !srLdapUser.getAttributes().get("userAccountControl").toString().isEmpty())
				 System.out.println("ok tem valor");
			
			
			
			
			
			
			
			

			// Attribute atr = (Attribute)
			// srLdapUser.getAttributes().get("physicalDeliveryOfficeName");
			String dn = (String) srLdapUser.getAttributes().get("distinguishedName").toString();

			ModificationItem[] mods = new ModificationItem[1];

			// replace the physicalDeliveryOfficeName
			// Attribute A0 = new BasicAttribute("physicalDeliveryOfficeName", office);
			// mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, A0);

			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("userAccountControl", Integer.toString(oper)));

			dirContext.modifyAttributes((dn.split(":")[1]).trim(), mods);
		} else {
			retorno = "1";
		}

		return retorno;
	}

	/**
	 * Consultar.
	 *
	 * @param user the user
	 * @return the string
	 * @throws Exception the exception
	 */
	public static LDAPUsuario consultar(String user) throws Exception {
		String retorno = "";
		String atributos = "";
		
		LDAPUsuario ldapUsuario = new LDAPUsuario();

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}

		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser;
			srLdapUser = (SearchResult) srLdapUsers.nextElement();

			ldapUsuario.setLogin(user);
			ldapUsuario.setNome(srLdapUser.getAttributes().get("displayName").get().toString());
			
			String userAccountControlString = srLdapUser.getAttributes().get("userAccountControl").get().toString();
			if (userAccountControlString != null) {
				if (userAccountControlString.equals(StatusUsuarioAD.CONTA_ATIVA.valor.toString())) {
					ldapUsuario.setStatusUsuarioAD(StatusUsuarioAD.CONTA_ATIVA);
				} else if (userAccountControlString.equals(StatusUsuarioAD.CONTA_BLOQUEADA.valor.toString())) {
					ldapUsuario.setStatusUsuarioAD(StatusUsuarioAD.CONTA_BLOQUEADA);
				} else if (userAccountControlString.equals(StatusUsuarioAD.CONTA_BLOQUEADA_SENHA_NAO_EXPIRA.valor.toString())) {
					ldapUsuario.setStatusUsuarioAD(StatusUsuarioAD.CONTA_BLOQUEADA_SENHA_NAO_EXPIRA);
				} else {
					ldapUsuario.setStatusUsuarioAD(StatusUsuarioAD.CONTA_STATUS_DESCONHECIDO);
				}
			}

		} else {
			ldapUsuario.setStatusUsuarioAD(StatusUsuarioAD.CONTA_STATUS_NAO_ENCONTRADO);
		}

		return ldapUsuario;
	}

	/**
	 * Consultar office.
	 *
	 * @param user the user
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String consultarOffice(String user) throws Exception {
		String retorno = "";
		String atributos = "";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}

		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser;
			srLdapUser = (SearchResult) srLdapUsers.nextElement();

			atributos = (String) srLdapUser.getAttributes().get("physicalDeliveryOfficeName").toString();
			retorno = retorno + (atributos.split(":")[1]).trim();
		}
		if ("".equals(retorno)) {
			retorno = "Usuario não existe ou senha não confere !";
		}

		return retorno;
	}

	/**
	 * Consultar DN.
	 *
	 * @param user the user
	 * @return the string
	 * @throws Exception the exception
	 */
	public static String consultarDN(String user) throws Exception {
		String retorno = "";
		String atributos = "";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}

		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser;
			srLdapUser = (SearchResult) srLdapUsers.nextElement();

			atributos = (String) srLdapUser.getAttributes().get("distinguishedName").toString();
			retorno = retorno + (atributos.split(":")[1]).trim();
		}
		if ("".equals(retorno)) {
			retorno = "Usuario não existe ou senha não confere !";
		}

		return retorno;
	}

	/**
	 * Autenticar.
	 *
	 * @param user the user
	 * @param pwd the pwd
	 * @return the string
	 */
	public static String autenticar(String user, String pwd) {
		String retorno = "";
		String atributos = "";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		try {
			properties.put(Context.SECURITY_PRINCIPAL, consultarDN(user));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// properties.put(Context.SECURITY_PRINCIPAL,
		// "CN="+user+",OU=Users,OU=Sistemas,OU=ASSINF,DC=sefnet,DC=rj");
		properties.put(Context.SECURITY_CREDENTIALS, pwd);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			retorno = "Usuario ou senha invalido !!!";
			// throw(e);
		}

		if ("".equals(retorno)) {
			try {
				srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (srLdapUsers.hasMoreElements()) {
				SearchResult srLdapUser;
				srLdapUser = (SearchResult) srLdapUsers.nextElement();

				atributos = (String) srLdapUser.getAttributes().get("displayName").toString();
				retorno = retorno + (atributos.split(":")[1]).trim();
			}
		}

		return retorno;

	}

	/**
	 * Find account bybs CPFLU.
	 *
	 * @param ctx the ctx
	 * @param ldapSearchBase the ldap search base
	 * @param accountName the account name
	 * @return the naming enumeration
	 * @throws NamingException the naming exception
	 */
	public static NamingEnumeration<SearchResult> findAccountBybsCPFLU(DirContext ctx, String ldapSearchBase,
			String accountName) throws NamingException {
		String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

		return results;
	}
	
	public static void printMods(String user) throws NamingException {
		String retorno = "0";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		
		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}
		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser = (SearchResult) srLdapUsers.nextElement();

			String userldap = (String) srLdapUser.getAttributes().get("userAccountControl").toString();
	        //System.out.println(userldap);
	        log.info(userldap);
		}
        
	}
	
	
	public static String obterStatusUsuario(String user) throws NamingException {
		String retorno = "0";

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);

		
		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (NamingException e) {
			throw (e);
		}
		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);

		if (srLdapUsers.hasMoreElements()) {
			SearchResult srLdapUser = (SearchResult) srLdapUsers.nextElement();

			String userldap = (String) srLdapUser.getAttributes().get("userAccountControl").toString();
			return userldap;
		}
		
		return null;
        
	}
	
//	public static LDAPUsuario obterUsuario(String user) throws NamingException {
//		String retorno = "0";
//
//		Properties properties = new Properties();
//
//		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
//		properties.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
//		properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
//		properties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
//
//		
//		// initializing active directory LDAP connection
//		try {
//			dirContext = new InitialDirContext(properties);
//		} catch (NamingException e) {
//			throw (e);
//		}
//		srLdapUsers = findAccountBybsCPFLU(dirContext, ldapSearchBase, user);
//
//		if (srLdapUsers.hasMoreElements()) {
//			SearchResult srLdapUser = (SearchResult) srLdapUsers.nextElement();
//
//			String userldap = (String) srLdapUser.getAttributes().get("userAccountControl").toString();
//			return userldap;
//		}
//		
//		return null;
//        
//	}
		
		
}

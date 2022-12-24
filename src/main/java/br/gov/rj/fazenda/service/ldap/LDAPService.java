package br.gov.rj.fazenda.service.ldap;

import br.gov.rj.fazenda.bloqueio.exception.BloqueioException;
import br.gov.rj.fazenda.service.ldap.LDAPResultado.ResultadoLDAP;

public class LDAPService {

	public static final String ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA = "512";
	public static final String ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA = "514";
	public static final String ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA = "66050";
	
	LDAPChange ldapChange;

	public LDAPService() {
		ldapChange = new LDAPChange();
	}

	public static LDAPService newInstance() {

		LDAPService ldapService = new LDAPService();
		return ldapService;
	}
	
	public LDAPUsuario consultar(String login) {
		LDAPUsuario ldapUsuario = null;
		try {
			
			ldapUsuario = LDAPChange.consultar(login);
			
		} catch (Exception e) {
			throw new BloqueioException("Não foi possível consultar o serviço do Active Directory", e);
		}
		return ldapUsuario;
	}
	
	public LDAPResultado bloquearUsuario(String login) {
		
		LDAPResultado ldapResultado = desabilitarUsuario(login);
			
		return ldapResultado;
	}

	private void classificarStatus(LDAPResultado ldapResultado) {
		
		if (ldapResultado.getStatusAnterior() != null && ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA)) {
			ldapResultado.setClassificacaoStatusAnterior(ldapResultado.getStatusAnterior().concat(" (Conta desabilitada)"));
		}

		if (ldapResultado.getStatusAnterior() != null && ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA)) {
			ldapResultado.setClassificacaoStatusAnterior(ldapResultado.getStatusAnterior().concat(" (Conta desabilitada)"));
		}
		
		if (ldapResultado.getStatusAnterior() != null && ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA)) {
			ldapResultado.setClassificacaoStatusAnterior(ldapResultado.getStatusAnterior().concat(" (Conta habilitada)"));
		}
		
		if (ldapResultado.getStatusAtual() != null && ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA)) {
			ldapResultado.setClassificacaoStatusAtual(ldapResultado.getStatusAtual().concat(" (Conta desabilitada)"));
		}
		
		if (ldapResultado.getStatusAtual() != null && ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA)) {
			ldapResultado.setClassificacaoStatusAtual(ldapResultado.getStatusAtual().concat(" (Conta desabilitada)"));
		}
		
		if (ldapResultado.getStatusAtual() != null && ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA)) {
			ldapResultado.setClassificacaoStatusAtual(ldapResultado.getStatusAtual().concat(" (Conta habilitada)"));
		}
		
	}
	
	private LDAPResultado desabilitarUsuario(String login) {
		
		LDAPResultado ldapResultado = new LDAPResultado();
		ldapResultado.setLogin(login);
		
		try {
			
			//TODO
			//LDAPChange.desbHabUsuario(login, LDAPChange.getACCOUNT_ENABLE());
			
			
			String statusAnterior = LDAPChange.obterStatusUsuario(login);
			
			if (statusAnterior != null) {
			
				ldapResultado.setStatusAnterior(statusAnterior);
				
				ldapResultado.setClassificacaoStatusAnterior(statusAnterior);
				
				if (ldapResultado.getClassificacaoStatusAnterior() != null && (ldapResultado.getClassificacaoStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA) || ldapResultado.getClassificacaoStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA))) {
					ldapResultado.setResultado(ResultadoLDAP.SEM_ACAO);
					ldapResultado.setStatusAtual(LDAPChange.obterStatusUsuario(login));
					classificarStatus(ldapResultado);
					ldapResultado.setDetalheResultado("A conta do usuário já estava bloqueada.");
					return ldapResultado;
					
				}
				
				LDAPChange.desbHabUsuario(login, LDAPChange.getACCOUNT_DISABLE());
				
				ldapResultado.setStatusAtual(LDAPChange.obterStatusUsuario(login));
				
				classificarStatus(ldapResultado);
				
				//Estado inicial
				ldapResultado.setResultado(ResultadoLDAP.FALHA);
				
				if (ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA) || ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA)) {
					if (ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA) || ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA_SENHA_NAO_EXPIRA)) {
						ldapResultado.setResultado(ResultadoLDAP.SEM_ACAO);
					} else if (ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA) || ldapResultado.getStatusAnterior().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA)) {
						ldapResultado.setResultado(ResultadoLDAP.SUCESSO);
					}
				}
				
				if (ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_HABILITADA)) {
					ldapResultado.setResultado(ResultadoLDAP.FALHA);
				}

				

				
//				if (ldapResultado.getStatusAtual().endsWith(ACTIVE_DIRECTORY_VALOR_CONTA_SUSPENSA) || ) {
//					ldapResultado.setResultado(ResultadoLDAP.SUCESSO);
//				} else {
//					ldapResultado.setResultado(ResultadoLDAP.FALHA);
//				}
				
			} else {
				
				ldapResultado.setResultado(ResultadoLDAP.FALHA);
				ldapResultado.setDetalheResultado("Usuário não encontado no Active Directory (userAccountControl é nulo).");
				
			}
			
		} catch (Exception e) {
			
			ldapResultado.setResultado(ResultadoLDAP.FALHA);
			ldapResultado.setException(e);
			ldapResultado.setDetalheResultado(e.getMessage());
			
		}
		
		return ldapResultado;
	}

}

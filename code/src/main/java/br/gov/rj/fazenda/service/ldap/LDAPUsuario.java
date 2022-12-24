package br.gov.rj.fazenda.service.ldap;

public class LDAPUsuario {

	private String login;
	private String nome;
	private StatusUsuarioAD statusUsuarioAD;
	
	public enum StatusUsuarioAD {
		CONTA_BLOQUEADA(514L),
		CONTA_ATIVA(512L),
		CONTA_STATUS_DESCONHECIDO(0L),
		CONTA_BLOQUEADA_SENHA_NAO_EXPIRA(66050L),
		CONTA_STATUS_NAO_ENCONTRADO(-1L);
		
		Long valor;
		
		StatusUsuarioAD(Long valor) {
			this.valor = valor;
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatusUsuarioAD getStatusUsuarioAD() {
		return statusUsuarioAD;
	}

	public void setStatusUsuarioAD(StatusUsuarioAD statusUsuarioAD) {
		this.statusUsuarioAD = statusUsuarioAD;
	}
	
	
	
	
}

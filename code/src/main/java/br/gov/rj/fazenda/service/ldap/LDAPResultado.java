package br.gov.rj.fazenda.service.ldap;

import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado.ResultadoBD;

public class LDAPResultado {

	public enum ResultadoLDAP {
		SUCESSO,
		FALHA,
		SEM_ACAO
	}
	
	public ResultadoLDAP resultado;
	public Exception exception;
	public String detalheResultado;
	public String statusAnterior;
	public String statusAtual;
	public String login;
	
	public String classificacaoStatusAnterior;
	public String classificacaoStatusAtual;
	
	public ResultadoLDAP getResultado() {
		return resultado;
	}

	public String getResultadoGeral() {
		return (this.resultado == ResultadoLDAP.FALHA ? " "  + ResultadoLDAP.FALHA.name() + " " : this.resultado.name());
	}
	
	public void setResultado(ResultadoLDAP resultado) {
		this.resultado = resultado;
	}
	public String getDetalheResultado() {
		return detalheResultado;
	}
	public void setDetalheResultado(String detalheResultado) {
		this.detalheResultado = detalheResultado;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public String getStatusAnterior() {
		return statusAnterior;
	}
	public void setStatusAnterior(String statusAnterior) {
		this.statusAnterior = statusAnterior;
	}
	public String getStatusAtual() {
		return statusAtual;
	}
	public void setStatusAtual(String statusAtual) {
		this.statusAtual = statusAtual;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getClassificacaoStatusAnterior() {
		return classificacaoStatusAnterior;
	}
	public void setClassificacaoStatusAnterior(String classificacaoStatusAnterior) {
		this.classificacaoStatusAnterior = classificacaoStatusAnterior;
	}
	public String getClassificacaoStatusAtual() {
		return classificacaoStatusAtual;
	}
	public void setClassificacaoStatusAtual(String classificacaoStatusAtual) {
		this.classificacaoStatusAtual = classificacaoStatusAtual;
	}
	
}

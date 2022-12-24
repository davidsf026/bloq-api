package br.gov.rj.fazenda.service.bdexecute;

public class DBExecuteResultado {

	public enum ResultadoBD {
		SUCESSO,
		FALHA,
		SEM_ACAO;
	}
	
	public ResultadoBD resultadoBD;
	public String indicardorResultado;
	public String detalheResultado;
	public String nomeBancoRetornado;
	public String nomeBancoCandidato;
	public String observacao;
	public Exception exception;
	
	
	public String getIndicardorResultado() {
		return indicardorResultado;
	}
	public void setIndicardorResultado(String indicardorResultado) {
		this.indicardorResultado = indicardorResultado;
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
	public ResultadoBD getResultadoBD() {
		return resultadoBD;
	}
	public void setResultadoBD(ResultadoBD resultadoBD) {
		this.resultadoBD = resultadoBD;
	}
	public String getNomeBancoRetornado() {
		return nomeBancoRetornado;
	}
	public void setNomeBancoRetornado(String nomeBancoRetornado) {
		this.nomeBancoRetornado = nomeBancoRetornado;
	}
	
	public String getNomeBancoCandidato() {
		return nomeBancoCandidato;
	}
	public void setNomeBancoCandidato(String nomeBancoCandidato) {
		this.nomeBancoCandidato = nomeBancoCandidato;
	}
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getResultadoGeral() {
		return (this.resultadoBD == ResultadoBD.FALHA ? " " + ResultadoBD.FALHA.name() + "&nbsp;&nbsp;" : (this.resultadoBD == ResultadoBD.SUCESSO ? ResultadoBD.SUCESSO.name() + "&nbsp;" : this.resultadoBD.name()));
	}
	
	
	@Override
	public String toString() {
		return ("[" + getResultadoBD().name() + "][" + getDetalheResultado() + "][" + (nomeBancoRetornado != null ? nomeBancoRetornado : " {sem retorno}" )+ "] [" + getDetalheResultado() + "] [Nome BD retornado: " + getNomeBancoRetornado() + "]");
	
	}
	
	
	
}

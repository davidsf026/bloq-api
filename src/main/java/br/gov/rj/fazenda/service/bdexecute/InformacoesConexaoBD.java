package br.gov.rj.fazenda.service.bdexecute;

public class InformacoesConexaoBD {

	private String stringConexao;
	private String usuario;
	private String senha;
	private String nomeBDcandidato;
	private String observacao;
	
	public InformacoesConexaoBD(String stringConexao, String usuario, String senha, String nomeBDcandidato, String observacao) {
		
		this.stringConexao = stringConexao;
		this.usuario = usuario;
		this.senha = senha;
		this.nomeBDcandidato = nomeBDcandidato;
		this.observacao = observacao;
	
	}

	public String getStringConexao() {
		return stringConexao;
	}

	public void setStringConexao(String stringConexao) {
		this.stringConexao = stringConexao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeBDcandidato() {
		return nomeBDcandidato;
	}

	public void setNomeBDcandidato(String nomeBDcandidato) {
		this.nomeBDcandidato = nomeBDcandidato;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}

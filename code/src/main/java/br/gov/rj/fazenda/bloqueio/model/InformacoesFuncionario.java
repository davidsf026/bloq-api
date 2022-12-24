package br.gov.rj.fazenda.bloqueio.model;

public class InformacoesFuncionario {

	private String login;
	private String matricula;
	private String nome;
	private String cpf;
	private String nomeBancoRetornado;
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNomeBancoRetornado() {
		return nomeBancoRetornado;
	}
	public void setNomeBancoRetornado(String nomeBancoRetornado) {
		this.nomeBancoRetornado = nomeBancoRetornado;
	}
	
}

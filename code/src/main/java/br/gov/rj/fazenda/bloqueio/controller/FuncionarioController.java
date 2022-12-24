package br.gov.rj.fazenda.bloqueio.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.rj.fazenda.bloqueio.business.BloqueioBusiness;
import br.gov.rj.fazenda.bloqueio.business.FuncionarioBusiness;
import br.gov.rj.fazenda.bloqueio.model.Funcionario;
import br.gov.rj.fazenda.bloqueio.model.InformacoesFuncionario;
import br.gov.rj.fazenda.bloqueio.model.ResultadoBloqueio;
import br.gov.rj.fazenda.service.ldap.LDAPUsuario;
import br.gov.rj.fazenda.service.ldap.LDAPUsuario.StatusUsuarioAD;
import ssa.control.SSA;

@ManagedBean(name = "funcionarioController")
@ViewScoped
public class FuncionarioController {

    private static final long serialVersionUID = -6583280050918589108L;

    private String login;
    private Collection<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
    private Funcionario funcionario;
    
    public String nomeUsuarioSSA = SSA.getSessao().getNomeUsuario();  
    
    private String mensagemDialog;
    
    private Boolean flagUltimoResultadoGeralSucesso;
    
    FuncionarioBusiness business = new FuncionarioBusiness();
    BloqueioBusiness bloqueioBusiness = new BloqueioBusiness();

    @PostConstruct
	public void init() {
    	funcionario = null;
    	nomeUsuarioSSA = SSA.getSessao().getNomeUsuario();  
    }
    
    public void buscarFuncionarioAD() {
    	
    	listaFuncionarios.clear();
    	
    	//listaFuncionarios.add(business.obterFuncionario(login));    	
    	//funcionario = business.obterFuncionario(login);

    	LDAPUsuario ldapUsuario = null; 
    	try {
    		ldapUsuario = business.obterFuncionarioAD(login);
    	} catch (Exception e) {
    		addMessage("Erro ao se contectar com o Active Directory. Tente mais tarde ou entre em contato com o administrador do sistema.");
    		return;
    	}
    	
    	Funcionario funcionario = null;
    	
    	//if (StatusUsuarioAD.CONTA_ATIVA == ldapUsuario.getStatusUsuarioAD() || StatusUsuarioAD.CONTA_STATUS_DESCONHECIDO == ldapUsuario.getStatusUsuarioAD()) {
    	if (!(StatusUsuarioAD.CONTA_STATUS_NAO_ENCONTRADO == ldapUsuario.getStatusUsuarioAD())) {
    		funcionario = new Funcionario();
    		funcionario.setLogin(ldapUsuario.getLogin());
    		funcionario.setNome(ldapUsuario.getNome());
    		
    		this.funcionario =  funcionario;
    	}
    	
    	//} else if (StatusUsuarioAD.CONTA_BLOQUEADA == ldapUsuario.getStatusUsuarioAD() || StatusUsuarioAD.CONTA_BLOQUEADA_SENHA_NAO_EXPIRA == ldapUsuario.getStatusUsuarioAD()) {
    	//	addMessage("O usuário '" + funcionario.getNome() + "' se encontra bloqueado.");
    		
    	//} else 
		if (StatusUsuarioAD.CONTA_STATUS_NAO_ENCONTRADO == ldapUsuario.getStatusUsuarioAD()) {
			addMessage("O usuário de login '" + login + "' não foi encontrado.");
		}
		
    	return;

    }
    
    public void addMessage(String message) {
    	mensagemDialog = message;
    }

	
    public void realizarBloqueio() {
    	
    	try {
    	
	    	InformacoesFuncionario informacoesFuncionario = new InformacoesFuncionario();
	    	informacoesFuncionario.setLogin(login);
	    	
	    	ResultadoBloqueio resultadoBloqueio = bloqueioBusiness.realizarBloqueioFuncionario(informacoesFuncionario);
	    	
	    	setFlagUltimoResultadoGeralSucesso(resultadoBloqueio.isResultadoGeralSucesso());

    	} catch (Exception e) {
    		addMessage("Ocorreu um erro inesperado no sistema. Contacte o administrador do sistema");
    	}
    	
    	limpar();
    	
    }
    
    public void limparFlagUltimoResultadoGeralSucesso() {
    	flagUltimoResultadoGeralSucesso = null;
    }
    
    public void limpar() {
    	funcionario = null;
    	login = null;
    	listaFuncionarios.clear();
    }

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Collection<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(Collection<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getFlagUltimoResultadoGeralSucesso() {
		return flagUltimoResultadoGeralSucesso;
	}

	public void setFlagUltimoResultadoGeralSucesso(Boolean flagUltimoResultadoGeralSucesso) {
		this.flagUltimoResultadoGeralSucesso = flagUltimoResultadoGeralSucesso;
	}

	public String getMensagemDialog() {
		return mensagemDialog;
	}

	public void setMensagemDialog(String mensagemDialog) {
		this.mensagemDialog = mensagemDialog;
	}
	
	public void limparMensagemDialog() {
		this.mensagemDialog = null;
	}

	public String getNomeUsuarioSSA() {
		return nomeUsuarioSSA;
	}

	public void setNomeUsuarioSSA(String nomeUsuarioSSA) {
		this.nomeUsuarioSSA = nomeUsuarioSSA;
	}
	

}

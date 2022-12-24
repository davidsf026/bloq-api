package br.gov.rj.fazenda.bloqueio.model;

import java.util.ArrayList;
import java.util.Collection;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado.ResultadoBD;
import br.gov.rj.fazenda.service.ldap.LDAPResultado;
import br.gov.rj.fazenda.service.ldap.LDAPResultado.ResultadoLDAP;

public class ResultadoBloqueio {

	private Collection<DBExecuteResultado> resultadosBD = new ArrayList<>();
	private Collection<DBExecuteResultado> resultadosBuscaCPF = new ArrayList<>();
	private Collection<DBExecuteResultado> resultadosSiafeRio = new ArrayList<>();
	private Collection<DBExecuteResultado> resultadosSispat = new ArrayList<>();
	private Collection<DBExecuteResultado> resultadosSSA = new ArrayList<>();
	private LDAPResultado resultadoLDAP;
	private InformacoesFuncionario informacoesFuncionario;

	
	public boolean isResultadoGeralSucesso() {

		boolean resultatoGeralSucesso = true;
		
		if (ApplicationExternalProperties.BLOQUEIO_LDAP_HABILITADO) {
			if (resultadoLDAP.getResultado() == ResultadoLDAP.FALHA) {
				resultatoGeralSucesso = false;
			}
		}
			
		if (ApplicationExternalProperties.BLOQUEIO_BD_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadosBD) {
				if (resultadoBD.getResultadoBD() == ResultadoBD.FALHA) {
					resultatoGeralSucesso = false; 
				}
			}
		}
		

		for (DBExecuteResultado resultadoBD : resultadosBuscaCPF) {
			if (resultadoBD.getResultadoBD() == ResultadoBD.FALHA) {
				resultatoGeralSucesso = false; 
			}
		}
		
		if (ApplicationExternalProperties.BLOQUEIO_SIAFERIO_HABILITADO) {
			for (DBExecuteResultado resultadoSiafeRio : resultadosSiafeRio) {
				if (resultadoSiafeRio.getResultadoBD() == ResultadoBD.FALHA) {
					resultatoGeralSucesso = false; 
				}
			}
		}
		
		
		if (ApplicationExternalProperties.BLOQUEIO_SISPAT_HABILITADO) {
			for (DBExecuteResultado resultadoSispat : resultadosSispat) {
				if (resultadoSispat.getResultadoBD() == ResultadoBD.FALHA) {
					resultatoGeralSucesso = false; 
				}
			}
		}
		
		if (ApplicationExternalProperties.BLOQUEIO_SSA_HABILITADO) {
			for (DBExecuteResultado resultadoSSA : resultadosSSA) {
				if (resultadoSSA.getResultadoBD() == ResultadoBD.FALHA) {
					resultatoGeralSucesso = false;
				}
			}
		}
		
		return resultatoGeralSucesso;
	
	}
	
	public Collection<DBExecuteResultado> getResultadosBD() {
		return resultadosBD;
	}

	public void setResultadosBD(Collection<DBExecuteResultado> resultadosBD) {
		this.resultadosBD = resultadosBD;
	}

	public LDAPResultado getResultadoLDAP() {
		return resultadoLDAP;
	}

	public void setResultadoLDAP(LDAPResultado resultadoLDAP) {
		this.resultadoLDAP = resultadoLDAP;
	}

	public InformacoesFuncionario getInformacoesFuncionario() {
		return informacoesFuncionario;
	}

	public void setInformacoesFuncionario(InformacoesFuncionario informacoesFuncionario) {
		this.informacoesFuncionario = informacoesFuncionario;
	}
	
	public Collection<DBExecuteResultado> getResultadosBuscaCPF() {
		return resultadosBuscaCPF;
	}

	public void setResultadosBuscaCPF(Collection<DBExecuteResultado> resultadosBuscaCPF) {
		this.resultadosBuscaCPF = resultadosBuscaCPF;
	}

	public Collection<DBExecuteResultado> getResultadosSiafeRio() {
		return resultadosSiafeRio;
	}

	public void setResultadosSiafeRio(Collection<DBExecuteResultado> resultadosSiafeRio) {
		this.resultadosSiafeRio = resultadosSiafeRio;
	}

	public Collection<DBExecuteResultado> getResultadosSispat() {
		return resultadosSispat;
	}

	public void setResultadosSispat(Collection<DBExecuteResultado> resultadosSispat) {
		this.resultadosSispat = resultadosSispat;
	}

	public Collection<DBExecuteResultado> getResultadosSSA() {
		return resultadosSSA;
	}

	public void setResultadosSSA(Collection<DBExecuteResultado> resultadosSSA) {
		this.resultadosSSA = resultadosSSA;
	}
	
}

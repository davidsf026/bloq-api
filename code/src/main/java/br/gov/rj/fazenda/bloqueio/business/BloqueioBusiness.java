package br.gov.rj.fazenda.bloqueio.business;

import org.apache.log4j.Logger;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.bloqueio.dao.FuncionarioDAO;
import br.gov.rj.fazenda.bloqueio.model.Funcionario;
import br.gov.rj.fazenda.bloqueio.model.InformacoesFuncionario;
import br.gov.rj.fazenda.bloqueio.model.ResultadoBloqueio;
import br.gov.rj.fazenda.bloqueio.util.EmailUtil;
import br.gov.rj.fazenda.bloqueio.util.RelatorioUtil;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado.ResultadoBD;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteService;
import br.gov.rj.fazenda.service.bdexecute.InformacoesConexaoBD;
import br.gov.rj.fazenda.service.ldap.LDAPResultado;
import br.gov.rj.fazenda.service.ldap.LDAPService;

public class BloqueioBusiness {

	ResultadoBloqueio resultadoBloqueio;
	InformacoesFuncionario informacoesFuncionario;
	
	public static Logger log = Logger.getLogger(BloqueioBusiness.class);
	
	public ResultadoBloqueio realizarBloqueioFuncionario(InformacoesFuncionario informacoesBloqueio) {
		
		atribuir(informacoesBloqueio);
		
		this.informacoesFuncionario = informacoesBloqueio;
		
		resultadoBloqueio = new ResultadoBloqueio();
		resultadoBloqueio.setInformacoesFuncionario(this.informacoesFuncionario);
		
		try {
		
			bloquearUsuarioBD();

			InformacoesFuncionario informacoesFuncionarioRecuperadaBanco = null;
			
			DBExecuteResultado resultadoObterCPF = new DBExecuteResultado();
			
			try {
				
				informacoesFuncionarioRecuperadaBanco = recuperarFuncionarioComCPF(informacoesBloqueio);
								
				if (informacoesFuncionarioRecuperadaBanco == null) {
					resultadoObterCPF.setDetalheResultado("O login '" + informacoesFuncionario.getLogin() + "' não está associado a nenhum CPF.");
					resultadoObterCPF.setResultadoBD(ResultadoBD.SEM_ACAO);
				} else {
					resultadoObterCPF.setDetalheResultado("O login '" + informacoesFuncionario.getLogin() + "' foi encontrado associado ao CPF " + (informacoesFuncionarioRecuperadaBanco.getCpf()!= null ? informacoesFuncionarioRecuperadaBanco.getCpf() : " - ") + " na base");
					resultadoObterCPF.setResultadoBD(ResultadoBD.SUCESSO);
				}
				
				
			
			} catch (Exception e) {
				
				log.error("Houve um erro na recuperação do CPF usuário.", e);
				resultadoObterCPF.setException(e);
				resultadoObterCPF.setResultadoBD(ResultadoBD.FALHA);
				resultadoObterCPF.setDetalheResultado((e.getMessage() != null ? e.getMessage() : "") + " - " + (e.getCause() != null ? e.getCause().toString() : ""));
				if (e.getCause() != null && (e.getCause() instanceof java.sql.SQLException || e.getCause() instanceof java.sql.SQLException)) resultadoObterCPF.setDetalheResultado((e.getCause()).getMessage());
				
				
			}
			try {
				String nomeBanco = new FuncionarioDAO(Funcionario.class).obterNomeBanco();
				resultadoObterCPF.setNomeBancoCandidato(nomeBanco);
				resultadoObterCPF.setNomeBancoRetornado(nomeBanco);
			} catch (Exception e) {
				log.error("Houve um erro na recuperação do nome do banco", e);
			}
			
			resultadoBloqueio.getResultadosBuscaCPF().add(resultadoObterCPF);
			
			//Atribui o formulário inicial caso não ache o funcionario na base
			if (informacoesFuncionarioRecuperadaBanco != null) {
				this.informacoesFuncionario = informacoesFuncionarioRecuperadaBanco;
			}
			
			if (isExisteCPFFuncionario(this.informacoesFuncionario)) {
				
				bloquearUsuarioSispat();
				bloquearUsuarioSiafeRio();
				bloquearUsuarioSSA();
				
			} else {
				
				preencherResultadosDeNaoRealizacao(resultadoObterCPF.detalheResultado);

			}
			
			bloquearUsuarioAD();
		
		} catch (Exception e) {
			log.error(e);
			throw e;
		} finally {
			resultadoBloqueio.setInformacoesFuncionario(this.informacoesFuncionario);
		}
		
		extrairResultado();
		enviarEmail();
		
		return resultadoBloqueio;
	}
	
	private boolean isExisteCPFFuncionario(InformacoesFuncionario informacaoRecuperadaFuncionario) {
		return informacaoRecuperadaFuncionario != null && informacaoRecuperadaFuncionario.getCpf() != null;
	}

	private InformacoesFuncionario recuperarFuncionarioComCPF(InformacoesFuncionario informacoesBloqueio) {
		InformacoesFuncionario informacaoRecuperadaFuncionario = null;

		informacaoRecuperadaFuncionario = new FuncionarioDAO(Funcionario.class).recuperarFuncionarioInterno(informacoesBloqueio.getLogin());
		
		if (informacaoRecuperadaFuncionario == null) {
			informacaoRecuperadaFuncionario = new FuncionarioDAO(Funcionario.class).recuperarFuncionarioExterno(informacoesBloqueio.getLogin());	
		}
		
		return informacaoRecuperadaFuncionario;
	}

	private void atribuir(InformacoesFuncionario informacoesBloqueio) {
		if (ApplicationExternalProperties.BLOQUEIO_USUARIO_TESTE_ATIVO) {
			informacoesBloqueio.setLogin(ApplicationExternalProperties.BLOQUEIO_USUARIO_TESTE_LOGIN);
			informacoesBloqueio.setNome(ApplicationExternalProperties.BLOQUEIO_USUARIO_TESTE_NOME);
			informacoesBloqueio.setCpf(ApplicationExternalProperties.BLOQUEIO_USUARIO_TESTE_CPF);
			informacoesBloqueio.setMatricula(ApplicationExternalProperties.BLOQUEIO_USUARIO_TESTE_MATRICULA);	
		}
	}

	private void bloquearUsuarioAD() {
		

		if (ApplicationExternalProperties.BLOQUEIO_LDAP_HABILITADO) {

			log.info(" ##### INICIANDO BLOQUEIO DO AD/LDAP");
			

			LDAPResultado ldapResultado = LDAPService.newInstance().bloquearUsuario(informacoesFuncionario.getLogin());
			
			resultadoBloqueio.setResultadoLDAP(ldapResultado);
			
			log.info("[" + ldapResultado.getResultado() + "]" +
					"[login: " + ldapResultado.getLogin() + "]" +
					"[Detalhes: " + ldapResultado.getDetalheResultado() + "]" +
					"[status anterior: " + ldapResultado.getClassificacaoStatusAnterior() + "]" +
					"[status atual: " + ldapResultado.getClassificacaoStatusAtual() + "]" );

			log.info(" ##### FINALIZANDO BLOQUEIO DO AD/LDAP");

		} else {
			
			log.info(" ##### SERVIÇO DE BLOQUEIO AD/LDAP DESABILITADO. NADA SERÁ FEITO.");
			
		}
	}

	
	private void bloquearUsuarioBD() {		
		
		if (ApplicationExternalProperties.BLOQUEIO_BD_HABILITADO) {
			
			log.info(" ##### BANCO DE DADOS - INICIANDO BLOQUEIO DO USUÁRIO DO BANCO DE DADOS");
			
			for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_BANCOS) {

				String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");
				
				InformacoesConexaoBD informacoesConexaoBD = new InformacoesConexaoBD(
						valorInfoBanco[3],
						valorInfoBanco[1],
						valorInfoBanco[2],
						valorInfoBanco[0],
						"Login candidato: " + informacoesFuncionario.getLogin()
						);

				DBExecuteResultado resultado = new DBExecuteService().bloquearUsuario(informacoesFuncionario.getLogin(), informacoesConexaoBD);

				resultadoBloqueio.getResultadosBD().add(resultado);
				
				log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
				
			}			

			log.info(" ##### BANCO DE DADOS - FINALIZANDO BLOQUEIO DO USUÁRIO DO BANCO DE DADOS");

		} else {
			
			log.info(" ##### BANCO DE DADOS - SERVIÇO DESABILITADO. NADA SERÁ FEITO.");
			
		}
	}
	
	private void bloquearUsuarioSispat() {		
		
		if (ApplicationExternalProperties.BLOQUEIO_SISPAT_HABILITADO) {
			
			log.info(" ##### SISPAT - INICIANDO BLOQUEIO DO USUÁRIO SISPAT (VIA BANCO DE DADOS)");
			
			for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SISPAT) {

				String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");
				
				InformacoesConexaoBD informacoesConexaoBD = new InformacoesConexaoBD(
						valorInfoBanco[3],
						valorInfoBanco[1],
						valorInfoBanco[2],
						valorInfoBanco[0],
						"Login candidato: " + informacoesFuncionario.getLogin()
						);

				DBExecuteResultado resultado = new DBExecuteService().bloquearUsuarioSISPAT(informacoesFuncionario.getCpf(), informacoesConexaoBD);

				resultadoBloqueio.getResultadosSispat().add(resultado);
				
				log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
				
			}			

			log.info(" ##### SISPAT - FINALIZANDO BLOQUEIO DO USUÁRIO SISPAT (VIA BANCO DE DADOS)");

		} else {
			
			log.info(" ##### SISPAT - SERVIÇO DESABILITADO. NADA SERÁ FEITO.");
			
		}
	}
	

	private void bloquearUsuarioSiafeRio() {		
		
		if (ApplicationExternalProperties.BLOQUEIO_SIAFERIO_HABILITADO) {
			
			log.info(" ##### SIAFE RIO - INICIANDO BLOQUEIO DO USUÁRIO DO SIAFE RIO (VIA BANCO DE DADOS)");
			
			for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SIAFE_RIO) {

				String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");
				
				InformacoesConexaoBD informacoesConexaoBD = new InformacoesConexaoBD(
						valorInfoBanco[3],
						valorInfoBanco[1],
						valorInfoBanco[2],
						valorInfoBanco[0],
						"Login candidato: " + informacoesFuncionario.getLogin()
						);

				DBExecuteResultado resultado = new DBExecuteService().bloquearUsuarioSiafeRio(informacoesFuncionario.getCpf(), informacoesConexaoBD);

				resultadoBloqueio.getResultadosSiafeRio().add(resultado);
				
				log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
				
			}			

			log.info(" ##### SIAFE RIO - FINALIZANDO BLOQUEIO DO USUÁRIO DO SIAFE RIO (VIA BANCO DE DADOS)");

		} else {
			
			log.info(" ##### SIAFE RIO - SERVIÇO DESABILITADO. NADA SERÁ FEITO.");
			
		}
	}
	
	private void bloquearUsuarioSSA() {		
		
		if (ApplicationExternalProperties.BLOQUEIO_SIAFERIO_HABILITADO) {
			
			log.info(" ##### SSA - INICIANDO BLOQUEIO DO USUÁRIO DO SSA (VIA BANCO DE DADOS)");
			
			for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SSA) {

				String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");
				
				InformacoesConexaoBD informacoesConexaoBD = new InformacoesConexaoBD(
						valorInfoBanco[3],
						valorInfoBanco[1],
						valorInfoBanco[2],
						valorInfoBanco[0],
						"Login candidato: " + informacoesFuncionario.getLogin()
						);

				DBExecuteResultado resultado = new DBExecuteService().bloquearUsuarioSSA(informacoesFuncionario.getCpf(), informacoesConexaoBD);

				resultadoBloqueio.getResultadosSSA().add(resultado);
				
				log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
				
			}			

			log.info(" ##### SSA - FINALIZANDO BLOQUEIO DO USUÁRIO DO SIAFE RIO (VIA BANCO DE DADOS)");

		} else {
			
			log.info(" ##### SSA - SERVIÇO DESABILITADO. NADA SERÁ FEITO.");
			
		}
	}

	
	private void preencherResultadosDeNaoRealizacao(String motivo) {
		preencherNaoRealizacaoSiafeRio(motivo);
		preencherNaoRealizacaoSispat(motivo);
		preencherNaoRealizacaoSSA(motivo);
	}
	
	private void preencherNaoRealizacaoSiafeRio(String motivo) {
		
		for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SIAFE_RIO) {

			String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");

			DBExecuteResultado resultado = new DBExecuteResultado();
			resultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			resultado.setNomeBancoCandidato(valorInfoBanco[0]);
			resultado.setDetalheResultado("Bloqueio no SIAFE-RIO não realizado. " + motivo);

			resultadoBloqueio.getResultadosSiafeRio().add(resultado);
			
			log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
			
		}
		
	}
	
	private void preencherNaoRealizacaoSispat(String motivo) {
		
		for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SISPAT) {

			String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");

			DBExecuteResultado resultado = new DBExecuteResultado();
			resultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			resultado.setNomeBancoCandidato(valorInfoBanco[0]);
			
			resultado.setDetalheResultado("Bloqueio no SISPAT não realizado. " + motivo);
			
			resultadoBloqueio.getResultadosSispat().add(resultado);
			
			log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
			
		}
		
	}

	private void preencherNaoRealizacaoSSA(String motivo) {
		
		for (String keyInfoBanco : ApplicationExternalProperties.KEYS_PROPERTY_BLOQUEIO_SSA) {

			String valorInfoBanco[] = ApplicationExternalProperties.obterRecurso(keyInfoBanco).split(",");

			DBExecuteResultado resultado = new DBExecuteResultado();
			resultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			resultado.setNomeBancoCandidato(valorInfoBanco[0]);		
			resultado.setDetalheResultado("Bloqueio no SSA não realizado. " + motivo);

			resultadoBloqueio.getResultadosSSA().add(resultado);
			
			log.info("[" + resultado.getResultadoBD().name() + "][banco candidato: " + resultado.nomeBancoCandidato + "] [login: " + informacoesFuncionario.getLogin() + "] [" + resultado.getDetalheResultado() + "] [Nome BD retornado: " + resultado.getNomeBancoRetornado() + "]");
			
		}
		
	}
		
	private void extrairResultado() {
		RelatorioUtil.extrairResultado(resultadoBloqueio);
	}
	
	private void enviarEmail() {
		EmailUtil.enviarEmail(resultadoBloqueio);
	}

}

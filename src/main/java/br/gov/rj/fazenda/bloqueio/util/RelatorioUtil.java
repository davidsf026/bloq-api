package br.gov.rj.fazenda.bloqueio.util;

import org.apache.log4j.Logger;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.bloqueio.model.ResultadoBloqueio;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado;
import br.gov.rj.fazenda.service.ldap.LDAPResultado;

public class RelatorioUtil {

	public static void extrairResultado(ResultadoBloqueio resultadoBloqueio) {
		
		Logger log = Logger.getLogger(RelatorioUtil.class);

		log.info("");
		log.info("##################### RESULTADO DOS BLOQUEIOS DO USUÁRIO #####################");
		log.info("");
		log.info("----------------------------------------------------------------------------------");
		log.info("  Login:     " + (resultadoBloqueio.getInformacoesFuncionario().getLogin() != null ? resultadoBloqueio.getInformacoesFuncionario().getLogin() : ""));
		log.info("  Nome:      " + (resultadoBloqueio.getInformacoesFuncionario().getNome() != null ? resultadoBloqueio.getInformacoesFuncionario().getNome() : ""));
		log.info("  CPF:       " + (resultadoBloqueio.getInformacoesFuncionario().getCpf() != null ? resultadoBloqueio.getInformacoesFuncionario().getCpf() : ""));
		log.info("  Matrícula: " + (resultadoBloqueio.getInformacoesFuncionario().getMatricula() != null ? resultadoBloqueio.getInformacoesFuncionario().getMatricula() : ""));
		log.info("----------------------------------------------------------------------------------");
		log.info("");

		log.info("#### 1. BANCO DE DADOS - Resultado do bloqueio do usuário");
		log.info("");
		
		if (ApplicationExternalProperties.BLOQUEIO_BD_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosBD()) {
				log.info("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + "[" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] [Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]");
			}
		} else {
			log.info("        Esta opção está desabilitada na aplicação. Nada será feito.");
		}
		log.info("");
		
		log.info("#### 2. Obter CPF do usuário das tabelas");
		log.info("");
		
		for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosBuscaCPF()) {
			log.info("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + "[" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "]");
		}
	
		log.info("");
		
		log.info("#### 3. SISPAT - Resultado do bloqueio do usuário na aplicação SISPAT (via banco de dados)");
		log.info("");
		if (ApplicationExternalProperties.BLOQUEIO_SISPAT_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSispat()) {
				log.info("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + "[" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] [Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]");
			}
		} else {
			log.info("        Esta opção está desabilitada na aplicação. Nada será feito.");
		}
		log.info("");
		
		log.info("#### 4. SIAFE RIO - Resultado do bloqueio do usuário na aplicação (via banco de dados)");
		log.info("");
		
		if (ApplicationExternalProperties.BLOQUEIO_SIAFERIO_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSiafeRio()) {
				log.info("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + "[" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] [Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]");
			}
		} else {
			log.info("        Esta opção está desabilitada na aplicação. Nada será feito.");
		}
		
		log.info("");
		
		log.info("#### 5. SSA - Resultado do bloqueio do usuário (via banco de dados)");
		log.info("");
		
		if (ApplicationExternalProperties.BLOQUEIO_SSA_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSSA()) {
				log.info("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + "[" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] [Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]");
			}
		} else {
			log.info("        Esta opção está desabilitada na aplicação. Nada será feito.");
		}
		
		log.info("");
		
		log.info("#### 6. ACTIVE DIRECTORY - Resultado do bloqueio do usuário");
		log.info("");
		
		if (ApplicationExternalProperties.BLOQUEIO_LDAP_HABILITADO) {
			
			LDAPResultado ldapResultado = resultadoBloqueio.getResultadoLDAP();
			
			log.info("        [" + ldapResultado.getResultadoGeral() + "]   [login: " + ldapResultado.getLogin() + "] [" + ldapResultado.getDetalheResultado() + "] [status anterior: " + (ldapResultado.getClassificacaoStatusAnterior() != null ? ldapResultado.getClassificacaoStatusAnterior() : "-") + "]" + "[status atual: " + (ldapResultado.getClassificacaoStatusAtual() != null ? ldapResultado.getClassificacaoStatusAtual() : "-") + "]" );
			
		} else {
			log.info("        Esta opção está desabilitada na aplicação. Nada será feito.");
		}
		log.info("");
		
		log.info("############################### FIM DO RELATÓRIO #################################");
	}
	
}

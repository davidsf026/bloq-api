package br.gov.rj.fazenda.bloqueio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.bloqueio.exception.BloqueioException;
import br.gov.rj.fazenda.bloqueio.model.ResultadoBloqueio;
import br.gov.rj.fazenda.client.suaf.mailclient.Email;
import br.gov.rj.fazenda.client.suaf.mailclient.MailClient;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado;
import br.gov.rj.fazenda.service.ldap.LDAPResultado;
import ssa.control.SSA;

public class EmailUtil {

	public static void enviarEmail(ResultadoBloqueio resultadoBloqueio) {

		StringBuilder sb = new StringBuilder();

		sb.append("</br>");
		sb.append("##################### RESULTADO DOS BLOQUEIOS DO USUÁRIO #####################</br>");
		sb.append("</br>");
		sb.append("----------------------------------------------------------------------------------</br>");
		sb.append("  Login:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + (resultadoBloqueio.getInformacoesFuncionario().getLogin() != null ? resultadoBloqueio.getInformacoesFuncionario().getLogin() : "") + "</br>");
		sb.append("  Nome:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + (resultadoBloqueio.getInformacoesFuncionario().getNome() != null ? resultadoBloqueio.getInformacoesFuncionario().getNome() : "")+ "</br>");
		sb.append("  CPF:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + (resultadoBloqueio.getInformacoesFuncionario().getCpf() != null ? resultadoBloqueio.getInformacoesFuncionario().getCpf() : "")+ "</br>");
		sb.append("  Matrícula:&nbsp;" + (resultadoBloqueio.getInformacoesFuncionario().getMatricula() != null ? resultadoBloqueio.getInformacoesFuncionario().getMatricula() : "")+ "</br>");
		sb.append("----------------------------------------------------------------------------------</br>");
		sb.append("</br>");

		sb.append("#### 1. BANCO DE DADOS - Resultado do bloqueio do usuário</br>");
		sb.append("</br>");
		
		if (ApplicationExternalProperties.BLOQUEIO_BD_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosBD()) {
				sb.append("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + " [" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] " + (resultadoBD.getNomeBancoRetornado() != null ? "[Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]" : "") + "</br>");
			}
		} else {
			sb.append("        Esta opção está desabilitada na aplicação. Nada será feito.</br>");
		}
		sb.append("</br>");
		
		sb.append("#### 2. Obter CPF do usuário das tabelas</br>");
		sb.append("</br>");
		
		for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosBuscaCPF()) {
			sb.append("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + " [" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "]</br>");
		}
	
		sb.append("</br>");
		
		sb.append("#### 3. SISPAT - Resultado do bloqueio do usuário na aplicação SISPAT (via banco de dados)</br>");
		sb.append("</br>");
		if (ApplicationExternalProperties.BLOQUEIO_SISPAT_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSispat()) {
				sb.append("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + " [" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] " + (resultadoBD.getNomeBancoRetornado() != null ? "[Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]" : "") + "</br>");
			}
		} else {
			sb.append("        Esta opção está desabilitada na aplicação. Nada será feito.</br>");
		}
		sb.append("</br>");
		
		sb.append("#### 4. SIAFE RIO - Resultado do bloqueio do usuário na aplicação (via banco de dados)</br>");
		sb.append("</br>");
		
		if (ApplicationExternalProperties.BLOQUEIO_SIAFERIO_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSiafeRio()) {
				sb.append("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + " [" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] " + (resultadoBD.getNomeBancoRetornado() != null ? "[Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]" : "") + "</br>");
			}
		} else {
			sb.append("        Esta opção está desabilitada na aplicação. Nada será feito.</br>");
		}
		
		sb.append("</br>");
		
		sb.append("#### 5. SSA - Resultado do bloqueio do usuário (via banco de dados)</br>");
		sb.append("</br>");
		
		if (ApplicationExternalProperties.BLOQUEIO_SSA_HABILITADO) {
			for (DBExecuteResultado resultadoBD : resultadoBloqueio.getResultadosSSA()) {
				sb.append("        [" + resultadoBD.getResultadoGeral() + "]   [" + resultadoBD.nomeBancoCandidato + "] " + (resultadoBD.observacao != null ? "[" + resultadoBD.observacao + "]" : "") + " [" + resultadoBD.getDetalheResultado().replaceAll("\\n", " - ") + "] " + (resultadoBD.getNomeBancoRetornado() != null ? "[Nome BD retornado: " + resultadoBD.getNomeBancoRetornado() + "]" : "") + "</br>");
			}
		} else {
			sb.append("        Esta opção está desabilitada na aplicação. Nada será feito.</br>");
		}
		
		sb.append("</br>");
		
		sb.append("#### 6. ACTIVE DIRECTORY - Resultado do bloqueio do usuário</br>");
		sb.append("</br>");
		
		if (ApplicationExternalProperties.BLOQUEIO_LDAP_HABILITADO) {
			
			LDAPResultado ldapResultado = resultadoBloqueio.getResultadoLDAP();
			
			sb.append("        [" + ldapResultado.getResultadoGeral() + "]   [login: " + ldapResultado.getLogin() + "] [" + ldapResultado.getDetalheResultado() + "] [status anterior: " + (ldapResultado.getClassificacaoStatusAnterior() != null ? ldapResultado.getClassificacaoStatusAnterior() : "-") + "]" + "[status atual: " + (ldapResultado.getClassificacaoStatusAtual() != null ? ldapResultado.getClassificacaoStatusAtual() : "-") + "]" );
			
		} else {
			sb.append("        Esta opção está desabilitada na aplicação. Nada será feito.</br>");
		}
		sb.append("</br>");
		sb.append("</br>");
		sb.append("############################### FIM DO RELATÓRIO #################################</br>");

		String email = convertInputStreamToString(Thread.currentThread().getContextClassLoader().getResourceAsStream("email_relatorio.html"));

		String textoEmail = email.replaceAll("Nome", sb.toString());

		MailClient mailClient = null;

		try {
			
			String emailUsuarioSSA = SSA.getSessao().getLoginUsuario() + "@fazenda.rj.gov.br";
			
			mailClient = new MailClient(ApplicationExternalProperties.EMAIL_RELATORIO_URI, ApplicationExternalProperties.EMAIL_RELATORIO_HASHSEC, ApplicationExternalProperties.EMAIL_RELATORIO_APLICACAO);
			
			Email emailBloqueio = new Email();
			emailBloqueio.setAssunto("Relatório de bloqueio de funcionário");
			if (ApplicationExternalProperties.EMAIL_RELATORIO_TO == null || (ApplicationExternalProperties.EMAIL_RELATORIO_TO != null && ApplicationExternalProperties.EMAIL_RELATORIO_TO.isEmpty())){
				emailBloqueio.setPara(emailUsuarioSSA);
			} else {
				emailBloqueio.setPara(ApplicationExternalProperties.EMAIL_RELATORIO_TO + "," + emailUsuarioSSA);
			}

			emailBloqueio.setDe(ApplicationExternalProperties.EMAIL_RELATORIO_FROM);
			emailBloqueio.setCorpo(textoEmail);
			emailBloqueio.setTipo("HTML");
			mailClient.enviarEmail(emailBloqueio);
			
		} catch (Exception e) {
			throw new BloqueioException("Não foi possível enviar o e-mail", e);
		}
		
	}


	public static String convertInputStreamToString(InputStream is) {

		StringBuilder textBuilder = new StringBuilder();
		try (Reader reader = new BufferedReader(
				new InputStreamReader(is, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				textBuilder.append((char) c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textBuilder.toString();
	}
	
		
}

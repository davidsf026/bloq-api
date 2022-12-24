package br.gov.rj.fazenda.bloqueio.business;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.gov.rj.fazenda.bloqueio.model.Funcionario;
import br.gov.rj.fazenda.service.ldap.LDAPService;
import br.gov.rj.fazenda.service.ldap.LDAPUsuario;
import br.gov.rj.fazenda.service.ldap.LDAPUsuario.StatusUsuarioAD;

/**
 * The Class Endereco.
 */

public class FuncionarioBusiness {

	public LDAPUsuario obterFuncionarioAD(String login) {
		return LDAPService.newInstance().consultar(login);
	}
	
//	public Funcionario obterFuncionario(String login) {
//		
//		Funcionario funcionario = new Funcionario();
////		funcionario.setNome("Fulano da Soares Santos Silva");
////		funcionario.setLogin("fsssilva");
////		funcionario.setMatricula("37485044");
////		funcionario.setSuperintendencia("SUCIEF");
////		funcionario.setAniversario("01/09");
////		funcionario.setDescricaoCargo("Auditor Fiscal da Receita Estadual");
////		funcionario.setDescricaoFuncao("Auditor Fiscal Subchefe da Receita Estadual Fiscal");
//		
//		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		Session session = sessionFactory.openSession();
//		
//
//		Query sqlQuery = session.createSQLQuery("select  TX_NOME, NU_CPF, TX_LOGINREDE from BLOQ.IE_SRH_VW_FUNCIONARIO WHERE tx_loginrede like :login");
//		sqlQuery.setParameter("login", login.toUpperCase());
//		Object object = sqlQuery.getSingleResult();
//		
//		Object[] oarray = (Object[])object;
//		
//		funcionario.setNome((String)oarray[0]);
//		funcionario.setLogin((String)oarray[2]);
//
//		session.close();
//
//		return funcionario;
//		
//	}

}

package br.gov.rj.fazenda.bloqueio.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.bloqueio.exception.BloqueioPersistenceException;
import br.gov.rj.fazenda.bloqueio.model.InformacoesFuncionario;
import br.gov.rj.fazenda.service.bdexecute.DBPersistenceException;
import br.gov.sefazrj.sati.sefazwork.model.exception.DAOException;

public class FuncionarioDAO extends BloqueioDAO {

    public FuncionarioDAO(Class<?> classeNegocio) {
		super(classeNegocio);
		// TODO Auto-generated constructor stub
	}

	private Logger log = Logger.getLogger(this.getClass());

	public InformacoesFuncionario recuperarFuncionarioInterno(String login) {
		
		Session session = null;
		
		InformacoesFuncionario informacoesFuncionario = new InformacoesFuncionario();
		
		try {
			
			session = obterSession();
			
			Query query = session.createNativeQuery(ApplicationExternalProperties.QUERY_CONSULTA_FUNCIONARIO_INTERNO);
			query.setParameter("login", login.toUpperCase());
			List result = query.getResultList();
			
			
			if (result == null || result.isEmpty()) {
				return null;
			} else {
				Object[] objFuncionario = (Object[])result.get(0);
				informacoesFuncionario.setLogin((String)objFuncionario[0]);
				informacoesFuncionario.setNome((String)objFuncionario[1]);
				informacoesFuncionario.setMatricula((String)objFuncionario[2].toString());
				informacoesFuncionario.setCpf((String)objFuncionario[3].toString());
			}
			
			return informacoesFuncionario;
			
		} catch (Exception e) {
			rollbackTransacao(session);
			throw new BloqueioPersistenceException("Houve um erro ao tentar recuperar o CPF do usuário na tabela de usuário interno no item 2.", e);
		} finally {
			fecharSession(session);
		}

	}
	
	public InformacoesFuncionario recuperarFuncionarioExterno(String login) {
		
		Session session = null;
		
		InformacoesFuncionario informacoesFuncionario = new InformacoesFuncionario();
		
		try {
			
			session = obterSession();
			
			Query query = session.createNativeQuery(ApplicationExternalProperties.QUERY_CONSULTA_FUNCIONARIO_EXTERNO);
			query.setParameter("login", login.toUpperCase());
			
			List result = query.getResultList();
			
			if (result == null || result.isEmpty()) {
				return null;
			} else {
				Object[] objFuncionario = (Object[])result.get(0);
				informacoesFuncionario.setLogin((String)objFuncionario[0]);
				informacoesFuncionario.setNome((String)objFuncionario[1]);
				informacoesFuncionario.setCpf((String)objFuncionario[2].toString());
			}
			
			return informacoesFuncionario;
			
		} catch (Exception e) {
			rollbackTransacao(session);
			throw new BloqueioPersistenceException("Houve um erro ao tentar recuperar o CPF do usuário na tabela de usuário externo no item 2.", e);
		} finally {
			fecharSession(session);
		}

	}
	
    public String obterNomeBanco() {

    	Session session = null;
    	
        try {
        	
        	session = obterSession();
        	
			Query query = session.createNativeQuery("SELECT sys_context('USERENV', 'DB_NAME') FROM DUAL");
			Object nomeBancoObject = query.getSingleResult();
			
			if (nomeBancoObject != null) {
				return nomeBancoObject.toString().toUpperCase();
			} else {
				return null;
			}

            
        } catch (Exception e) {
			rollbackTransacao(session);
			throw new BloqueioPersistenceException("Erro ao tentar recuperar o nome do banco de dados", e);
		} finally {
			fecharSession(session);
		}
        
    }
	
	protected Session obterSession() throws DAOException {

		Session currentSession = super.getSession();

		iniciarTransacao(currentSession);

		return currentSession;
	}

	protected void fecharSession(Session session) throws BloqueioPersistenceException {
		if (session != null)
			session.close();
	}

	protected Transaction iniciarTransacao(Session session) {
		if (!session.getTransaction().isActive())
			return session.beginTransaction();
		return session.getTransaction();
	}

	protected void commitTransacao(Session session) {
		if (session.getTransaction().isActive())
			session.getTransaction().commit();
	}

	protected void rollbackTransacao(Session session) {
		if (session != null && session.getTransaction().isActive()) {
			session.getTransaction().rollback();
		}
	}
	
	protected void commitTransacao(Transaction transaction) {
		if (transaction != null && !transaction.isActive())
			transaction.commit();
	}
	
	protected void rollbackTransacao(Transaction transaction) {
		if (transaction != null && !transaction.isActive())
			transaction.rollback();
	}

}

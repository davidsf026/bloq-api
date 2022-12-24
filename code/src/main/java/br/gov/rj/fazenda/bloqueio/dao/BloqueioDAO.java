package br.gov.rj.fazenda.bloqueio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.gov.rj.fazenda.bloqueio.exception.BloqueioPersistenceException;
import br.gov.sefazrj.sati.sefazwork.model.dao.hibernate.HibernateDAO;
import br.gov.sefazrj.sati.sefazwork.model.exception.DAOException;

public class BloqueioDAO extends HibernateDAO {

	private final Logger log = Logger.getLogger(this.getClass());

	protected Connection connection;
	protected PreparedStatement ps;
	protected ResultSet rs;

	public BloqueioDAO(Class<?> classeNegocio) {
		super(classeNegocio);
	}

	protected Session obterSession() throws DAOException {

		Session currentSession = super.getSession();

		// iniciarTransacao(currentSession);

		return currentSession;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			throw new BloqueioPersistenceException("Erro ao tentar fechar a conexão.", e);
		}
	}

	public void rollback() {
		try {
			if (connection != null) {
				log.debug("Efetuando rollback...");
				connection.rollback();
				log.debug("Rollback executado!");
			}
		} catch (Exception e) {
			throw new BloqueioPersistenceException("Erro ao tentar efetuar o rollback.", e);
		}
	}

	protected ResultSet executeQuery(PreparedStatement ps) throws BloqueioPersistenceException {
		ResultSet rs;
		try {
			rs = ps.executeQuery();
			return rs;
		} catch (Exception e) {
			throw new BloqueioPersistenceException("", e);
		}
	}

}

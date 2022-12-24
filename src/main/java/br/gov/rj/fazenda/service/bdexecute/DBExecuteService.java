package br.gov.rj.fazenda.service.bdexecute;

import java.sql.CallableStatement;
import java.sql.Connection;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.bloqueio.exception.BloqueioException;
import br.gov.rj.fazenda.service.bdexecute.DBExecuteResultado.ResultadoBD;

public class DBExecuteService extends DBExecuteDAO {
	
	private String stringConexao;
	private String usuario;
	private String senha;
	
	public static String INDICADOR_SUCESSO = "0";
	public static String INDICADOR_USUARIO_INEXISTENTE= "1";
	
	public DBExecuteResultado bloquearUsuario(String login, InformacoesConexaoBD informacoesConexaoBD) {
		
		DBExecuteResultado bdResultado = new DBExecuteResultado();
		bdResultado.setNomeBancoCandidato(informacoesConexaoBD.getNomeBDcandidato());
		bdResultado.setObservacao(informacoesConexaoBD.getObservacao());

		try {
		
			validarLogin(login);
			
			getConnection(informacoesConexaoBD.getStringConexao(), informacoesConexaoBD.getUsuario(), informacoesConexaoBD.getSenha());
			
			final CallableStatement cs = connection.prepareCall(ApplicationExternalProperties.BLOQUEIO_BD_FUNCTION_BLOQUEIO_USUARIO_BANCO);
			
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, login);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            cs.execute();

            String indicardorResultado = cs.getString(1);
            String mensagemResultado = cs.getString(3);
                        
            bdResultado.setIndicardorResultado(indicardorResultado);
            bdResultado.setDetalheResultado("Mensagem recebida do BD: " + mensagemResultado);		
			bdResultado.setNomeBancoRetornado(obterNomeBanco(connection));
			
			if (INDICADOR_SUCESSO.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SUCESSO);
			} else if (INDICADOR_USUARIO_INEXISTENTE.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			} else {
				bdResultado.setResultadoBD(ResultadoBD.FALHA);
				bdResultado.setDetalheResultado(mensagemResultado.concat(" - O indicador '" + indicardorResultado + "' não é reconhecido pela aplicação."));
			}

		} catch (Exception e) {
			
			bdResultado.setResultadoBD(ResultadoBD.FALHA);
			bdResultado.setDetalheResultado(e.getMessage());
			if (e.getCause() != null && (e.getCause() instanceof java.sql.SQLException || e.getCause() instanceof java.sql.SQLException)) bdResultado.setDetalheResultado((e.getCause()).getMessage());
			bdResultado.setException(e);
		
        } finally {
        	
        	close(connection);
        }
		
		return bdResultado;
		
	 }
	
	public DBExecuteResultado bloquearUsuarioSSA(String cpf, InformacoesConexaoBD informacoesConexaoBD) {
		
		DBExecuteResultado bdResultado = new DBExecuteResultado();
		bdResultado.setNomeBancoCandidato(informacoesConexaoBD.getNomeBDcandidato());
		bdResultado.setObservacao(informacoesConexaoBD.getObservacao());

		try {
		
			validarCpf(cpf);
			
			getConnection(informacoesConexaoBD.getStringConexao(), informacoesConexaoBD.getUsuario(), informacoesConexaoBD.getSenha());
			
			final CallableStatement cs = connection.prepareCall(ApplicationExternalProperties.BLOQUEIO_BD_FUNCTION_BLOQUEIO_SSA);
			
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, cpf);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            cs.execute();

            String indicardorResultado = cs.getString(1);
            String mensagemResultado = cs.getString(3);
                        
            bdResultado.setIndicardorResultado(indicardorResultado);
            bdResultado.setDetalheResultado("Mensagem recebida do BD: " + mensagemResultado);		
			bdResultado.setNomeBancoRetornado(obterNomeBanco(connection));
			
			if (INDICADOR_SUCESSO.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SUCESSO);
			} else if (INDICADOR_USUARIO_INEXISTENTE.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			} else {
				bdResultado.setResultadoBD(ResultadoBD.FALHA);
				bdResultado.setDetalheResultado(mensagemResultado.concat(" - O indicador '" + indicardorResultado + "' não é reconhecido pela aplicação."));
			}

		} catch (Exception e) {
			
			bdResultado.setResultadoBD(ResultadoBD.FALHA);
			bdResultado.setDetalheResultado(e.getMessage());
			if (e.getCause() != null && (e.getCause() instanceof java.sql.SQLException || e.getCause() instanceof java.sql.SQLException)) bdResultado.setDetalheResultado((e.getCause()).getMessage());
			bdResultado.setException(e);
		
        } finally {
        	
        	close(connection);
        }
		
		return bdResultado;
		
	 }
	
	public DBExecuteResultado bloquearUsuarioSISPAT(String cpf, InformacoesConexaoBD informacoesConexaoBD) {
		
		
		
		DBExecuteResultado bdResultado = new DBExecuteResultado();
		bdResultado.setNomeBancoCandidato(informacoesConexaoBD.getNomeBDcandidato());
		bdResultado.setObservacao(informacoesConexaoBD.getObservacao());

		try {
		
			validarCpf(cpf);
			
			getConnection(informacoesConexaoBD.getStringConexao(), informacoesConexaoBD.getUsuario(), informacoesConexaoBD.getSenha());
			
			final CallableStatement cs = connection.prepareCall(ApplicationExternalProperties.BLOQUEIO_BD_FUNCTION_BLOQUEIO_SISPAT);
			
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, cpf);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            cs.execute();

            String indicardorResultado = cs.getString(1);
            String mensagemResultado = cs.getString(3);
			
            bdResultado.setIndicardorResultado(indicardorResultado);
            bdResultado.setNomeBancoRetornado(obterNomeBanco(connection));
            bdResultado.setDetalheResultado("Mensagem recebida do BD: " + mensagemResultado);
            
			if (INDICADOR_SUCESSO.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SUCESSO);
			} else if (INDICADOR_USUARIO_INEXISTENTE.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SEM_ACAO);
			} else {
				bdResultado.setResultadoBD(ResultadoBD.FALHA);
				bdResultado.setDetalheResultado(mensagemResultado.concat(" - O indicador '" + indicardorResultado + "' não é reconhecido pela aplicação."));
			}

		} catch (Exception e) {
			
			bdResultado.setResultadoBD(ResultadoBD.FALHA);
			bdResultado.setDetalheResultado(e.getMessage());
			if (e.getCause() != null && (e.getCause() instanceof java.sql.SQLException || e.getCause() instanceof java.sql.SQLException)) bdResultado.setDetalheResultado((e.getCause()).getMessage());
			bdResultado.setException(e);
		
        } finally {
        	
        	close(connection);
        }
		
		return bdResultado;
		
	 }
	 
	public DBExecuteResultado bloquearUsuarioSiafeRio(String cpf, InformacoesConexaoBD informacoesConexaoBD) {
		
		DBExecuteResultado bdResultado = new DBExecuteResultado();
		bdResultado.setNomeBancoCandidato(informacoesConexaoBD.getNomeBDcandidato());
		bdResultado.setObservacao(informacoesConexaoBD.getObservacao());

		try {
		
			validarCpf(cpf);
			
			getConnection(informacoesConexaoBD.getStringConexao(), informacoesConexaoBD.getUsuario(), informacoesConexaoBD.getSenha());
			
			final CallableStatement cs = connection.prepareCall(ApplicationExternalProperties.BLOQUEIO_BD_FUNCTION_BLOQUEIO_SIAFE_RIO);
			
			cs.registerOutParameter(1, java.sql.Types.VARCHAR);
			cs.setString(2, cpf);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            cs.execute();

            String indicardorResultado = cs.getString(1);
            String mensagemResultado = cs.getString(3);
                        
            bdResultado.setIndicardorResultado(indicardorResultado);
            bdResultado.setNomeBancoRetornado(obterNomeBanco(connection));
            bdResultado.setDetalheResultado("Mensagem recebeido do BD: " + mensagemResultado);
            
			if (INDICADOR_SUCESSO.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SUCESSO);
				
			} else if (INDICADOR_USUARIO_INEXISTENTE.equals(indicardorResultado)) {
				bdResultado.setResultadoBD(ResultadoBD.SEM_ACAO);
				
			} else {
				bdResultado.setResultadoBD(ResultadoBD.FALHA);
				bdResultado.setDetalheResultado(mensagemResultado.concat("- O indicador '" + indicardorResultado + "' não é reconhecido pela aplicação."));
			}

		} catch (Exception e) {
			
			bdResultado.setResultadoBD(ResultadoBD.FALHA);
			bdResultado.setDetalheResultado(e.getMessage());
			if (e.getCause() != null && (e.getCause() instanceof java.sql.SQLException || e.getCause() instanceof java.sql.SQLException)) bdResultado.setDetalheResultado((e.getCause()).getMessage());
			bdResultado.setException(e);
		
        } finally {
        	
        	close(connection);
        }
		
		return bdResultado;
		
	 }
	
	public void validarCpf(String cpf) {
		if (cpf == null || (cpf != null && cpf.isEmpty())) {
			throw new BloqueioException("CPF é nulo ou vazio. Entre em contato com o administrador do sistema.", new Exception());
		}
	}

	public void validarLogin(String login) {
		if (login == null || (login != null && (login.isEmpty() || login.length() < 3 ))) {
			throw new BloqueioException("CPF é nulo ou vazio. Entre em contato com o administrador do sistema.", new Exception());
		}
	}
	
    public String obterNomeBanco(Connection connection) {

        try {
        	
            ps = connection.prepareStatement("SELECT sys_context('USERENV', 'DB_NAME') FROM DUAL");
            rs = executeQuery(ps);
            
            while(rs.next()) {
            	return rs.getString(1);
            }
            
        } catch (Exception e) {
            throw new DBPersistenceException("Erro ao tentar recuperar o nome do banco de dados.", e);
        }
        
        return null;
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
	    
}

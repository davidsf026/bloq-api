package br.gov.rj.fazenda.service.bdexecute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.rj.fazenda.bloqueio.config.ApplicationExternalProperties;
import br.gov.rj.fazenda.security.CriptografiaUtil;
import oracle.jdbc.OraclePreparedStatement;

/**
 * Classe DAO contendo todas as consultas SQL da aplicação
 * @author fpeclat 28/10/2014
 */
public class DBExecuteDAO {

    private static Map<String, String> cacheParametros = new HashMap<String, String>();  
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected Connection connection;
    protected PreparedStatement ps;
    protected ResultSet rs;

    /**
     * Retorna a conexão ativa. Caso não existe, será criada. Todo o 'movimento' deve trabalhar com a mesma conexão, do
     * início ao fim.
     * @return
     * @throws Exception
     */
    protected Connection getConnection(String stringConexao, String usuario, String senha) throws Exception {
    	
    	if (ApplicationExternalProperties.SEGURANCA_UTILIZAR_SENHAS_CRIPTOGRAFADAS) {
    		senha = CriptografiaUtil.decrypt(senha, CriptografiaUtil.secretKey);
    	}

        try {

        	connection = DriverManager.getConnection(stringConexao, usuario, senha);
        	
//        	connection = DriverManager.getConnection(
//        			"jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = sefdm01-scan.sefnet.rj)(PORT = 1521))) (CONNECT_DATA = (SERVICE_NAME = sef01d)))",
//        			"BLOQ_WEB",
//        			"BLOQ_WEB"
//        		);
        	
            connection.setAutoCommit(false);

            return connection;
        } catch (Exception e) {
            throw new DBPersistenceException("Erro ao tentar obter a conexão de banco.", e);
        }
    }
    
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            throw new DBPersistenceException("Erro ao tentar fechar a conexão.", e);
        }
    }
    
    public void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            throw new DBPersistenceException("Erro ao tentar fechar a conexão.", e);
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
            throw new DBPersistenceException("Erro ao tentar efetuar o rollback.", e);
        }
    }

    /**
     * Metodo responsável por executar o commit. Este método também notifica o último commit como efetuado. Esta
     * estratégia é importante para que manter o número de erros consecutivos de banco para que a aplicação tome uma
     * decisão sobre o que fazer
     * @throws DBPersistenceException
     */
//    public void commit() throws DFePersistenceException {
//        try {
//            getConnection().commit();
//        } catch (Exception e) {
//            throw new DFePersistenceException("", e);
//        }
//    }

    protected ResultSet executeQuery(PreparedStatement ps) throws DBPersistenceException {
        ResultSet rs;
        try {
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new DBPersistenceException("", e);
        }
    }

    protected int executeUpdate(PreparedStatement ps, boolean ignorarUpdateNenhumRegistro) throws DBPersistenceException {
        try {
            int result = ps.executeUpdate();
            if (result == 0 && !ignorarUpdateNenhumRegistro) {
                throw new DBPersistenceException("O update não alterou nenhum registro", new Throwable("O update não alterou nenhum registro"));
            }
            return result;
        } catch (Exception e) {
            throw new DBPersistenceException("", e);
        }
    }
    
    protected int executeUpdate(PreparedStatement ps) throws DBPersistenceException {
    	return executeUpdate(ps, false);
    }
    
    
    /**
     * @param movimento
     * @param tipoAmbiente
     * @param tipoServico
     * @return
     */

    public String exibirParametros(Object... parametros) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        sb.append("(");
        for (Object parametro : parametros) {
            if (parametro instanceof String)
                parametro = "'" + parametro + "'";
            sb.append((i != 0 ? ", " : "") + (parametro != null ? parametro.toString() : "null"));
            i++;
        }
        sb.append(")");
        return sb.toString();
    }

}

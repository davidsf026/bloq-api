-- BLOQUEIO BD
CREATE OR REPLACE PROCEDURE BLOQ.KILL_SESSION(P_USERNAME VARCHAR2)
IS
BEGIN
  IF (UPPER(P_USERNAME) IN ('SYS','SYSTEM')) THEN 
    RAISE_APPLICATION_ERROR(-20000,'ATTEMPT TO KILL PROTECTED SYSTEM SESSION HAS BEEN BLOCKED.');
  ELSE
    FOR C IN (SELECT SID, SERIAL#, INST_ID FROM GV$SESSION WHERE (UPPER(USERNAME) = UPPER(P_USERNAME)) OR (UPPER(USERNAME) = UPPER(P_USERNAME||'_SQL')) ) LOOP
      EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION '''||C.SID||','||C.SERIAL#||',@'||C.INST_ID||'''';
    END LOOP;
  END IF;
END;
/
GRANT EXECUTE ON BLOQ.KILL_SESSION TO RL_BLOQ_PRV;
/
GRANT EXECUTE ON BLOQ.KILL_SESSION TO BLOQ_WEB;
/

CREATE OR REPLACE FUNCTION BLOQ.FC_BLOQUEIO_USUARIO(P_USERNAME IN VARCHAR2, P_MSG OUT VARCHAR2)
                  RETURN NUMBER IS CODERR NUMBER;

  VS_COMANDO_BLOCK VARCHAR2(200);
  VI_QTD_USUARIOS INTEGER;
  BEGIN

  SELECT COUNT(*) INTO VI_QTD_USUARIOS FROM ALL_USERS U WHERE (UPPER(USERNAME) = UPPER(P_USERNAME)) OR (UPPER(USERNAME) = UPPER(P_USERNAME||'_SQL'));

  IF VI_QTD_USUARIOS = 0
  THEN
    P_MSG  := 'Usuário ' || P_USERNAME || ' não existe no banco de dados';
    CODERR := 1;
    RETURN(CODERR);

  ELSE
    FOR C IN (SELECT * FROM ALL_USERS U WHERE (UPPER(USERNAME) = UPPER(P_USERNAME)) OR (UPPER(USERNAME) = UPPER(P_USERNAME||'_SQL'))) LOOP
      VS_COMANDO_BLOCK := 'ALTER USER '||C.USERNAME||' ACCOUNT LOCK';
      BLOQ.KILL_SESSION(C.USERNAME);
      EXECUTE IMMEDIATE VS_COMANDO_BLOCK;
    END LOOP;

    P_MSG  := 'Usuário bloqueado com sucesso';
    CODERR := 0;
    RETURN(CODERR);

  END IF;

  EXCEPTION
    WHEN OTHERS THEN
      CODERR := 2;
      P_MSG :=' Erro ao desabilitar o usuário ' || P_USERNAME || ' - ' || SQLERRM;
      RETURN(CODERR);

END FC_BLOQUEIO_USUARIO;
/
GRANT EXECUTE ON BLOQ.FC_BLOQUEIO_USUARIO TO RL_BLOQ_PRV;
/
GRANT EXECUTE ON BLOQ.FC_BLOQUEIO_USUARIO TO BLOQ_WEB;
/
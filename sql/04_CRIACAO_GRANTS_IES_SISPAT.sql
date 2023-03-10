-- BLOQUEIO SISPAT
GRANT EXECUTE ON SISPAT.IS_PK_SISPAT TO BLOQ WITH GRANT OPTION;
/
GRANT EXECUTE ON SISPAT.IS_PK_SISPAT TO RL_BLOQ_PRV;
/
CREATE OR REPLACE PACKAGE BLOQ.IE_PK_SISPAT_BLOQUEAR IS
 FUNCTION FC_BLOQUEIO_SISPAT(P_NUM_CPF IN NUMBER, P_MSG OUT VARCHAR2)  RETURN NUMBER;
END IE_PK_SISPAT_BLOQUEAR;
/
CREATE OR REPLACE PACKAGE BODY BLOQ.IE_PK_SISPAT_BLOQUEAR IS
 FUNCTION FC_BLOQUEIO_SISPAT(P_NUM_CPF IN NUMBER, P_MSG OUT VARCHAR2) RETURN NUMBER IS CODERR NUMBER;
  BEGIN

    SISPAT.IS_PK_SISPAT.PR_BLOQUEIA_USUARIO(P_NUM_CPF, CODERR, P_MSG);
    RETURN(CODERR);

   EXCEPTION
     WHEN OTHERS THEN
       CODERR := 2;
       P_MSG :='Erro ao desabilitar o usuário cpf ' || P_NUM_CPF || ' no SIAFERIO';
       RETURN(CODERR);
  END;
END IE_PK_SISPAT_BLOQUEAR;
/

GRANT EXECUTE ON BLOQ.IE_PK_SISPAT_BLOQUEAR TO RL_BLOQ_PRV;
/
GRANT EXECUTE ON BLOQ.IE_PK_SISPAT_BLOQUEAR TO BLOQ_WEB;
/
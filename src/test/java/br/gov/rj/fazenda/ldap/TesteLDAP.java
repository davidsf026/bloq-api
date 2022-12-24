package br.gov.rj.fazenda.ldap;

import br.gov.rj.fazenda.service.ldap.LDAPChange;

public class TesteLDAP {

	public static void main(String[] args) {
	

		LDAPChange ldap = new LDAPChange();

		try {
			System.out.println("usuario " + ldap.consultar("exoneracao").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("usuario DN " + ldap.consultarDN("Bloqueioad1").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("desabilita usuario2 " + ldap.desbHabUsuario("Bloqueioad2",ldap.getACCOUNT_DISABLE()).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("habilita usuario2 " + ldap.desbHabUsuario("Bloqueioad2",ldap.getACCOUNT_ENABLE()).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

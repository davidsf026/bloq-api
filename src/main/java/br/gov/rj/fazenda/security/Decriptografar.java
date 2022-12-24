package br.gov.rj.fazenda.security;

import java.util.Scanner;

public class Decriptografar {
	
    public static void main(String args[]) {
		
		System.out.print("Entre com a senten�a criptografada: ");
	    Scanner scanner = new Scanner(System. in);
	    String originalString = scanner.nextLine();
	    scanner.close();
	    
	    System.out.println("Sentenca descriptografada: " + CriptografiaUtil.decrypt(originalString, CriptografiaUtil.secretKey));

    }
	
}
	
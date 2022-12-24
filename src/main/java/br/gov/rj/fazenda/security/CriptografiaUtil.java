package br.gov.rj.fazenda.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class CriptografiaUtil {

	private static SecretKeySpec secretKeySpec;
	private static byte[] key;
	
	public final static String secretKey = "7&k3$9zwsklTR@";

	public static void main(String[] args) {

		System.out.print("Enter a string : ");
		Scanner scanner = new Scanner(System.in);
		String originalString = scanner.nextLine();
		scanner.close();

		System.out.println(originalString);

		String encryptedString = encrypt(originalString, secretKey);
		String decryptedString = decrypt(encryptedString, secretKey);

		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedString);
	}

	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKeySpec = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt, String secret) {

		try {
			setKey(secret);
			long inicio = new Date().getTime();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			long iniciobase64 = new Date().getTime();
			String stringFinal = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			long fimbase64 = new Date().getTime();
			long fim = new Date().getTime();
			// System.out.println("Criptografia em " + (fim - inicio));
			// System.out.println("Base64 em " + (fimbase64- iniciobase64));
			return stringFinal;
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}

		return null;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		long inicio = new Date().getTime();

		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

			String stringFinal = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			long fim = new Date().getTime();
			return stringFinal;
		} catch (Exception e) {
			throw new RuntimeException("Erro na decriptografia da senha.", e);
		}

	}

}

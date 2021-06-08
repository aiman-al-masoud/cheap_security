package rsa.tests;

import java.math.BigInteger;

import rsa.RSA;

public class Tester {

	public static void main(String[] args) {
		
		
		//create a new RSA object with 300-digit (base 10) based encrypton. 
		RSA rsa = new RSA(300);
		
		//encrypt a string of chars. You can choose what public key to use for encryption.
		String ecnrypted = rsa.encryptText("questo e un bel messaggio", rsa.getPublicKey()[0].toString(), rsa.getPublicKey()[1].toString());
		//decipher the string of chars using the RSA object's private key.
		String decrypted = rsa.decryptText(ecnrypted);
		
		//print the encrypted string
		System.out.println(ecnrypted);
		//print the deciphered string
		System.out.println(decrypted);
		
	}

}

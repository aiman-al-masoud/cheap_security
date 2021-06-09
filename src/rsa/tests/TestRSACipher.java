package rsa.tests;

import rsa.RSACipher;

public class TestRSACipher {

	public static void main(String[] args) {
		
		RSACipher rsa = new RSACipher(300);
		
		String cipherText = rsa.encrypt("ciao mondo");
		
		
		System.out.println(cipherText);
		
		
		String decipheredText = rsa.decipher(cipherText);
		
		System.out.println(decipheredText);
		
	}

}

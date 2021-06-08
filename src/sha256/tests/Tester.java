package sha256.tests;

import sha256.SHA256;

public class Tester {

	public static void main(String[] args) {
		
		String plaintext = "hello world!";
		String plaintextCopy = new String(plaintext);
		String totallyDifferent = "hello world";
		
		SHA256 sha = new SHA256();
		
		String encrypted = sha.encrypt(plaintext);
		String encryptedCopy = sha.encrypt(plaintextCopy);
		String encryptedDifferent = sha.encrypt(totallyDifferent);
		
		System.out.println(encrypted);
		System.out.println(encryptedCopy);
		System.out.println(encryptedDifferent);
	}
	
	

}

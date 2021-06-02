package vigenere.tests;

import vigenere.Vigenere;

public class RandomKeyGeneratorTester {

	/**
	 * Generate a random key.
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Vigenere encrypter = new Vigenere();
		
		System.out.println(encrypter.generateRandomKey(30));
		
	}

}

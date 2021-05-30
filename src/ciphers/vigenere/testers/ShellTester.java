package ciphers.vigenere.testers;


import java.util.Scanner;

import ciphers.vigenere.Vigenere;

public class ShellTester {
	
	/**
	 * Encrypt and decipher strings of alphabetical text, with a custom key as 
	 * well as with a randomly generated key.
	 * @param args
	 */

	public static void main(String[] args) {
		
		
		//make a new encrypter object
		Vigenere ecrypter = new Vigenere();
		
		//make a new scanner object
		Scanner scanner = new Scanner(System.in);
		
	
		
		while(true) {
			
			System.out.println("(en)crypt or (de)crypt?");
			
			if(scanner.nextLine().toUpperCase().contains("EN")) {
				
				System.out.println("enter plaintext:");
				String plaintext = scanner.nextLine();
				
				System.out.println("(ch)oose key or (ge)nerate OTP?");
				String key = "";
				if(scanner.nextLine().toUpperCase().contains("CH")) {
					System.out.println("enter key:");
					key = scanner.nextLine();
				}else {
					key = ecrypter.generateRandomKey(plaintext.length());
				}
				
				System.out.println("this is the encrypted text:");
				System.out.println(ecrypter.encryptString(key, plaintext));
				System.out.println("this is the OTP: (Write it down!)");
				System.out.println(key);
				
			
			}else{
				System.out.println("enter key:");
				String key = scanner.nextLine();
				System.out.println("enter encrypted text:");
				String encrText = scanner.nextLine();
				System.out.println("this is the plaintext:");
				System.out.println(ecrypter.decryptString(key, encrText));
			
			}
			
		}
		
		
				
			
				
				
		
		
		
		
		
		
		
	}

}

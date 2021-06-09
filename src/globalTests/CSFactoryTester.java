package globalTests;

import interfaces.CSFactory;
import interfaces.CipherIF;
import interfaces.HasherIF;

public class CSFactoryTester {

	public static void main(String[] args) {
		
		
		//building an RSACipher:
		CipherIF rsa = CSFactory.getInstance().buildCipher("rsa");
		
		//building another RSACipher
		CipherIF rsaTwo = CSFactory.getInstance().buildCipher("rsa");
		
		//exchange public encryption keys
		rsa.setEncryptionKey(rsaTwo.getPublicKey());
		rsaTwo.setEncryptionKey(rsa.getPublicKey());
		
		//rsa sends a message to rsaTwo
		String ecryptedForRsaTwo = rsa.encrypt("hello rsaTwo");
		//rsaTwo deciphers the message:
		System.out.println(rsaTwo.decipher(ecryptedForRsaTwo));
		//rsaTwo replies to the rsa's message
		String ecryptedForRsa = rsaTwo.encrypt("well hello there sucker lol yeah");
		//rsa deciphers the reply
		System.out.println(rsa.decipher(ecryptedForRsa));
		
		//building a vigenere cipher
		CipherIF vigenere = CSFactory.getInstance().buildCipher("vigenere");
		String ciphertext = vigenere.encrypt("ciao");
		String deciphered = vigenere.decipher(ciphertext);
		System.out.println(ciphertext);
		System.out.println(deciphered);
		
		//building a SHA256Hasher 
		HasherIF sha = CSFactory.getInstance().buildHasher("sha256");
		ciphertext = sha.encrypt("ciao");
		System.out.println(ciphertext);
		
	}

}

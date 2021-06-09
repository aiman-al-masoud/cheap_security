package interfaces;

import rsa.RSACipher;
import sha256.SHA256Hasher;
import vigenere.VigenereCipher;

public class CSFactory {
	
	private static CSFactory instance;
	
	private CSFactory(){
		
	}
	
	public static CSFactory getInstance() {
		
		if(instance==null) {
			instance = new CSFactory();
		}
		return instance;
	}
	

	int DEFAULT_RSA_BASE_TEN_DIGITS = 300;
	int DEFAULT_VIGENERE_RADOM_KEY_SIZE = 100; 
	
	//cipher types
	public static final String RSA = "RSA";
	
	//hasher types 
	public static final String SHA256 = "SHA256";
	public static final String VIGENERE = "VIGENERE";
	
	
	public CipherIF buildCipher(String type) {
		switch(type.toUpperCase()) {
		case RSA:
			return new RSACipher(DEFAULT_RSA_BASE_TEN_DIGITS);
		case VIGENERE:
			return new VigenereCipher(DEFAULT_VIGENERE_RADOM_KEY_SIZE);
		
		}
		
		
		throw new IllegalArgumentException("no such cipher");
	}
	
	
	public HasherIF buildHasher(String type) {
		switch(type.toUpperCase()) {
		
		case SHA256:
			return new SHA256Hasher();
		
		}
		
		throw new IllegalArgumentException("no such hasher");
	}
	
	
	
}

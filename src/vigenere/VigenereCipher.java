package vigenere;

import interfaces.CipherIF;

public class VigenereCipher implements CipherIF {

	Vigenere vigenere;
	String key;
	
	
	public VigenereCipher(int keyLength) {
		vigenere = new Vigenere();
		key = vigenere.generateRandomKey(keyLength);
	}

	public VigenereCipher(String key) {
		vigenere = new Vigenere();
		this.key = key;
	}


	@Override
	public String encrypt(String plaintext) {
		return vigenere.encryptString(key, plaintext);
	}

	@Override
	public void setEncryptionKey(String encryptionKey) {
		this.key = encryptionKey;
	}

	@Override
	public void setDecryptionKey(String decryptionKey) {
		this.key = decryptionKey;
	}

	@Override
	public String decipher(String ciphertext) {
		return vigenere.decryptString(key, ciphertext);
	}

	@Override
	public String getPublicKey() {
		return key;
	}

	@Override
	public String getType() {
		return "Vigenere_Cipher";
	}

}

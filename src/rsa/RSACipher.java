package rsa;

import interfaces.CipherIF;

public class RSACipher implements CipherIF {

	RSA rsa;
	
	/**
	 * In the format: e n (e and n separated by whitespace)
	 */
	String encryptionKey;
	
	
	String decryptionKey;
	
	
	public RSACipher(int numberOfBaseTenDigits){
		rsa = new RSA(numberOfBaseTenDigits);
		
	}


	@Override
	public String encrypt(String plaintext) {
		if(encryptionKey==null) {
			return rsa.encryptText(plaintext, rsa.getPublicKey()[0].toString(), rsa.getPublicKey()[1].toString());
		}
		
		String[] keyParts = encryptionKey.split("\\s+");
		
		return rsa.encryptText(plaintext, keyParts[0].trim(), keyParts[1].trim());
		
	}


	@Override
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}


	@Override
	public void setDecryptionKey(String decryptionKey) {
		this.decryptionKey = decryptionKey;
	}


	@Override
	public String decipher(String ciphertext) {
		if(decryptionKey==null) {
			return rsa.decryptText(ciphertext);
		}
		
		return null;
	}


	@Override
	public String getPublicKey() {
		return rsa.getPublicKey()[0]+" "+rsa.getPublicKey()[1];
	}


	@Override
	public String getType() {
		return "RSA_Cipher";
	}
	
	


}

package interfaces;

import java.math.BigInteger;

public interface CipherIF extends EncrypterIF{

	public void setEncryptionKey(String encryptionKey);
	public void setDecryptionKey(String decryptionKey);
	public String decipher(String ciphertext);
	public String getPublicKey();
	public String getType();


}

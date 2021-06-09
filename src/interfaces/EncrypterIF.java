package interfaces;

import java.io.Serializable;

public interface EncrypterIF extends Serializable{

	public String encrypt(String plaintext);
	public String getType();
	
}

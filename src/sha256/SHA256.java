package sha256;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256 is a popular hashing algorithm. It can be used 
 * to store passwords safely. Password attempts by the user 
 * can then be encrypted and compared to the stored encrypted 
 * password. The idea is that it's very difficult to find a 
 * string that hashes to the same exact hash as the password.
 * 
 *
 */
public class SHA256 implements Serializable{

	MessageDigest digest;

	public SHA256() {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {}
	}

	/**
	 * Returns the hash of plaintext in the form of a base-16 number-string.
	 * @param plaintext
	 * @return
	 */
	public String encrypt(String plaintext) {
		
		//hash the text into an array of bytes
		byte[] hashArray = digest.digest(plaintext.getBytes());
		String binaryString = "";

		//convert the hash into binarystring form
		for(byte b : hashArray) {
			binaryString+=Integer.toBinaryString(b);
		}

		//represent the hash as a hexadecimal number
		return new BigInteger(binaryString,2).toString(16);
	}







}

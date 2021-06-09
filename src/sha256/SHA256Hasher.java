package sha256;

import interfaces.HasherIF;

public class SHA256Hasher implements HasherIF {

	SHA256 sha;
	
	public SHA256Hasher() {
		sha = new SHA256();
	}
	
	@Override
	public String encrypt(String plaintext) {
		return sha.encrypt(plaintext);
	}

	@Override
	public String getType() {
		return "SHA256_Hasher";
	}

}

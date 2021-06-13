package rsa.tests;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class StringsAndByteArrays {

	public static void main(String[] args) {

		String plaintext = "ciao, questo e' abbastanza migliore";
		byte[] bytes = plaintext.getBytes(StandardCharsets.US_ASCII);

		//convert to binary string 
		String binaryString = "";
		String byteBuf = null;
		for(byte b : bytes) {
			byteBuf=Integer.toBinaryString(b);
			byteBuf = addBinaryPadding(8, byteBuf);
			binaryString+=byteBuf;
		}

		
		
		
		

		//reconstruct byte array from binary string 
		byte[] bytesCopy = new BigInteger(binaryString, 2).toByteArray();


		String copy = new String(bytesCopy, StandardCharsets.US_ASCII);


		System.out.println(copy);


	}
	
	
	
	
	
	
	/**
	 * Make a binary word compatible with a given word-size by adding zeroes to the left.
	 * @param sizeOfWordInBits
	 * @param word
	 * @return
	 */
	public static String addBinaryPadding(int sizeOfWordInBits, String word) {
		//int STANDARD_SIZE = 8;//size of a byte in bits 
		String padding = "";
		for(int i =0; i<sizeOfWordInBits-word.length(); i++) {
			padding+="0";
		}
		return padding+word;
	}
	
	
	
	
	
	

}

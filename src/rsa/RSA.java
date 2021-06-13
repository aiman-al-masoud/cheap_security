package rsa;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import rsa.primes.Primebably;

/**
 * This class was inspired by this python implementation of RSA:
 * 
 * https://gist.github.com/marnix135/582c78891b29186ba4c6882a4bc62822
 * 
 * Very many thanks to the original author.
 * 
 * 
 * Any suggestion for improvements would be warmly welcomed, as I'm very shaky in the relevant maths.
 */


public class RSA implements Serializable{


	private BigInteger publicExponentE;
	private BigInteger privateExponentD;
	private BigInteger firstPrimeP;
	private BigInteger secondPrimeQ;
	private BigInteger productN;
	private BigInteger totientPhi;


	public RSA(int numberOfbaseTenDigits) {

		//any number
		int numberOfbaseTenDigitsOffset = ThreadLocalRandom.current().nextInt(1, 3); 

		boolean isOk = false;
		while(!isOk) {
			try {
				String firstPrimeP = new Primebably().getPrime(numberOfbaseTenDigits).toString();
				String secondPrimeQ = new Primebably().getPrime(numberOfbaseTenDigits+numberOfbaseTenDigitsOffset).toString();
				generateKeys(firstPrimeP, secondPrimeQ);
				isOk = true;
			}catch(IllegalArgumentException e) {
				isOk = false;
			}
		}

	}


	/**
	 * Initialize RSA with a pair of ready-found primes
	 * @param firstPrimeP
	 * @param secondPrimeQ
	 */
	public RSA(String firstPrimeP, String secondPrimeQ ) {
		generateKeys(firstPrimeP, secondPrimeQ);
	}




	/**
	 * Gets g, x, y from ax+by=gcd(a,b)
	 */
	public BigInteger[] extendedEuclidean(String aString, String bString) {
		BigInteger a = new BigInteger(aString);
		BigInteger b = new BigInteger(bString);
		BigInteger[] results = new BigInteger[3]; 

		BigInteger gcd = new BigInteger("0");
		BigInteger x = new BigInteger("0");
		BigInteger y = new BigInteger("0");


		//if a = 0, 
		if(a.intValue()==0) {
			results[0] = b; 
			results[1] = new BigInteger("0");
			results[2] = new BigInteger("1"); 
			return results;
		}


		results = extendedEuclidean(b.mod(a).toString(), a.toString());
		gcd = results[0];
		y = results[1];
		x = results[2];

		results[0] = gcd;
		results[1] = x.subtract( b.divide(a).multiply(y) );
		results[2] = y;

		return results;
		//0->gcd, 1->x, 2->y
	}


	private BigInteger computePrivateKey(String aString, String phiString) {
		BigInteger[] results = extendedEuclidean(aString, phiString);

		//if g!=1
		if(results[0].intValue()!=1) {
			throw new IllegalArgumentException("e*d%Phi != 1, probably illegal p, q");
		}

		return results[1].mod(new BigInteger(phiString));	
	}


	public void generateKeys(String firstPrimeP, String secondPrimeQ) {

		this.firstPrimeP = new BigInteger(firstPrimeP);
		this.secondPrimeQ = new BigInteger(secondPrimeQ);
		this.productN = this.firstPrimeP.multiply(this.secondPrimeQ);
		this.totientPhi = (this.firstPrimeP.subtract(new BigInteger("1"))).multiply(this.secondPrimeQ.subtract(new BigInteger("1")));
		this.publicExponentE = generatePublicExponentE(totientPhi);
		this.privateExponentD = computePrivateKey(publicExponentE.toString(), totientPhi.toString());

		if(totientPhi.mod(publicExponentE).intValue()==0 || publicExponentE.compareTo(totientPhi) >0) {
			throw new IllegalArgumentException("'e' must be less than and coprime to Phi!");
		}

	}

	public BigInteger[] getPublicKey() {
		BigInteger[] publicKey = new BigInteger[2];
		publicKey[0] = publicExponentE;
		publicKey[1] = productN;
		return publicKey;
	}


	private BigInteger[] getPrivateKey() {
		BigInteger[] privateKey = new BigInteger[2];
		privateKey[0] = privateExponentD;
		privateKey[1] = productN;
		return privateKey;
	}


	/**
	 * publicExponentE must be coprime and less than totientPhi.
	 * @param totientPhi
	 * @return
	 */
	private BigInteger generatePublicExponentE(BigInteger totientPhi) {	

		BigInteger 	randomNum = new BigInteger(ThreadLocalRandom.current().nextInt(1 , 20 )+"");

		do {
			randomNum = randomNum.add(new BigInteger("1"));
		}while( extendedEuclidean(randomNum.toString(), totientPhi.toString())[0].intValue()!=1  );

		//return new BigInteger("3");
		return randomNum;
	}



	/**
	 * Uses publicKey (potentially of others) to encrypt messages to be sent.
	 * @param plainNum
	 * @param publicKey
	 * @return
	 */
	public String encrypt(String plainNum, String publicExponent, String publicProductN) {

		BigInteger plainNumInt = new BigInteger(plainNum);
		BigInteger publicExponentInt = new BigInteger(publicExponent);
		BigInteger publicProductNInt = new BigInteger(publicProductN);

		return plainNumInt.modPow(publicExponentInt, publicProductNInt).toString();	
	}


	/**
	 * Encrypts a number with the private key of this object.
	 * @param plainNum
	 * @return
	 */
	public String encryptWithPrivate(String plainNum) {
		return encrypt(plainNum, getPrivateKey()[0].toString(), getPrivateKey()[1].toString());
	}


	
	/**
	 * Deciphers a number using any provided key.
	 * @param cipherNum
	 * @param d
	 * @param n
	 * @return
	 */

	public String decrypt(String cipherNum, String d, String n) {
		BigInteger cipherNumInt = new BigInteger(cipherNum);
		return cipherNumInt.modPow(new BigInteger(d), new BigInteger(n)).toString();
	}
	
	/**
	 * Uses private (its own) key to decipher messages sent to it by others.
	 * @param cipherNum
	 * @return
	 */

	public String decrypt(String cipherNum) {
		return decrypt(cipherNum, getPrivateKey()[0].toString(), getPrivateKey()[1].toString());
	}



	/**
	 * Encrypts a string of text, obtaining a single decimal number string.
	 * @param plaintext
	 * @param e
	 * @param n
	 * @return
	 */
	public String encryptText(String plaintext, String e, String n) {

		//convert the plaintext into a (longish ;-( ) binary string 
		String binaryString = "";

		for(byte b : plaintext.getBytes(StandardCharsets.US_ASCII)) {
			
			//get the bytes corresponding to the encoding of the chars
			String binaryBuf = Integer.toBinaryString(b);	
			//make sure that each binaryBuf is exactly 8 bits long. 
			binaryBuf = addBinaryPadding(8, binaryBuf);
			//concatenate the binaryBufs to form a huge binary string
			binaryString+=binaryBuf;
		}
				
		//the binary string represents a number, you can convert it to base 10
		BigInteger plainNumber = new BigInteger(binaryString, 2);

		//the base 10 number can be raised to a power with a modulus (encrypted!)
		String cipheredNumber = encrypt(plainNumber.toString(), e, n);

		//NOW there is absolutely no way of getting the original binary string back without the private key! And no chance of applying frequency analysis!
		return cipheredNumber;
	}



	/**
	 * Encrypts text with this object's private key.
	 * @param plaintext
	 * @return
	 */
	public String encryptTextWithPrivate(String plaintext) {
		return encryptText(plaintext,getPrivateKey()[0].toString(),  getPrivateKey()[1].toString());
	}




	/**
	 * Turn a cipheredNumber string back to intelligible text
	 * @param cipheredNumber
	 * @return
	 */
	public String decryptText(String cipheredNumber, String d, String n) {

		//the number can be deciphered...
		String decipheredNumber = decrypt(cipheredNumber, d, n);

		//converted back to a binary string... 
		String decipheredBinaryString = new BigInteger(decipheredNumber).toString(2);
			
		//add a non-significant zero.
		decipheredBinaryString="0"+decipheredBinaryString;
		
		//convert the binary string to an array of bytes
		byte[] bytesCopy = new BigInteger(decipheredBinaryString, 2).toByteArray();

		//convert the array of bytes into an ascii-encoded string
		String decipheredText = new String(bytesCopy, StandardCharsets.US_ASCII);
		
		return decipheredText;
	}


	public String decryptText(String cipheredNumber) {
		return decryptText(cipheredNumber, getPrivateKey()[0].toString(), getPrivateKey()[1].toString());
	}


	/**
	 * Make a binary word compatible with a given word-size by adding zeroes to the left.
	 * @param sizeOfWordInBits
	 * @param word
	 * @return
	 */
	public static String addBinaryPadding(int sizeOfWordInBits, String word) { 
		String padding = "";
		for(int i =0; i<sizeOfWordInBits-word.length(); i++) {
			padding+="0";
		}
		return padding+word;
	}
	







































}

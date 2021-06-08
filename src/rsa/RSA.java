package rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
	 * Uses private (its own) key to decipher messages sent to it by others.
	 * @param cipherNum
	 * @return
	 */

	public String decrypt(String cipherNum) {
		BigInteger cipherNumInt = new BigInteger(cipherNum);
		BigInteger[] privateKey = getPrivateKey();

		return cipherNumInt.modPow(privateKey[0], privateKey[1]).toString();
	}


	/**
	 *Encrypts a string of text, obtaining a single decimal number string.
	 * @param plaintext
	 * @param e
	 * @param n
	 * @return
	 */
	public String encryptText(String plaintext, String e, String n) {

		//convert whitespaces to uderscores, 'cuz spaces don't appear to work well with 7-bit ASCII for some reason :-( ...
		String[] words = plaintext.split("\\s+");
		plaintext = "";
		for(String word : words) {
			plaintext+=word+"_";
		}
		
		
		String binaryString = "";
		//convert the plaintext into a (longish ;-( ) binary string 
		for(byte b : plaintext.getBytes()) {
			binaryString+=Integer.toBinaryString(b);
		}
		//System.out.println(binaryString);

		//the binary string represents a number, you can convert it to base 10
		BigInteger plainNumber = new BigInteger(binaryString, 2);
		//System.out.println(plainNumber);

		//the base 10 number can be raised to a power with a modulus (encrypted!)
		String cipheredNumber = encrypt(plainNumber.toString(), e, n);
		//System.out.println(cipheredNumber);

		//NOW there is absolutely no way of getting the original binary string back without the private key! And no chance of applying frequency analysis!
		return cipheredNumber;
	}



	/**
	 * Turn a cipheredNumber string back to intelligible text
	 * @param cipheredNumber
	 * @return
	 */
	public String decryptText(String cipheredNumber) {

		//the number can be deciphered...
		String decipheredNumber = decrypt(cipheredNumber);
		//System.out.println(decipheredNumber);

		//converted back to a binary string... 
		String decipheredBinaryString = new BigInteger(decipheredNumber).toString(2);
		//System.out.println(decipheredBinaryString);

		//and finally, the binary string can be interpreted as an array of (ASCII) chars
		String decipheredText = "";
		String accumulator = "";
		for(int i =0; i<decipheredBinaryString.length(); i++) {

			//accumulate chars of string till accumulator.lenth ==8
			accumulator+=decipheredBinaryString.charAt(i);

			//8 digits accumulated, proceed to converting them to an ASCII char
			if(accumulator.length()==7) {
				decipheredText+=(char)Integer.parseInt(accumulator,2);
				accumulator=""; //reset accumulator!
			}			
		}

		
		//convert underscores back to spaces
		String[] words = decipheredText.split("_");
		decipheredText = "";
		for(String word : words) {
			decipheredText+=word+" ";
		}
		
		return decipheredText;
	}
	
	








































}

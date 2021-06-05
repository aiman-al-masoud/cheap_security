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
		
		//BigInteger randomNum = new BigInteger(ThreadLocalRandom.current().nextInt(1 , totientPhi.intValue()-1)+"");
		
		//System.out.println(randomNum);
		
		return new BigInteger("3");
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
	 * @overload
	 * Encrypt an ascii char, turning it into a String-represented int.
	 * @param character
	 * @param publicExponent
	 * @param publicProductN
	 * @return
	 */
	public String encrypt(char character, String publicExponent, String publicProductN) {
		return encrypt( ((int)character)+"" , publicExponent, publicProductN);
	}



	/**
	 * Encrypts a string of text into a series of integers, based on a provided public key (publicExponentE, publicProductN).
	 * @param text
	 * @param publicExponent
	 * @param publicProductN
	 * @return
	 */

	public String encryptText(String text, String publicExponentE, String publicProductN) {
		String encrypted = "";
		for(int i=0; i<text.length(); i++) {
			encrypted+=encrypt(text.charAt(i), publicExponentE, publicProductN)+" ";
		}
		return encrypted;
	}


	/**
	 * Deciphers a string of space-separated numbers assumed to be a string of chars encrypted using this RSA's public key.
	 * @param cipherText
	 * @return
	 */
	public String decryptText(String cipherText) {
		String[] letters  = cipherText.split(" ");
		String deciphered = "";
		
		for(String letter : letters) {
			deciphered+=((char)Integer.parseInt(decrypt(letter)));
		}
		
		return deciphered;
	}

	
	
	public void save(String pathname){
		try {
			ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(pathname));
			objOutStream.writeObject(this);
			objOutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static RSA load(String pathname) {
		try {
			ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(new File(pathname)));
			return (RSA)objInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	































}

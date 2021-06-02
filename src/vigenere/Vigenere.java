package vigenere;

import java.util.Random;

/**
 * Public methods:
 * 	->encryptString(String key, String plaintext): encrypts a string with a given key.
 * 	->decryptString(String key, String encryptedText): attempts to 'decrypt' a string assuming that it is encrypted with the given key.
 *  ->generateRandomKey(int keyLength): generates a random key-string of arbitrary length.
 *  
 *  This Object contains a simple java-implementation of the Vigenere Cipher, 
 *  an old and popular polyalphabetic cipher. Using this Cipher is esentially
 *  like encrypting each letter of the plain-text with a (potentially) different 
 *  alphabet, based on the encryption key provided. In other words, it's like having multiple 
 *  Caesar Ciphers, one for each letter. The advantage being, that it makes the text 
 *  immune to simple frequency analysis. Because, unlike in a monoalphabetic cipher, there is 
 *  no one-to-one relationship between any pair of letters (from plain-text vs cipher-text).
 *  
 *  Of course, nowadays, this cipher can still be cracked, but it's kinda' complicated to explain (or even understand) how :-)
 *  
 *  Suffice to say, that if you use a random key that is longer than the text to encrypt your top-secret high-stake pieces of 
 *  exclusive insights, you should generally be fine.
 *
 *
 *
 */

public class Vigenere {

	//this table is basically a 26*26 letters matrix
	private char[][] tabulaRecta;

	//this is a letters array in standard alph order
	private char[] lettersArray;



	public Vigenere(){

		//initializing alph array
		lettersArray = new char[26];
		for(int i =0; i<26; i++) {
			lettersArray[i] = (char)(i+65);
		}

		//initializing tabula recta
		//i- rows: keyword letters. j-columns: plaintext letters
		tabulaRecta = new char[26][26];
		int displacement = 0;
		for(int i=0; i<26; i++) {
			for(int j=0; j<26; j++) {
				int slideChar = j+65+displacement;
				tabulaRecta[i][j] = (char)( slideChar<=90 ? slideChar : slideChar-26);
			}
			displacement++;
		}

	}


	/**
	 * encrypt a string 
	 * @param key
	 * @param plaintext
	 * @return
	 */
	public String encryptString(String key, String plaintext) {

		//this is the string that will be returned
		String encryptedText = "";

		//parallel running key string counter
		int keyStringCounter = 0;

		//go through each letter of the plaintext
		for(int i =0; i<plaintext.length(); i++) {
			encryptedText += encryptLetter( key.charAt(keyStringCounter) , plaintext.charAt(i));
			keyStringCounter++;
			if(keyStringCounter==key.length()) {
				keyStringCounter = 0;
			}
		}

		return encryptedText;
	}


	/**
	 * 	decrypt a string
	 * @param key
	 * @param encryptedText
	 * @return
	 */

	public String decryptString(String key, String encryptedText) {

		//result to be returned
		String decryptedText = "";

		//parallel running key string counter
		int keyStringCounter = 0;

		//go through each letter of the encrypted text
		for(int i=0; i<encryptedText.length(); i++) {
			decryptedText += decryptLetter(key.charAt(keyStringCounter), encryptedText.charAt(i));
			keyStringCounter++;
			if(keyStringCounter==key.length()) {
				keyStringCounter = 0;
			}
		}

		return decryptedText;
	}


	/**
	 * generate a random key. Can be used for OTPs
	 * @param keyLength
	 * @return
	 */
	public String generateRandomKey(int keyLength) {
		String result = "";
		Random rand = new Random();

		for(int i = 0; i<keyLength; i++) {
			result+=(char)(rand.nextInt(90-65 +1) +65);
		}

		return result;
	}


	/**
	 * print matrix for debugging purposes
	 */
	public void printTabulaRecta() {
		for(int i=0; i<26; i++) {
			for(int j=0; j<26; j++) {
				System.out.print(tabulaRecta[i][j]+" ");
			}
			System.out.println("");
		}
	}




	//takes a letter of the key, a letter of plaintext and converts the latter to encrypted text
	private char encryptLetter(char keyLetter, char plaintextLetter) {

		//if not alpha, return unchanged
		if(!Character.isAlphabetic(plaintextLetter)){
			return plaintextLetter;
		}

		//convert to upper case for case-insensitivity while looking up in the table
		keyLetter = Character.toUpperCase(keyLetter);
		char plaintextLetterToUpper = Character.toUpperCase(plaintextLetter);

		//to leave case unchanged 
		if(Character.isUpperCase(plaintextLetter)) {
			//choose keyLetter's alphabet, and get plaintextLetter's equivalent in it.
			return tabulaRecta[keyLetter-65][plaintextLetter-65];
		}

		return Character.toLowerCase(tabulaRecta[keyLetter-65][plaintextLetterToUpper-65]);
	}




	//decript letter
	private char decryptLetter(char keyLetter, char encryptedLetter) {

		//if not alpha, return unchanged
		if(!Character.isAlphabetic(encryptedLetter)){
			return encryptedLetter;
		}

		//convert keyLetter to uppercase 
		keyLetter = Character.toUpperCase(keyLetter);	
		char encryptedLetterToUpper = Character.toUpperCase(encryptedLetter);

		//search for encryptedLetter in keyLetter's row
		for(int i =0; i<26; i++) {
			if(tabulaRecta[keyLetter-65][i]==encryptedLetterToUpper) {
				if(Character.isUpperCase(encryptedLetter)) {
					return lettersArray[i];
				}else {
					return Character.toLowerCase(lettersArray[i]);
				}

			}
		}


		return 0;
	}









}

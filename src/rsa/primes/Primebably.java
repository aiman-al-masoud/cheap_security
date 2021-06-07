package rsa.primes;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * This class can find (probable) primes of arbitrary length.
 */

public class Primebably {
	
	/**
	 * A logger to display all of the attempts at finding primes.
	 */
	Logger logger;
	
	
	public Primebably() {
		logger = Logger.getLogger("");
	}
	
	
	/**
	 * Returns a prime of arbitrary length.
	 * @param numberOfDigitsInBaseTen
	 * @return
	 */
	public BigInteger getPrime(int numberOfDigitsInBaseTen) {
		
		
		//generate a random number-string
		String numberString  = "";
		for(int i =0; i<numberOfDigitsInBaseTen; i++) {
			numberString+=ThreadLocalRandom.current().nextInt(1, 9+1);
		}
		
		//convert the random number-string into a BigInteger
		BigInteger bigInt = new BigInteger(numberString);
		
		//While the random number is not a prime, check if the one right after it is.
		//cons: as the numbers grow, the distance between primes increases, so maybe 
		//incrementing by one might not be the best strategy, but it doesn't run the risk of skipping primes.
		while(!bigInt.isProbablePrime(1)) {
			bigInt = bigInt.add(new BigInteger("1"));
			//not a prime!
			//logger.info(bigInt+" definitely not a prime");
		}
		
		//succes! (Prime-bably)
		//logger.info(bigInt+" primebably!");
		return bigInt;
	}
	
	

}

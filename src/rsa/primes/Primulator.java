package rsa.primes;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 
 * This class finds primes deterministically.
 * But the algorithm used is sequential and painfully slow! 
 *
 *
 * (Primulator = Prime+Calculator)
 */

public class Primulator {

	/**
	 * List of all of the primes that where found.
	 */
	ArrayList<BigInteger> primes;

	public Primulator() {
		primes = new ArrayList<BigInteger>();
	}


	/**
	 * Computes, stores and returns the next prime to be obtained.
	 * @return
	 */
	public BigInteger getNext() {

		//get the last found
		BigInteger lastFound;
		if(primes.size()==0) {
			//initialize last found to 1 if "primes" is still empty
			lastFound = new BigInteger("1");
			primes.add(lastFound);
		}else {
			lastFound = primes.get(primes.size()-1);
		}


		BigInteger one = new BigInteger("1");
		//increment last found
		lastFound = lastFound.add(one);

		while(!isPrime(lastFound)) {
			lastFound = lastFound.add(one);
		}

		primes.add(lastFound);
		return lastFound;
	}


	/**
	 * Returns the nth prime.
	 * @param n
	 * @return
	 */
	public BigInteger getNth(int n) {
		while(primes.size()<n) {
			getNext();
		}

		return primes.get(primes.size()-1);
	}


	/**
	 * Returns the list of all of the primes found at a given point.
	 * @return
	 */
	public ArrayList<BigInteger> getAll() {
		return primes;
	}



	/**
	 * Check if a number is prime by going through the whole painful (naive) process.
	 * @param number
	 * @return
	 */

	public boolean isPrime(BigInteger number) {

		BigInteger one = new BigInteger("1");
		BigInteger counter = one.add(one); //two


		//while counter < number
		while(counter.compareTo(number) <0) {
			if(isDivisible(number, counter)) {
				return false;
			}
			//increment the counter
			counter = counter.add(one);
		}

		return true;
	}

	/**
	 * Check if a number is divisible by another.
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public boolean isDivisible(BigInteger dividend, BigInteger divisor) {

		BigInteger remainder = dividend.divideAndRemainder(divisor)[1];
		if(remainder.compareTo(new BigInteger("0"))==0) {
			return true;
		}

		return false;
	}














}

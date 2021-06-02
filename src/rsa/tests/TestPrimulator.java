package rsa.tests;

import java.math.BigInteger;
import rsa.primes.Primebably;

public class TestPrimulator {

	public static void main(String[] args) {
		
		
		Primebably primebably = new Primebably();
		
		BigInteger prime = primebably.getPrime(400);
		System.out.println(prime);
		
		//System.out.println(primulator.isPrime(new BigInteger("9")));
		
		
		
		
	}

}

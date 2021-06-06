package rsa.tests;

import rsa.RSA;

public class SerTester {

	public static void main(String[] args) {
		
		
		//rsa.save("ciao");
		
		RSA rsa = RSA.load("ciao.ser");
		
		System.out.println(rsa.getPublicKey()[0]+" "+rsa.getPublicKey()[1]);
		
		
	}

}

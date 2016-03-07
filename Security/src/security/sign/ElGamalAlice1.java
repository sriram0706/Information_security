package security.sign;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice1
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		BigInteger Y = g.modPow(d, p);  // Y = g^d mod p
		return Y;
	}

	private static BigInteger computeK(BigInteger p)
	{
		SecureRandom random = new SecureRandom();      // generates a new random number
		int numBits = 1024;                             // defines the length
		BigInteger K = new BigInteger(numBits, random); // This will generate a random number K
		BigInteger pMinusOne = p.subtract(BigInteger.ONE); // generate p-1
    
        
		while(!K.gcd(pMinusOne).equals(BigInteger.ONE)) // checks if K is relatively prime to (p-1)
		{
			K = new BigInteger(numBits, random);    // generates a random number K
		}
		
		return K;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		BigInteger A = g.modPow(k, p); // A = g^k mod p
		
		return A;
	}

	private static BigInteger computeB(String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		BigInteger n = new BigInteger(message.getBytes()); //convert string into bytes
		BigInteger pMinusOne = p.subtract(BigInteger.ONE); //generate p-1
		BigInteger q1 = pMinusOne;  // q1= p-1
		BigInteger y0 = BigInteger.ZERO;//0
		BigInteger y1 = BigInteger.ONE;//1
		BigInteger y2 = k;
		BigInteger z, z2, z3;
		
		while(!y2.equals(BigInteger.ZERO))
		{
			z = q1.divide(y2);//q1/y2
			z2 = q1.subtract(y2.multiply(z));//q1-y2*z
			q1 = y2;
			y2 = z2;
			z3 = y0.subtract(y1.multiply(z));//y0-y1*z
			y0 = y1;
			y1 = z3;
		}
		
		BigInteger b = y0.multiply(n.subtract(d.multiply(a))).mod(pMinusOne); //b = ((n-da)/k) mod (p-1)
		 
		return b;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}
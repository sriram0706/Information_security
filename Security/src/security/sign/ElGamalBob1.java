package security.sign;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob1
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		BigInteger r1 = (y.modPow(a, p).multiply(a.modPow(b, p))).mod(p); //((y^a mod p)(a^b mod p)) mod p
		
		BigInteger m = new BigInteger(message.getBytes());   // converts the string into bytes
		BigInteger r2 = g.modPow(m, p); //g^m mod p         //  computes the value of r2
		
		return r1.equals(r2);                               // if the values are same then return 
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;                                    // initialising the port
                ServerSocket s = new ServerSocket(port);            // open new socket for communication
                Socket client = s.accept();                         // accept the connection from the client
                ObjectInputStream is = new ObjectInputStream(client.getInputStream());
                
                // read public key
                BigInteger y = (BigInteger)is.readObject();        
                BigInteger g = (BigInteger)is.readObject();
                BigInteger p = (BigInteger)is.readObject();
                
                // read message
                String message = (String)is.readObject();
                
                // read signature
                BigInteger a = (BigInteger)is.readObject();
                BigInteger b = (BigInteger)is.readObject();
                
                boolean result = verifySignature(y, g, p, a, b, message);
                
                System.out.println(message);
                
                if (result == true)
                    System.out.println("Signature verified.");
                else
                    System.out.println("Signature verification failed.");
                
                s.close();
            }
	}

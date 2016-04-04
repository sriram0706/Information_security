package security.publickey;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class PublicKeyRSABob
{
	public static void main(String[] args) throws Exception
    {
	    int port = 7999;                                        // initialize any arbitary port
		ServerSocket s = new ServerSocket(port);            // create the new socket for communication with client
		Socket client = s.accept();                         //  accepting communication with the client
		

		ObjectInputStream AlicePublicKey = new ObjectInputStream(client.getInputStream()); // creats object to retrives and reads Alice's public key
		ObjectOutputStream PublicKey = new ObjectOutputStream(client.getOutputStream());
		
		/* This will generate Bob's keys (private key and public key)*/
		KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
	    genKeyPair.initialize(1024, new SecureRandom()); 
	    KeyPair keyPair = genKeyPair.genKeyPair();
	    RSAPublicKey enBob = (RSAPublicKey) keyPair.getPublic();                //generates Bob's public key
	    RSAPrivateKey dnBob = (RSAPrivateKey) keyPair.getPrivate();             // generates Bob's private key
	    
	    
	    PublicKey.writeObject(enBob);                                           // will send Bob's public key to Alice
	    
	    /* Get Alice's public key */
	    RSAPublicKey enAlice = (RSAPublicKey) AlicePublicKey.readObject();      // will retrive Alice's public key
	    Cipher cipher = Cipher.getInstance("RSA");                              // initialise RSA enciphering
	    
	    int choice = AlicePublicKey.readInt();
	    
		    switch(choice)
			{
			  case 1:
				  /*Achieve Confidentiality by using Bob's private key to decipher */ 
				  System.out.println("Confidentiality: encipher by receiver's public  key(Bob)");
				  System.out.println("                 decipher by receiver's private key(Bob)");
				  byte[] in1 = (byte[]) AlicePublicKey.readObject();                // initialises byte array for the key
				  cipher.init(Cipher.DECRYPT_MODE, dnBob);                          // deciphering using the private key of Bob
   			          byte[] plaintText1 = cipher.doFinal(in1);                         // initialise byte array for the plain text
				  System.out.println("The plaintext is: " + new String(plaintText1)); 
				  break;
				    
				  
			  case 2:
				  /*Achieve Integrity/Authentication by using Alice's public key to decipher */ 
				  System.out.println("Integrity/Authentication: encipher by sender's private key (Alice)");
				  System.out.println("                          decipher by sender's public key (Alice)");
				  byte[] in2 = (byte[]) AlicePublicKey.readObject();                // initialise byte array for alice's key
				  cipher.init(Cipher.DECRYPT_MODE, enAlice);                        // decipher using public key of alice
   			          byte[] plaintText2 = cipher.doFinal(in2);                         // initialise byte array for the plain text
				  System.out.println("The plaintext is: " + new String(plaintText2));
				  break;
              case 3:
                  /*Achieve both confidentiality and Integrity/Authentication by first decrypting using Bob's private key and then using Alice's public key. */
                  System.out.println("Combination of both: Enciphered by sender's public key (Alice) and then re-enciphered by receiver's public key (Bob)");
                  System.out.println(" Decipher first by receiver's private key (Bob) and then re-deciphering by sender's public key (Alice)");
                  BigInteger in3 = (BigInteger) AlicePublicKey.readObject();                                        // reades the public key of Alice
                  BigInteger plaintText3 = (in3.modPow(dnBob.getPrivateExponent(), dnBob.getModulus()));            // deciphers using private key of Bob first
                  BigInteger plaintText4 = (plaintText3.modPow(enAlice.getPublicExponent(), enAlice.getModulus())); // again deciphers using public key of Alice
              
                  System.out.println("The plaintext is: ");
                  System.out.println(new String(plaintText4.toByteArray()));
                  break;

			
			}
		 
		}
		
	}
 



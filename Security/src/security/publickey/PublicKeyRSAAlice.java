package security.publickey;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;


public class PublicKeyRSAAlice {
    
	
	public static void main(String[] args) throws Exception {
        

		System.out.println("Which kind of RSA service would you like ?");
		System.out.println("1. Confidentiality            : ");
	    System.out.println("2. Integrity & Authentication : ");
        System.out.println("3. Combination of both        : ");
	    
		Scanner input = new Scanner(System.in);             // Either of the three inputs are taken
		
	    String host = "localhost";                              // host for communication with the server is established
		int port = 7999;                                    // the port for communication is addressed
		Socket s = new Socket(host, port);                  // a neew socket is created between client and server
		
		ObjectOutputStream BobPublicKey = new ObjectOutputStream(s.getOutputStream());  // A new object is created as the Public key of Bob
		ObjectInputStream AlicePublicKey = new ObjectInputStream(s.getInputStream());   // A new object is created as the Public key of Alice
		
		/* This will generate Alice's keys (private key and public key)*/
		KeyPairGenerator generateKeyPair = KeyPairGenerator.getInstance("RSA");
		generateKeyPair.initialize(1024 ,new SecureRandom());
	        KeyPair keyPair = generateKeyPair.genKeyPair();
	        RSAPublicKey enAlice = (RSAPublicKey) keyPair.getPublic();     // Generate Alice's Public key
	        RSAPrivateKey dnAlice = (RSAPrivateKey) keyPair.getPrivate();  // Generate Alice's Private key
	    
	    
	    BobPublicKey.writeObject(enAlice);      // Will send Alice's Public Key to Bob
	    
	    
	    RSAPublicKey enBob = (RSAPublicKey) AlicePublicKey.readObject(); // Bob's public key is received
	    Cipher cipher = Cipher.getInstance("RSA");                       // initializing RSA algorithm
	    
	    int choice= input.nextInt();                   // the input is initialized         
        
        switch(choice)
        {
            case 1:                                       // The Enciphering for Confidentiality using receivers public key
                
                
                System.out.println("Confidentiality: encipher by receiver's(Bob) public key ");
                Scanner input1 = new Scanner(System.in);                                            // scans the input 
                System.out.println("Please enter the plaintext");                                   // user should enter the text to be enciphered
                String in1= input1.nextLine();
                cipher.init(Cipher.ENCRYPT_MODE, enBob);                                            // encryption is done using Publickey of Bob
                byte[] cipherText1 = cipher.doFinal(in1.getBytes());                                // initialise byte array for the ciphertext
                System.out.println("The ciphertext is: " + cipherText1);                            // display the ciphertext
                BobPublicKey.writeInt(1);                       
                BobPublicKey.writeObject(cipherText1);
                BobPublicKey.flush();
                BobPublicKey.close();
                break;
                
            case 2:
                
                                                    // Enciphering using sender's private key
                System.out.println("Integrity/Authentication: encipher by sender's(Alice) private key");
                Scanner input2 = new Scanner(System.in);                                        // scans the input text
                System.out.println("Please enter the plaintext");                               // user should enter the text to be encihpered
                String in2= input2.nextLine();
                cipher.init(Cipher.ENCRYPT_MODE, dnAlice);                                      // encryption is done using the private key of Alice
                byte[] cipherText2 = cipher.doFinal(in2.getBytes());                            // initialise byte array for the ciphertext
                System.out.println("The ciphertext is: " + cipherText2);                        // display the cihpertext
                BobPublicKey.writeInt(2);
                BobPublicKey.writeObject(cipherText2);
                BobPublicKey.flush();
                BobPublicKey.close();
                break;
                
            case 3:
                //Combination of both Confidentiality and Intergrity/Authentication: Encipher by sender's private key first and then by receiver's public key.
                System.out.println("Combination of both: Encipher by sender's private key first (Alice) and then re-enciphered by receiver's public key (Bob)");
                Scanner input3 = new Scanner(System.in);
                System.out.println("Please enter the plaintext");
                String in3 = input3.nextLine();
               
                BigInteger BI = new BigInteger(in3.getBytes());                                                     // initialise a BitInteger for the ciphertext
                BigInteger cipherText3 = (BI.modPow(dnAlice.getPrivateExponent(), dnAlice.getModulus()));           // encipher using sender's private key
                BigInteger cipherText4 = (cipherText3.modPow(enBob.getPublicExponent(), enBob.getModulus()));       // again enchpher using receiver's public key
                System.out.println("The ciphertext is: ");                                                          // display the output
                System.out.println(new String(cipherText4.toByteArray()));                                          // conversion of cipher text to byte
                BobPublicKey.writeInt(3);
                BobPublicKey.writeObject(cipherText4);
                BobPublicKey.flush();
                BobPublicKey.close();
                break;
                
        }
        s.close();
        input.close();
    }
    
    
    
}
package security.certificate;

import java.io.*;
//import java.math.BigInteger;
import java.net.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.*;

public class X509client1
{
	public static void main(String[] args) throws Exception 
	{
		String host = "localhost";                      // initialising the host
		int port = 7999;                                // initialising the port number to communicate
		Socket s = new Socket(host, port);              // creating the new socket with the host name and port
	    
		
             ObjectOutputStream op = new ObjectOutputStream(s.getOutputStream()); // create a new object to read the certificate
             InputStream in = new FileInputStream("Ani.cer");                    // reads the certificate file 
             CertificateFactory cf = CertificateFactory.getInstance("X.509");   // instantiate a X.509 certificate
             X509Certificate  cert = (X509Certificate)cf.generateCertificate(in);
             in.close();  
            
        
        
        System.out.println("The detail of the certificate file is:");   // prints the contents of the certificate
        System.out.println(cert.toString());

        Date date = cert.getNotAfter();                                 // computes the expiry of the certificate
        if(date.after(new Date()))
        {
        	System.out.println("The certificate is current.");      //will check the Expiration date of the certificate
        	System.out.println("It is valid from "+ cert.getNotBefore()+ " to "+cert.getNotAfter()) ;
        }
        else
        {     
        	System.out.println("The certificate is expired.");
        }
   
        try
        {
    	   cert.checkValidity(); //This will check server's signature
    	   System.out.println("The certificate is valid.");
        } 
        catch (Exception e)
        {
    	   System.out.println(e);   
        }
        
        System.out.println("Please input a string:");
        Scanner input = new Scanner(System.in);
        String message= input.nextLine();
        
        
        RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();  //it will retrieve the RSA public key from the certificate
        
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // initialise the encryption of server's public key
        cipher.init(Cipher.ENCRYPT_MODE, eServer);                  // encrypt the server's public key 
        byte[] cipherText = cipher.doFinal(message.getBytes());     // converts the ciphettext into byte
        System.out.println("The ciphertext is: " + cipherText);     // prints the ciphertext
        op.writeObject(cipherText);
		op.flush();
		
	}
}





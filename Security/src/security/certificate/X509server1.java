package security.certificate;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class X509server1
{
	public static void main(String[] args) throws Exception 
	{   
		
		String aliasname="anirudh";                               // inputing the alias name which is same in the certificate
                char[] password="anirudh".toCharArray();                  // creating the same password as the certificate 
		
                int port = 7999;                                            // initialise port for communication
                ServerSocket server = new ServerSocket(port);               // open a new socket for communication
                Socket s = server.accept();                                 // accept the request for communication form the client
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());   // retrives the information passed in the socket from the client
                
                
                KeyStore ks = KeyStore.getInstance("jks");          // reads the keystore
                ks.load(new FileInputStream("Anirudh.jks"), password);  
                PrivateKey dServer = (PrivateKey)ks.getKey(aliasname, password);    // retriving the server's private key
                
                
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // initialising a decryption
                byte[] i = (byte[]) is.readObject();                    
                cipher.init(Cipher.DECRYPT_MODE, dServer);                  // decrypting the server's private key
                byte[] plaintText = cipher.doFinal(i);                      // retriving the plain text from bytes
                System.out.println("The plaintext is: " + new String(plaintText));  // printing the plain text
                
                s.close();
            }
	}



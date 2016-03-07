package security.auth;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient1
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream); // this will put data into buffer

                Date date = new Date();
                long time1= date.getTime();  //generates time stamp
                double rn1= Math.random();   // generates a random number
                byte[] value1 = Protection1.makeDigest( user, password, time1, rn1); // calls the protection class
                
                long time2= date.getTime();  // another time stamp
                double rn2= Math.random();  // generate another random number
                byte[] value2 = Protection1.makeDigest( value1, time2, rn2); // calls protection class to create hash value
                
                out.writeUTF(user);             // writes and sends the user name to the client
                out.writeLong(time1);           // writes and sends the first timestamp1
                out.writeDouble(rn1);           // writes and sends the random number generated 
                out.writeLong(time2);           // writes and sends the timestamp2
                out.writeDouble(rn2);           // writes and send the random number2
                out.writeInt(value1.length);    // writes a integer value with specific length
                out.write(value2);              // writes the value 2 and sends to th client
                        
                        
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "localhost";              // initialise connection by logging into a host address 
		int port = 7999;                        // initialise the port for communication    
		String user = "George";                 // the given user name
		String password = "abc123";             // the password
		Socket s = new Socket(host, port);      // creates new socket for connection

		ProtectedClient1 client = new ProtectedClient1();
		client.sendAuthentication(user, password, s.getOutputStream()); 

		s.close();
	}
}
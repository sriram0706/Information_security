package security.auth;

import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer1
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream); // gets the data from the client
                
                String user= in.readUTF();                          // reads the username from the client
                String password= lookupPassword(user);              // checks the password of the user
                
                long time1= in.readLong();                          // reads the timestamp1
                double rn1 = in.readDouble();                       // reads the random number1
                long time2= in.readLong();                          // reads the timestamp2
                double rn2 = in.readDouble();                       // reads the random number2
                
                
                
                int length= in.readInt();                           
                byte[] obtainedValue = new byte[length];            // creates a new byte array
                
                
                in.readFully(obtainedValue);                    // reads the above byte
                
               byte [] value1= Protection1.makeDigest(user, password, time1, rn1);  // generates a SHA digest
               byte [] value2= Protection1.makeDigest(value1, time2, rn2);          // generate the SHA digest using value1
               
                
                boolean flag = true;   
                 flag= MessageDigest.isEqual(obtainedValue, value2);                
                 return flag;
                
	}

	protected String lookupPassword(String user)
        { 
            return "abc123";
        }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;                                                // initialise port
                ServerSocket s = new ServerSocket(port);                        // create new socket for communication
                Socket client = s.accept();                                     // accepts the client's request to communicate
                
                ProtectedServer1 server = new ProtectedServer1();
                
                if (server.authenticate(client.getInputStream()))
                    System.out.println("Client logged in.");
                else
                    System.out.println("Client failed to log in.");
                
                s.close();
            }
	}

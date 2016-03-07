
package security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;

public class NewMain {

    
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        
    try {
        
        int success = 0;
        BufferedReader reader = new BufferedReader(new FileReader("/users/sri/desktop/filename.txt"));
                   String line = null;
                   Scanner in = new Scanner(System.in);

                String s,a;
                System.out.println("Enter the username, whose password you want to crack");
                s = in.nextLine();
        while ((line = reader.readLine()) != null) {
           
                BufferedReader read = new BufferedReader(new FileReader("/users/sri/desktop/dictionary.txt"));
                String lin = null;
                while ((lin = read.readLine()) != null) {
                
                a = lin;
                String cat = null;
            
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(lin.getBytes());
                byte byteData[] = md.digest();
                //convert the byte to hex format 
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                cat = s + "\t" + sb.toString();
                
                }
                
                if ((cat).equals(line)) { 
                    System.out.println("Password Cracked");
                    System.out.println("Your password is \t" + lin);
                    success = 1;
                    break;
                } else if (cat == line) {                  
                    success = 0;
                }                               
                }                             
    }   
        if (success != 1) {
        System.out.println("Password cannot be cracked using dictionary attack");
    }
    	reader.close();
        //r.close();
    }
     catch (IOException e) {
	    e.printStackTrace();
}
}
    }



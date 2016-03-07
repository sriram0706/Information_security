
package security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

public class part2 {
   
    public static long s = System.currentTimeMillis();
    
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
           
        BufferedReader reader = new BufferedReader(new FileReader("/users/sri/desktop/dict.txt"));
                   String line = null;
                   Scanner in = new Scanner(System.in);
                String s1;
                System.out.println("Enter the username, whose password you want to crack");
                s1 = in.nextLine();
                
        while ((line = reader.readLine()) != null) {            
            int q;
            q = line.length(); 
            
            int combinations = 1 << line.length();   
            for (int i=0; i<combinations; i++) {
            StringBuilder buf = new StringBuilder(line);
            for (int j=0; j<line.length(); j++) {
            if ((i & 1<<j) != 0) {
                String s = line.substring(j, j+1).toUpperCase();
                buf.replace(j, j+1, s);
               }
            }
          
            String z = buf.toString();        
              int b=0,r=0;
             b = 7 - q;
             int h=0;
          for(h=0;h<=b;h++){
              r=h;
            // Create an alphabet to work with
             char[] alphabet = new char[] {'#','1'};
             
             char[] charArray = z.toCharArray();       
           
          
              possibleStrings(r, alphabet,"",z,s1);

             MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(buf.toString().getBytes());
                byte byteData[] = md.digest();
                //convert the byte to hex format 
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < byteData.length; k++) {
                sb.append(Integer.toString((byteData[k] & 0xff) + 0x100, 16).substring(1));
            }
          }
        }       
        } 
        }
    
    
        public static void possibleStrings(int maxLength, char[] alphabet, String curr, String z, String s1 ) throws IOException, FileNotFoundException, NoSuchAlgorithmException {

                               
                 if(curr.length() == maxLength) {                 
                 String d = curr + z;                
                 permutation("", d,s1);                   
                  
                    } else {
                for(int i = 0; i < alphabet.length; i++) {
                
                String oldCurr = curr;                
                curr += alphabet[i];
                possibleStrings(maxLength,alphabet,curr,z,s1);
                curr = oldCurr;
            } 
        }                                      
    } 

   
    private static void permutation(String prefix, String d, String s1) throws IOException, FileNotFoundException, NoSuchAlgorithmException {
    int n = d.length();
    if (n == 0) comparison(prefix,s1);
    else {
        for (int u = 0; u < n; u++)
            permutation(prefix + d.charAt(u), d.substring(0, u) + d.substring(u+1, n),s1);
    }
    
}

    private static void comparison(String prefix, String s1) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
         BufferedReader read = new BufferedReader(new FileReader("/users/sri/desktop/filename.txt"));
                   String lin = null;
                    while ((lin = read.readLine()) != null) {
                 
                
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(prefix.toString().getBytes());
                byte byteData[] = md.digest();
                //convert the byte to hex format 
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < byteData.length; k++) {
                sb.append(Integer.toString((byteData[k] & 0xff) + 0x100, 16).substring(1));
                
                    }
                String cat = null;
                
                     cat = s1 + '\t' + sb.toString(); 
                    
                int success = 0;
                 if (cat.equals(lin)) { 
                    System.out.println("Password Cracked");
                    System.out.println("Your password is \t" + prefix);
                    success = 1;
                    long e = System.currentTimeMillis();
                    long difference = (e - s);
                    System.out.println("Time in milliseconds\t" + difference);
                    System.exit(0);
 
                 } else if (cat == lin) {
                   
                    success = 0;
    }                                    
}
    }  
    }
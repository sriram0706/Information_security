
package security;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class New {
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
         
        try {   
                System.out.println("Are you a new user? Say 'yes' or 'no'");
                Scanner in = new Scanner(System.in);
                //int c= 2;
                String s,a,b;
                
                b = in.nextLine();
                
                switch (b.toLowerCase()) {
                    
                    case "yes":   
                
                BufferedWriter output;
                output = new BufferedWriter(new FileWriter("/users/sri/desktop/filename.txt",true));
                               
                System.out.println("Enter the username");
                s = in.nextLine();
                System.out.println("Enter the password");
                a = in.nextLine();
                System.out.println("New user accepted...");
                
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(a.getBytes());
                byte byteData[] = md.digest();
                //convert the byte to hex format 
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
                
                output.append(s);
                output.append("\t");
                output.append(sb.toString());
                output.newLine();
                output.close();
                break;
                    case "no":
                        String s1,a2,cat;
                        System.out.println("Enter the username");
                        s1 = in.nextLine();
                        System.out.println("Enter the password");
                        a2 = in.nextLine();
                        System.out.println("Authenticating....");
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        md5.update(a2.getBytes());
                        byte byteData1[] = md5.digest();
                        //convert the byte to hex format 
                        StringBuffer sb1 = new StringBuffer();
                        for (int i = 0; i < byteData1.length; i++) {
                        sb1.append(Integer.toString((byteData1[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        cat = s1 + "\t" + sb1.toString();
                       
                        BufferedReader reader = new BufferedReader(new FileReader("/users/sri/desktop/filename.txt"));
                        String line = null;
       
                        int success = 0;
        
                        while ((line = reader.readLine()) != null) {
                        
                            if ((line).equals(cat)){
                                System.out.println("User Authenticated");
                                success = 1;
                                break;
                        } else if(line != cat) {
                                success = 0;
                                
                            }
                       
        }          if (success!=1) {
                        System.out.println("User not found");
        }
                }
                }
		 catch (IOException e) {
			e.printStackTrace();
		}
        
        
        
    }
        
        
    }
    
}

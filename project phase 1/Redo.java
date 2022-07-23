/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Redo {
    public static void main(String[] args) {
        try
        {
        Scanner sc=new Scanner(new FileInputStream("F:\\fyp\\Final Year Proj\\Dataset\\Trace.txt"));
        FileOutputStream fs=new FileOutputStream(new File("F:\\fyp\\Final Year Proj\\Dataset\\Trace1.txt"));
        
        while(sc.hasNext())
        {
            for(int i=0;i<18;i++)
            {
             String a=sc.next();
             fs.write(a.getBytes());
             String s="\t";
             fs.write(s.getBytes());
            }
            String s="\r\n";
            fs.write(s.getBytes());
        }
        fs.close();
        
    }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

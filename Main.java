/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author rakib3004
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
       System.out.println("Control Flow Graph\n\n");
       System.out.println("Enter the directory of a C source code file\n");
       String fileName;                         // F:\\Downloads\\CFG-master\\test.txt
       Scanner input = new Scanner(System.in);


        fileName = "/home/rakib3004/Documents/SoftwareTesting/TestCaseGenerator/test.txt";
       SourceFileReader Sc;
       Sc = new SourceFileReader(fileName);
    }  
}

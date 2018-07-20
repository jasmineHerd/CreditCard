/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author jasmineherd
 */
public class Card {
    private double climit,baldue;
    private long acctno;
    private String errmsg;
    
    public Card(){
        this.climit =0;//credit limit
        this.baldue = 0;       
        this.acctno = 0;
        this.errmsg = "";
        
        while(this.acctno == 0){
            try{
                this.acctno = (long)(Math.random()* 1000000);
                BufferedReader in = new BufferedReader(
                                    new FileReader("CC" + this.acctno + ".txt"));
                //success is bad outcome: File already exists
                in.close();
                this.acctno = 0;
            }catch(FileNotFoundException e ){
                //success!(acctno  ok)
                this.climit = 1000;
                writestatus();
                
            }catch(IOException e){
                
                this.acctno = 0;
                        
            }
        }       
    }//end of constructor
    private boolean writestatus(){
        try{
            PrintWriter out = new PrintWriter(
            new FileWriter("CC"+ this.acctno + ".txt"));
            out.println(this.climit);
            out.print(this.baldue);
            out.close();
            return true;
        }catch(IOException e){
            this.errmsg = "Unable to write CC file for: " + this.acctno;
            return false;
            
        }
    }
    

    
    
    
    
    
    
    
    
    
}

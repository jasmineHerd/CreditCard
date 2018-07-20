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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 *
 * @author jasmineherd
 */
public class Card {
    private double climit,baldue;
    private long acctno;
    private String errmsg,actionmsg;
    private NumberFormat curr = NumberFormat.getCurrencyInstance();
    
    
    public Card(){
        this.climit =0;//credit limit
        this.baldue = 0;       
        this.acctno = 0;
        this.errmsg = "";
        this.actionmsg = "";
        
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
                if(writestatus()){
                    this.actionmsg = "Account "+ this.acctno + " opened.";
                    writelog(this.actionmsg);                  
                }else{
                    this.acctno = 0;
                    this.climit = 0;
                }
                
            }catch(IOException e){
                
                this.acctno = 0;
                        
            }
        }       
    }//end of constructor
    public Card(long a){
        this.errmsg="";
        this.actionmsg = "";
        this.climit = 0;
        this.baldue =0;
        this.acctno = a;
        try{
            BufferedReader in =  new BufferedReader(
                new FileReader("cc"+this.acctno + ".txt"));
            this.climit = Double.parseDouble(in.readLine());
            this.baldue = Double.parseDouble(in.readLine());
            in.close();
            this.actionmsg = "Account "+ this.acctno + "re-opened.";
          
        }catch(Exception e){
            this.errmsg = "Unable to re-open account #: "+ a + e.getMessage();
            this.acctno = 0;
            this.climit = 0;
            this.baldue = 0;
        }
    }
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
    }//ends writestatus()
    
    private void writelog(String msg){
        try{
            Calendar cal = Calendar.getInstance();
            DateFormat df = DateFormat.getDateTimeInstance();
            String ts = df.format(cal.getTime());
            PrintWriter out = new PrintWriter(
                              new FileWriter("CCL"+this.acctno+".txt",true));//apends
            out.println(ts + ": " + msg);
            out.close();
        }catch(IOException e){
            this.errmsg  = "Unable to update log for: "+ this.acctno;
        }
    }
    
    
    
    public String getErrorMsg(){
        return this.errmsg;
    }
    public String getActionMsg(){
        return this.actionmsg;
    }
    public long getAcctNo(){
        return this.acctno;
    }
    public double getCreditLimit(){
        return this.climit;
    }
    public double getBalDue(){
        return this.baldue;
    }
    public double getCrAvail(){
        return(this.climit - this.baldue);
    }
    public void setCharge(double amt,String desc){
        this.errmsg = "";
        this.actionmsg = "";
        
        if(this.acctno <= 0){
            this.errmsg = "Charge attempted on unknown account";
            return;                    
        }
        if(amt <= 0){
            this.actionmsg = "Charge declined: amount must be positive.";
            writelog(this.actionmsg);
        }else if(desc.isEmpty()){
            this.actionmsg ="Charge declind: must have a description";
            writelog(this.actionmsg);
        }else if( (this.baldue + amt) > this.climit){
            this.actionmsg = "Charge declined: over credit limit";
            writelog(this.actionmsg);
        }else{
            this.baldue += amt;
            if(writestatus()){
                this.actionmsg = "Charge of "+ curr.format(amt) + " for "+ desc 
                        + " posted";
            writelog(this.actionmsg);
            }else{
                this.baldue -= amt; //failed status update 
            }
            
        }
    }
    
    
    
    
    
    
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mywsdl;


public class TestCases {
public static void main(String args[]) throws Throwable{

    boolean valid;
    Main mail=new Main("ValidateEmail.asmx");
    mail.parse();
    Many.urlForming(mail);
    valid = Many.createOutput(mail);
    if(valid)
    {
    Main phone=new Main("phone3t.asmx");
    phone.parse();
    Many.urlForming(phone);
    valid = Many.createOutput(phone);
    }
    else
    {
    Main phone1=new Main("phone3t.asmx");
    phone1.parse();
    Many.urlForming(phone1);
    valid = Many.createOutput(phone1);
    if(valid){
    Main phone=new Main("phone3t.asmx");
    phone.parse();
    Many.urlForming(phone);
    valid = Many.createOutput(phone); }
    else
    {
    System.out.println("Invalid input");
    System.exit(0);
    }
    }
    if(valid){
    Main city=new Main("globalweather.asmx");
    city.parse();
    Many.urlForming(city);
    valid = Many.createOutput(city);
    }
    else
    {
    System.out.println("Invalid input");
    System.exit(0);
    }
}
}

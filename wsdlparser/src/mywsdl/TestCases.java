package mywsdl;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class TestCases {
    public static int count=0;
public static void main(String args[]) throws Throwable{
    boolean valid;
    Main mail=new Main("ValidateEmail.asmx");
    mail.parse();
    Many.urlForming(mail);
    valid = Many.createOutput(mail);
    if(valid)
    {
    count++;
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
    count++;
    Main phone=new Main("phone3t.asmx");
    phone.parse();
    Many.urlForming(phone);
    valid = Many.createOutput(phone); }
    else
        Error();
    }
    if(valid){
    count++;
    Main city=new Main("globalweather.asmx");
    city.parse();
    Many.urlForming(city);
    valid = Many.createOutput(city);
    }
    else
        Error();
    if(valid){
     count++;
     int file=Many.filecount-1;
     File xml=new File("2.xml");
     FileInputStream fin=new FileInputStream(xml);
     BufferedReader br=new BufferedReader(new InputStreamReader(fin));
     BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
     String str,token;
     boolean flag=false;
     System.out.println("Enter city name : ");
     String str1 = br1.readLine();
     while((str=br.readLine())!=null)
	{
        StringTokenizer s=new StringTokenizer(str," < > &lt; &gt;",false);
		while(s.hasMoreElements())
		{
			token=(String)s.nextElement();
			token.trim();
			if(token.equals(str1)){
                flag=true;
                break;
            }
         }
        if(flag==true){
            System.out.println("City exists...");
            break;
            }
		}
     if(flag==false)
        Error();
    Main weather=new Main("globalweather.asmx");
    weather.parse();
    Many.urlForming(weather);
    valid = Many.createOutput(weather);
    }   
    else
        Error();
    if(valid){
        count++;
        System.out.println("Successfully completed...");
        System.out.println("Sequence count is "+ count);
    }
    else
        Error();
}
public static void Error(){
    System.out.println("Invalid input");
    System.out.println("Sequence count is "+ count);
    System.exit(0);
}
}

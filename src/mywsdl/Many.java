package mywsdl;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class Many {

    public static int filecount=0;

    public static String urlForming(Main phone)throws Throwable{
         BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
     String token=phone.element;

     StringTokenizer splitter=new StringTokenizer(token,",",false);

    String array[]=new String[splitter.countTokens()];
    String values[]=new String[splitter.countTokens()];
    int i=0;
    String tempurl=phone.url+"/"+phone.operation+"?";
    while(splitter.hasMoreTokens()){
     array[i]=splitter.nextToken();
     System.out.println(" Enter value for "+array[i]);
     values[i]=br.readLine();
    if(i==0){
          tempurl+=array[i]+"="+values[i];
    }
   else{
         tempurl+="&"+array[i]+"="+values[i];
    }
    i++;
    }
    System.out.println(tempurl);
    URL url=new URL(tempurl);
    URLConnection con=url.openConnection();
    File xml=new File(filecount+".xml");
    FileOutputStream fout=new FileOutputStream(xml);
    InputStream in=con.getInputStream();
    int c=0;
    while((c=in.read())!=-1){
       fout.write((char)c);
    }
    return tempurl;
}
public static boolean createOutput(Main service) throws SAXException, IOException{
        try {
            String[] primType = {"int", "string", "double", "float", "boolean"};
            String[] invalid = {"not", "invalid", "false", "error","NewDataSet/"};
            File file = new File(filecount + ".xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            String value="",prim="";
            String node=doc.getDocumentElement().getNodeName();
           for(int j=0;j<primType.length;j++)
           {
               if(primType[j].compareToIgnoreCase(node)==0)
               {
                   prim=primType[j];
                   break;
               }
           }
           if(prim.equals("")) prim="Valid";
            NodeList nd=doc.getElementsByTagName(prim);
            Element ele = (Element) nd.item(0);
            NodeList ndl = ele.getChildNodes();
            value = ((Node) ndl.item(0)).getNodeValue();
        //    System.out.println("node value is "+value);
            boolean nodata = false;

            for (String test : invalid) {
                if (value.toLowerCase().contains(test) ) {
                    nodata = true;
                    return false;
                }
               }
            
             File xml=new File(filecount+".xml");
             FileOutputStream fout=new FileOutputStream("out.xml",true);
             FileInputStream fin=new FileInputStream(xml);
             int c=0;
             while((c=fin.read())!=-1){
                fout.write((char)c);
             }
             filecount++;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Many.class.getName()).log(Level.SEVERE, null, ex);
        }
 return true;
}

}

package mywsdl;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Main {

    public String fname="";
    public String element="";
    public String operation="";
    public String url ="";
    String opTag = "";
    String prevOp = "";
    Document document=null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Main(String n){
      fname=n;
     //   factory.setNamespaceAware(true);
      try{
       DocumentBuilder builder = factory.newDocumentBuilder();
      if(fname.substring(fname.lastIndexOf('.')+1).equals("xml"))
       document = builder.parse(fname);
      else
       document = builder.parse(fname+".xml");
      }
      catch(Exception e){}
    }
public void getURL()
{
   NodeList urlnode =document.getElementsByTagName("http:address");
   for (int i = 0; i < urlnode.getLength(); i++)
    {
      Element uNode = (Element) urlnode.item(i);

      Element uParent = (Element) uNode.getParentNode();
      if ( uParent.getTagName().equals("wsdl:port") )
      {
        url = uNode.getAttribute("location");
  //      System.out.println("URL is " + url);
      }
      if(url!=null)break;
   }
}
public void getOperation() throws Exception
{
    NodeList opNodes = document.getElementsByTagName("wsdl:operation");

    for (int i = 0; i < opNodes.getLength(); i++)
    {
      Element opNode = (Element) opNodes.item(i);
      Element opParent = (Element) opNode.getParentNode();
      if ( opParent.getTagName().equals("wsdl:portType") )
      {
        opTag = opNode.getAttribute("name");
  //      System.out.println("Operation Name is " + opTag);
        if(opTag.equals(prevOp))
          continue;
        else
          operation=operation+","+opTag;
      }
         prevOp = opTag;
    }
       if(!(operation.equals(","+opTag)))
       {
         StringTokenizer splitter=new StringTokenizer(operation,",",false);
         BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
         int i=0;
         System.out.println("Available operations are : ");
         while(splitter.hasMoreTokens() && i<=splitter.countTokens()/3){
            System.out.println(splitter.nextToken());
            i++;
          }
         System.out.println("Enter operation to perform : ");
         operation=br.readLine();
       }
       else
           operation =opTag;    
}
public void parse() throws Exception
{
    getURL();
    getOperation();
  try
  {
    String msgType=null;
    String typeTag=null;
    String eleType=null;
   String eleNameList [] = new String[10];
   String eleTypeList [] = new String[10];
   String eleTypeList1 [] = new String[10];
   String primType[] = {"int","string","double","float","boolean"};
   String value[] = new String[10];
   int numParams =0;
   String preTypes = null;

   NodeList defs = document.getElementsByTagName("wsdl:definitions");
   Element def = (Element)defs.item(0);
   NamedNodeMap attrs = def.getAttributes();
   for(int i =0; i < attrs.getLength(); i++)
   {
       String str = attrs.item(i).toString();
  //    System.out.println(attrs.item(i).toString());
       if(str.contains("http://www.w3.org/2001/XMLSchema"))
       {
        int index1 = str.indexOf(':');
        int index2 = str.indexOf('=');
        preTypes = str.substring(index1+1,index2);
      // System.out.println(preTypes);
        break;
       }
       else
           preTypes="s";
   }  
   NodeList opNodes = document.getElementsByTagName("wsdl:operation");
   Element opNode=null;
    for (int i = 0; i < opNodes.getLength(); i++)
    {
    opNode = (Element) opNodes.item(i);
  //  System.out.println("Operation node is " + opNode.getAttribute("name"));
    if(opNode.getAttribute("name").equals(operation))
     break;
    }

// Operation Child Nodes

    NodeList inMsgNode = opNode.getElementsByTagName("wsdl:input");
	Element inMsg = (Element) inMsgNode.item(0);
	String inAtt = inMsg.getAttribute("message");
	String inStr[] = inAtt.split("[:]");
	String inMsgAtt = inStr[1];
   //    System.out.println("\nInput Message is " + inMsgAtt);

/* not needed as it is for return type */
/*
        NodeList outMsgNode = opNode.getElementsByTagName("wsdl:output");
        	Element outMsg = (Element) outMsgNode.item(0);
	String outAtt = outMsg.getAttribute("message");
       	String outStr[] = outAtt.split("[:]");
	String outMsgAtt = outStr[1];
        System.out.println("\nAttribute value is " + outMsgAtt);
*/
	NodeList msgList = document.getElementsByTagName("wsdl:message");
	for (int j = 0; j < msgList.getLength(); j++)
        {
          Element msgNode = (Element) msgList.item(j);
	      String msgName = msgNode.getAttribute("name");
	   if(msgName.equals(inMsgAtt))
	   {
	     NodeList msgChldList = msgNode.getElementsByTagName("wsdl:part");
	     Element msgPart = (Element) msgChldList.item(0);
	     String msgTypeStr[] = msgPart.getAttribute("element").split("[:]");
	     msgType = msgTypeStr[1];
	//    System.out.println("\nInput Msg Type is " + msgType);
           } // end if
	} // end for j

// dataTypes

   NodeList typeNodes = document.getElementsByTagName(preTypes+":element");

	for (int j = 0; j < typeNodes.getLength(); j++)
        {
	  Element typeNode = (Element) typeNodes.item(j);
          Element typeParent = (Element) typeNode.getParentNode();
	  if ( typeParent.getTagName().equals(preTypes+":schema") )
	  {
	    typeTag = typeNode.getAttribute("name");
	    if(msgType.equals(typeTag))
	    {
	      NodeList eleTypeNodes = typeNode.getElementsByTagName(preTypes+":element");
	 //     System.out.println(" eleTypeNodes length is " + eleTypeNodes.getLength());
              numParams = eleTypeNodes.getLength();
	      for(int k=0; k < eleTypeNodes.getLength(); k++ )
       	      {
                value[k] = "";
	        Element eleTypeItem = (Element) eleTypeNodes.item(k);
	        Element eleTypeParent = (Element) eleTypeItem.getParentNode();
	        if (eleTypeParent.getTagName().equals(preTypes+":sequence"))
	        {
	          String eleName = eleTypeItem.getAttribute("name");
	          eleNameList[k] = eleName;
              element+=","+eleName;
	       //   System.out.println("Element Name is " + eleNameList[k]);
	          String eleTypeAtt = eleTypeItem.getAttribute("type");
	          String eleTypeStr[] = eleTypeAtt.split("[:]");
	          eleType = eleTypeStr[1];
             }  // end if
	        } // end for k
           }  // end if
     	  } // end if parent
       } // end for j
 } // end try block

  catch(Exception e) { System.out.println("Hello 2"); e.printStackTrace(); }
}  // parse() ends

}  // end class


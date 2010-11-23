
package wsdlparser;
import java.io.*;
//import java.util.*;
import org.w3c.dom.*;
//import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class Main {
Main(String n){
    fname=n;
}
Main(){

}
    public String fname="";
    public String element="";
    public String operation="";
    public String url ="";
/*
public Node createEventNode(Document document , String oname, int numParams, String pname[], String ptype[], String val[])
{
  Element opNode = document.createElement ("opName" );
  Attr opName = document.createAttribute ( "name" );
  opName.setValue (oname);
  opNode.setAttributeNode (opName );
  Attr pName=null, pType=null, pCustom=null;

  //for(int k=0; k<numParams; k++)
    //System.out.println(val[k]);

  for(int k=0; k<numParams; k++)
  {
  Element paramElement=document.createElement("parameters");
  opNode.appendChild(paramElement);

  pName = document.createAttribute ( "name" );
  pName.setValue (pname[k]);
  paramElement.setAttributeNode (pName );

  pType = document.createAttribute ( "type" );
  pType.setValue(ptype[k]);
  paramElement.setAttributeNode (pType );

  if( val[k]!= null && val[k].length() > 0)
   {
      pCustom = document.createAttribute ( "userDefined" );
      pCustom.setValue(val[k]);
      paramElement.setAttributeNode (pCustom );
   }
  }
  return opNode;
}
*/
public void parse()
{
 Main m = new Main();
  try
  {
   int opCounter = 0;

/*** Wrting in XML file  ***/
/*      DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder1 = factory1.newDocumentBuilder();
      Document document1 = builder1.newDocument();
      Element root = document1.createElement ("opList");
      document1.appendChild(root);  */
/***  Writing in XML file ends ***/
    Document document=null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    if(fname.substring(fname.lastIndexOf('.')+1).equals("xml")){
  document = builder.parse(fname);
    }
    else
    {
  document = builder.parse(fname+".xml");
    }
    String opTag = null;
    String msgType=null;
    String typeTag=null;
    String eleType=null;

   String eleNameList [] = new String[10];
   String eleTypeList [] = new String[10];
   String eleTypeList1 [] = new String[10];
   String primType[] = {"int","string","double","float","boolean"};
   String value[] = new String[10];
   boolean flagS = false, flagC = false;
   int numParams =0;
   String prevOp = null;
   String preTypes = null;

   NodeList defs = document.getElementsByTagName("wsdl:definitions");
   Element def = (Element)defs.item(0);
   NamedNodeMap attrs = def.getAttributes();
   for(int i =0; i < attrs.getLength(); i++)
   {
       String str = attrs.item(i).toString();
      System.out.println(attrs.item(i).toString());
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


   // NodeList urlnode = document.getElementsByTagName("wsdl:service");
    NodeList urlnode =document.getElementsByTagName("http:address");
   for (int i = 0; i < urlnode.getLength(); i++)
    {
      Element uNode = (Element) urlnode.item(i);

      Element uParent = (Element) uNode.getParentNode();
      if ( uParent.getTagName().equals("wsdl:port") )
      {
        url = uNode.getAttribute("location");
        System.out.println("URL is " + url);
      }
      if(url!=null)break;
   }
   NodeList opNodes = document.getElementsByTagName("wsdl:operation");

    for (int i = 0; i < opNodes.getLength(); i++)
    {
      Element opNode = (Element) opNodes.item(i);

      Element opParent = (Element) opNode.getParentNode();
      if ( opParent.getTagName().equals("wsdl:portType") )
      {
        opCounter++;
        opTag = opNode.getAttribute("name");
        operation=opTag;
        System.out.println("Operation Name is " + opTag);
        if(opTag.equals(prevOp))
          continue;
// Operation Child Nodes

        NodeList inMsgNode = opNode.getElementsByTagName("wsdl:input");
	Element inMsg = (Element) inMsgNode.item(0);
	String inAtt = inMsg.getAttribute("message");
	String inStr[] = inAtt.split("[:]");
	String inMsgAtt = inStr[1];
       System.out.println("\nInput Message is " + inMsgAtt);

/* not needed as it is for return type */

        NodeList outMsgNode = opNode.getElementsByTagName("wsdl:output");
        	Element outMsg = (Element) outMsgNode.item(0);
	String outAtt = outMsg.getAttribute("message");
       	String outStr[] = outAtt.split("[:]");
	String outMsgAtt = outStr[1];
        System.out.println("\nAttribute value is " + outMsgAtt);
//*/

// Message Nodes
    // if(inMsgAtt.contains("Soap")) // this if is needed for asmx file
    // {
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
	    System.out.println("\nInput Msg Type is " + msgType);
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
	      System.out.println(" eleTypeNodes length is " + eleTypeNodes.getLength());
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
	          System.out.println("Element Name is " + eleNameList[k]);
	          String eleTypeAtt = eleTypeItem.getAttribute("type");
	          String eleTypeStr[] = eleTypeAtt.split("[:]");
	          eleType = eleTypeStr[1];

          /*        int l=0;
                  while(l<primType.length)
                  {
                    if(eleType.equals(primType[l]))
                    { eleTypeList[k] = eleType; break; }
                    else
                    {
                     l++;
                     if ( l < primType.length) continue;
                     else
                     {
                      eleTypeList1[k] = eleType;
                      NodeList cSimpNodes = document.getElementsByTagName(preTypes+":simpleType");
                      if(cSimpNodes.getLength()> 0)
                      {
              	      for(int n=0; n < cSimpNodes.getLength(); n++ )
                      {
                         Element cNameItem = (Element) cSimpNodes.item(n);
	                 String cName = cNameItem.getAttribute("name");
                         if (eleTypeList1[k].equals(cName))
                         {
                           flagS = true;
                           NodeList cTypeNode = cNameItem.getElementsByTagName(preTypes+":restriction");
                           Element cType = (Element) cTypeNode.item(0);
                           String cEleType[] = cType.getAttribute("base").split(":");
                           eleType = cEleType[1];
                           eleTypeList[k] = eleType;
                           NodeList cEnumNodes = cType.getElementsByTagName(preTypes+":enumeration");
                           for(int o=0;o<cEnumNodes.getLength();o++)
                           {
                               Element enumElement = (Element)cEnumNodes.item(o);
                               value[k]  += enumElement.getAttribute("value") + "  ";
                           }
                          }
                      } } // end for n, end if cSimpNodes
                      else
                      {
                        NodeList cCompNodes = document.getElementsByTagName(preTypes+":complexType");
                        if (cCompNodes.getLength() > 0)
                        { System.out.println("comp Len "+cCompNodes.getLength());
                          for(int n=0; n < cCompNodes.getLength(); n++ )
                          {
                            Element cNameItem = (Element) cCompNodes.item(n);
	                    String cName = cNameItem.getAttribute("name");
                            if(eleTypeList1[k].equals(cName))
                            {
                              flagC = true;
                              NodeList cTypeNode = cNameItem.getElementsByTagName(preTypes+":element");
                              Element cType = (Element) cTypeNode.item(0);
                              String cEleType[] = cType.getAttribute("type").split(":");
                              eleType = cEleType[1];
                              eleTypeList[k] = eleType;
                            } } // end if, end for n
                        } // end if cCompNodes
                      }  // end else
                    } }// end else
                  } */ // end while l
	         }  // end if
	       } // end for k
             }  // end if
   	  } // end if parent
        } // end for j
     // }  // end if Soap
/*** Wrting in XML file ***/
/*        Node EventoNode =null;
        if(flagS)
            EventoNode = m.createEventNode(document1, opTag, numParams, eleNameList, eleTypeList, value);
        else if (flagC)
            EventoNode = m.createEventNode(document1, opTag, numParams, eleNameList, eleTypeList,eleTypeList1);
        else
            EventoNode = m.createEventNode(document1, opTag, numParams, eleNameList, eleTypeList, value);
	root.appendChild ( EventoNode );   */
/*** Wrting in XML file ends ***/

      }  // end if opTag
      prevOp = opTag;
    }  // end for i

/*** Wrting in XML file ***/
/*    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
     trans.transform (new DOMSource(document1), new StreamResult (new FileOutputStream ("opList.xml")));  */
/*** Wrting in XML file ends ***/

 //  System.out.println("opCounter is " + opCounter);
  // System.out.println("\nEnd Parsing\n");
 } // end try block

  catch(Exception e) { System.out.println("Hello 2"); e.printStackTrace(); }
}  // parse() ends

/*
public static void main(String[] args)
{
    System.out.println("\nStarted Parsing ....\n");
    Main m = new Main();
    m.parse();

}
 */// end main

}  // end class

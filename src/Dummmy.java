/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rani
 */
public class Dummmy {
public static void main(String args[]){
    String invalid[]={"not","invalid","false","null","error"};
    boolean nodata=false;
    for(String test:invalid){
        System.out.println("Data Not found  false null".toLowerCase().contains(test));
     if("Data Not found".toLowerCase().contains(test)){
         nodata=true;
         break;
     }
     
    }
    
    
}
}

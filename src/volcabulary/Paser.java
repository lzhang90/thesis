/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

/**
 *
 * @author lzhang90
 */
public class Paser {
    public static String parseObj(String origin){ //eliminate the number of the instance 
        String returned="";
        char c;
        for(int i=0;i<origin.length();i++){
            c=origin.charAt(i);
            if(c<60 || c>71)
                returned+=c;
            else 
                break;
        }
        return returned;
    }
}

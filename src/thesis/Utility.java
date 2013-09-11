/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

/**
 *
 * @author lzhang90
 */
public class Utility {
    public static String delNum(String name){
    StringBuilder transferedName=new StringBuilder();
        char c;
        for(int i=0;i<name.length();i++){
            c=name.charAt(i);
            if(c<48 || c>57)
                transferedName.append(c);
            else
                break;
        }
        return transferedName.toString();
    }
        
}

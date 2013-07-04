/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.util.LinkedList;

/**
 *
 * @author lzhang90
 */
public class Feature {
    String title;
    LinkedList<String> vars=new LinkedList<String>();
    String obj;
    int generalLevel=0; //0 no generalization. 1 use variable instead of constant. 2 feature don't exist in the constraits. 
    int num;
    
    public Feature(String title){
        this.title=title;
    }
    


    public String getFeatureText(){
        String returnStr=title+"(";
        if(generalLevel==0)
            returnStr+=obj+", A, B)";
        else if(generalLevel==1)
            returnStr+="X"+num+", A, B)";
        /*for(int i=0;i<vars.size()-1;i++)
            returnStr+=vars.get(i)+",";
        returnStr+=vars.getLast()+")";*/
        return returnStr;
    }
}

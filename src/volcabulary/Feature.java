/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

import thesis.Utility;

/**
 *
 * @author lzhang90
 */
public class Feature {
    private String predicate;
    private String obj;
    private String extra;
    private String name;
    
    public Feature(String pred, String obj){
        this.predicate=pred;
        this.obj=obj;
        this.name=predicate+" "+obj;
    }
    public String getPred(){
        return this.predicate;
    }
    public String getObj(){
        return this.obj;
    }
    public boolean equals(Feature obj){
        if(this.predicate.equals(obj.predicate) && this.obj.equals(obj.obj))
            return true;
        else
            return false;
        
    }
    public boolean similarTo(Feature obj){
        String obj1=Utility.delNum(this.obj);
        String obj2=Utility.delNum(obj.obj);
        if(this.predicate.equals(obj.predicate) && obj1.equals(obj2))
            return true;
        else
            return false;
    }
    
}

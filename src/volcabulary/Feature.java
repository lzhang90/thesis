/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

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
    String getPred(){
        return this.predicate;
    }
    String getObj(){
        return this.obj;
    }
    
}

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
public class FeatureDef {
    String title;
    String predicate;
    LinkedList<String> conditions=new LinkedList<String>();
    
    public FeatureDef(String title, String predicate){
        this.title=title;
        this.predicate=predicate;
        if(predicate.equals("instance_of"))
        {
            conditions.add("A!=Obj");
            conditions.add("B!=Obj");
        }
        conditions.add("has(A_ins, instance_of, A)");
        conditions.add("has(A_ins, "+predicate+", Obj)");
        conditions.add("has(B_ins, instance_of, B)");
        conditions.add("has(B_ins, "+predicate+", Obj)");
}
    String genDefinitionText(){
        String def="";
        def=title+"(Obj, A, B):-\n";
        for(int i=0;i<this.conditions.size()-1;i++)
            def+=conditions.get(i)+",\n";
        def+=conditions.getLast()+".";
        
        return def;
    }
}

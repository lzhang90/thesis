/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lzhang90
 */
public class SchemaDiff {
    LinkedList<String> thingA_ins, thingB_ins;
    String thingA, thingB;
    FileReader fileReader;
    BufferedReader in;
    LinkedList<String []> predicateAndObjsA;
    LinkedList<String []> predicateAndObjsB;
    LinkedList<String []> predicateAndObjs;
    LinkedList<String []> relations;
    HashMap<String, LinkedList<String>> predicates=new HashMap<String, LinkedList<String>>();
    
    LinkedList<String> conditions=new LinkedList<String>();
    LinkedList<Feature> features=new LinkedList<Feature>();
    LinkedList<FeatureDef> featureDefs=new LinkedList<FeatureDef>();
    
    public SchemaDiff(String thingA, String thingB){
       this.thingA=thingA;
       this.thingB=thingB;
       this.thingA_ins=new LinkedList();
       this.thingB_ins=new LinkedList();
       predicateAndObjsA=new LinkedList();
       predicateAndObjsB=new LinkedList();
       predicateAndObjs=new LinkedList();
       relations=new LinkedList();
    }
    void openFile(File file){
        try {
            fileReader = new FileReader(file);
            in=new BufferedReader(fileReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SchemaDiff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void closeFile(){
        try {
            in.close();
            fileReader.close();
        } catch (IOException ex) {
            Logger.getLogger(SchemaDiff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void printOutInstances(){
        
        System.out.println("Instances of "+this.thingA);
        for(int i=0; i<thingA_ins.size();i++)
            System.out.print(thingA_ins.get(i)+",");
        System.out.println();
        System.out.println("Instances of "+this.thingB);
        for(int i=0; i<thingB_ins.size();i++)
            System.out.print(thingB_ins.get(i)+",");
        
    }
    void mergePairs(){
        String[] pairA,pairB;
        for(int i=0;i<this.predicateAndObjsA.size();i++){
            pairA=this.predicateAndObjsA.get(i);
            for(int j=0;j<this.predicateAndObjsB.size();j++){
                pairB=this.predicateAndObjsB.get(j);
                if(pairA[0].equals(pairB[0]) && pairA[1].equals(pairB[1])){
                    this.predicateAndObjs.add(pairA);
                    if(this.predicates.containsKey(pairA[0]))
                    {
                        this.predicates.get(pairA[0]).add(pairA[1]);
                    }
                    else
                    {
                        LinkedList<String> vars=new LinkedList<String>();
                        vars.add(pairA[1]);
                        this.predicates.put(pairA[0], vars);
                    }
                }
            }
        }
        for(int i=0;i<this.predicateAndObjs.size();i++)
            System.out.println(predicateAndObjs.get(i)[0]+","+predicateAndObjs.get(i)[1]);
        for(int i=0;i<this.relations.size();i++)
            System.out.println(relations.get(i)[0]+","+relations.get(i)[1]);
        
        Iterator<Map.Entry<String, LinkedList<String>>> it;
        it = predicates.entrySet().iterator();
        Map.Entry<String, LinkedList<String>> entry;
        while(it.hasNext()){
            entry=it.next();
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
    
    void addNewpair(LinkedList<String []> predicateAndObjs, String[] predicateAndObj){
        String[] pair;
        for(int i=0;i<predicateAndObjs.size();i++){
            pair=predicateAndObjs.get(i);
            boolean equal=true;
            for(int j=0;j<pair.length;j++)
                if(!pair[j].equals(predicateAndObj[j]))
                    equal=false;
            if(equal)
                return;
        }
        predicateAndObjs.add(predicateAndObj);
        
    }
    
    FeatureDef findFeatureTitle(String title){
        for(int i=0;i<this.featureDefs.size();i++){
            if(title.equals(this.featureDefs.get(i).title))
                return this.featureDefs.get(i);
        }
        return null;
    }
    
    void genConditions(){ //the condition is in the format "has(agent, predicate, object)"
        
        
        conditions.add("A!=B"); //book keeping conditions
        Iterator<Map.Entry<String, LinkedList<String>>> it;
        it = predicates.entrySet().iterator();
        Map.Entry<String, LinkedList<String>> entry;
        int num=0;
        while(it.hasNext()){
            entry=it.next();
            String predicate=entry.getKey();
            LinkedList<String> vars=new LinkedList<String>();//store all the variables of the predicate, if any exists. 
            LinkedList<String> objs=entry.getValue();
            
            if(this.findFeatureTitle("twoleveltree_"+predicate)==null)
                this.featureDefs.add(new FeatureDef("twoleveltree_"+predicate,predicate));
            
            //add the features 
            for(int i=0;i<objs.size();i++){
                String obj=objs.get(i);
                Feature feature;
                feature=new Feature("twoleveltree_"+predicate);
                feature.obj=obj;
                feature.vars.add(obj);
                feature.vars.add("A");
                feature.vars.add("B");
                feature.num=num++;
                feature.generalLevel=1;//debug
                this.features.add(feature);
                
                if(!obj.equals(obj.toLowerCase()))//obj is a variable
                {
                    for(int j=0;j<vars.size();j++)
                        conditions.add(obj+"!="+vars.get(j));
                    vars.add(obj);
                }
            }
            System.out.println(conditions);
        }
    }
    
    void genCode(){
        PrintStream out=System.out;
        
        for(int i=0;i<this.featureDefs.size()-1;i++)
            out.println(this.featureDefs.get(i).genDefinitionText());
        out.println("what_is_the_difference_between_A_and_B(A,B) :-");
        for(int i=0;i<this.features.size()-1;i++)
            out.println(this.features.get(i).getFeatureText()+",");
        for(int i=0;i<this.conditions.size()-1;i++)
            out.println(conditions.get(i)+",");
        out.println(conditions.getLast()+".");
        out.println("#hide has(X,Y,Z).");
        out.println("#hide has_slot_condition(A,B,C,D).");
        out.println("#hide has_slot_condition(A,B,C,D,E).");
        out.println("#hide equation_variable_binding(A,B).");
    }
    
    void printPredicatesAndObjs(){
        for(int i=0;i<this.predicateAndObjsA.size();i++)
            System.out.println(this.thingA+","+predicateAndObjsA.get(i)[0]+","+predicateAndObjsA.get(i)[1]);
        for(int i=0;i<this.predicateAndObjsB.size();i++)
            System.out.println(this.thingB+","+predicateAndObjsB.get(i)[0]+","+predicateAndObjsB.get(i)[1]);
    }
    
    void readPredicatesAndObj(File file){
        try {
            String instance;
            String predicate;
            String thing;
            String newLine;
            String[] items;
            String[] predicateAndObj;
            String[] relation;
            while((newLine=in.readLine())!=null)
            {
                if(!newLine.contains(",") || !newLine.contains("(") || !newLine.contains(")"))
                    continue;
                if(!newLine.startsWith("has"))
                    continue;
                newLine=newLine.substring(newLine.indexOf("(")+1, newLine.indexOf(")"));
                items=newLine.split(",");
                if(items.length!=3)
                    continue;
                instance=items[0].trim();
                predicate=items[1].trim();
                thing=items[2].trim();
                if(this.thingA_ins.contains(instance)){
                    predicateAndObj=new String[2];
                    predicateAndObj[0]=predicate;
                    predicateAndObj[1]=thing;
                    addNewpair(this.predicateAndObjsA, predicateAndObj);
                    System.out.println(instance+", "+predicate+", "+thing);
                    
                    if(this.thingB_ins.contains(thing))
                    {
                        relation=new String[3];
                        relation[0]=instance;
                        relation[1]=predicate;
                        relation[2]=thing;
                        addNewpair(this.relations,relation); 
                        System.out.println(instance+", "+predicate+", "+thing);
                    }
                }
                else if(this.thingB_ins.contains(instance)){
                    predicateAndObj=new String[2];
                    predicateAndObj[0]=predicate;
                    predicateAndObj[1]=thing;
                    addNewpair(this.predicateAndObjsB, predicateAndObj);
                    System.out.println(instance+", "+predicate+", "+thing);
                    
                    if(this.thingA_ins.contains(thing))
                    {
                        relation=new String[3];
                        relation[0]=instance;
                        relation[1]=predicate;
                        relation[2]=thing;
                        addNewpair(this.relations,relation);  
                        System.out.println(instance+", "+predicate+", "+thing);
                    }
                }
                
                    
            }
        } catch (IOException ex) {
            Logger.getLogger(SchemaDiff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void readInstancesFromFile(File file){
        try {
            
            String newLine;
            String instance;
            String predicate;
            String thing;
            String[] items;
            while((newLine=in.readLine())!=null)
            {
                if(!newLine.contains(",") || !newLine.contains("(") || !newLine.contains(")"))
                    continue;
                newLine=newLine.substring(newLine.indexOf("(")+1, newLine.indexOf(")"));
                items=newLine.split(",");
                if(items.length!=3)
                    continue;
                instance=items[0].trim();
                predicate=items[1].trim();
                thing=items[2].trim();
                if(predicate.equals("instance_of"))
                {
                    if(thing.equals(this.thingA))
                    {
                        if(!this.thingA_ins.contains(instance))
                            this.thingA_ins.add(instance);
                    }
                    else if(thing.equals(this.thingB))
                        if(!this.thingB_ins.contains(instance))
                            this.thingB_ins.add(instance);
                    
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    void updateFeaturesWithNegExp(SchemaDiff negExp){ //decrease the generalization level with negative example
        LinkedList<Feature> candidates=new LinkedList<Feature>();
        for(int i=0;i<this.features.size();i++){
            Feature feature=this.features.get(i);
            if(feature.generalLevel>0){
                boolean match=false;
                for(int j=0;j<negExp.features.size();j++){
                    Feature negFeature=negExp.features.get(j);
                    if(feature.obj.equals(negFeature.obj) && feature.title.equals(negFeature.title))
                        match=true;
                }
                if(!match){
                    //when no match is found, de-generalizing the feature can rule out the negative example.
                    candidates.add(feature);
                }
            }
        }
        
        //debug
        for(int i=0;i<candidates.size();i++){
            candidates.get(i).generalLevel--;
        }
        this.genCode();
        
    }
}

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
import java.util.LinkedList;
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
                if(pairA[0].equals(pairB[0]) && pairA[1].equals(pairB[1]))
                    this.predicateAndObjs.add(pairA);
            }
        }
        for(int i=0;i<this.predicateAndObjs.size();i++)
            System.out.println(predicateAndObjs.get(i)[0]+","+predicateAndObjs.get(i)[1]);
        for(int i=0;i<this.relations.size();i++)
            System.out.println(relations.get(i)[0]+","+relations.get(i)[1]);
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
                    
                    if(this.thingB_ins.contains(thing))
                    {
                        relation=new String[3];
                        relation[0]=instance;
                        relation[1]=predicate;
                        relation[2]=thing;
                        addNewpair(this.relations,relation);                       
                    }
                }
                else if(this.thingB_ins.contains(instance)){
                    predicateAndObj=new String[2];
                    predicateAndObj[0]=predicate;
                    predicateAndObj[1]=thing;
                    addNewpair(this.predicateAndObjsB, predicateAndObj);
                    
                    if(this.thingA_ins.contains(thing))
                    {
                        relation=new String[3];
                        relation[0]=instance;
                        relation[1]=predicate;
                        relation[2]=thing;
                        addNewpair(this.relations,relation);                       
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
}

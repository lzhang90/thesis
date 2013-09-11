/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import thesis.Utility;

/**
 *
 * @author lzhang90
 */
public class Volcabulary {
    HashMap<String, Concept> concepts=new HashMap<String, Concept>();
    public Concept findConcept(String name){
        return concepts.get(name);
    }
    private void print(){
        for(int i=0;i<this.concepts.size();i++){
            System.out.print(this.concepts.get(i).name+"  ");
            for(int j=0;j<concepts.get(i).cells.size();j++){
                System.out.print(concepts.get(i).cells.get(j)+" ");
            }
        }
    }
    public Concept[] getConceptArray(){
        Concept[] conceptArray=this.concepts.values().toArray(new Concept[concepts.size()]);
        return conceptArray;
        
    }
    public void readFromFile(String filename){
        FileReader file=null;
        try {
            concepts.clear();
            file = new FileReader(filename);
            BufferedReader freader=new BufferedReader(file);
            String line;
            Concept concept;
            String[] items;
            String[] parts;
            while((line=freader.readLine())!=null){
                items=line.split(",");
                concept=new Concept(items[0]);
                for(int i=1;i<items.length;i++){
                    parts=items[i].split("@");
                    concept.addACell(parts[0], parts[1], Integer.valueOf(parts[2]));
                }
                this.concepts.put(items[0], concept);
            }
            freader.close();
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public void buildVolcabulary(String filename){
        BufferedReader freader=null;
        try {
            FileReader file=new FileReader(filename);
            freader = new BufferedReader(file);
            String newline;
            String[] items;
            Concept concept;
            //int limit=80000;
            int count=0;
            String conceptName;
            while((newline=freader.readLine())!=null){
                if(!newline.contains(",") || !newline.contains("(") || !newline.contains(")"))
                    continue;
                if(!newline.startsWith("has"))
                    continue;
                newline=newline.substring(newline.indexOf("(")+1, newline.indexOf(")"));
                //System.out.println(newline);
                items=newline.split(",");
                if(items.length!=3)
                    continue;
                conceptName=Utility.delNum(items[0].trim()).replaceAll("_", " ").trim();
                concept=this.findConcept(conceptName);
                if(concept==null){
                    concept=new Concept(conceptName);
                    this.concepts.put(concept.name, concept);
                }
                concept.updateFeature(items[1].trim(), items[2].trim());
           //     if(count++>limit)
             //       break;
               
            }
            
            this.print2file("output.csv");
            //this.print();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                freader.close();
            } catch (IOException ex) {
                Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void makeGeneralConstraints(String[] egConcepts){
        //decide a threshold, if the number of features of a concept exceeds the threshold, the concept will be excluded. 
        int threshold=-1;
        for(int i=0;i<egConcepts.length;i++){
            Concept concept=this.findConcept(egConcepts[i]);
            int temp=0;
            for(int j=0;j<concept.cells.size();j++){
                temp+=concept.cells.get(j).value;
            }
            if(threshold==-1)
                threshold=temp;
            else if(temp<threshold)
                threshold=temp;
        }
        System.out.println(threshold);
        Iterator<Entry<String,Concept>> it=this.concepts.entrySet().iterator();
        while(it.hasNext()){
            Entry<String,Concept> entry=it.next();
            Concept concept=entry.getValue();
            int temp=0;
            for(int j=0;j<concept.cells.size();j++){
                temp+=concept.cells.get(j).value;
            }
            if(temp>=threshold){
                concept.ignore=true;
                System.out.println(concept.name);
            }
        }       
    }
    
    public void print2file(String filename){
        BufferedWriter out = null;
        try {
            Concept concept;
            FileWriter file=new FileWriter(filename);
            out = new BufferedWriter(file);
            Iterator it=this.concepts.entrySet().iterator();
            while(it.hasNext()){
                concept=((Entry<String,Concept>)it.next()).getValue();
                out.write(concept.name+",");
                for(Cell cell: concept.cells){
                    out.write(cell.feature.getPred()+"@"+cell.feature.getObj()+"@"+cell.value+",");
                }
                out.write("\n"); 
            }
            out.close();
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Volcabulary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}





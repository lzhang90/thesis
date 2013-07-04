/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lzhang90
 */
public class Volcabulary {
    LinkedList<Concept> concepts=new LinkedList<Concept>();
    private Concept findConcept(String name){
        for(int i=0;i<concepts.size();i++){
            if(concepts.get(i).name.equals(name))
                return concepts.get(i);
        }
        return null;
    }
    private void print(){
        for(int i=0;i<this.concepts.size();i++){
            System.out.print(this.concepts.get(i).name+"  ");
            for(int j=0;j<concepts.get(i).cells.size();j++){
                System.out.print(concepts.get(i).cells.get(j)+" ");
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
            while((newline=freader.readLine())!=null){
                if(!newline.contains(",") || !newline.contains("(") || !newline.contains(")"))
                    continue;
                if(!newline.startsWith("has"))
                    continue;
                newline=newline.substring(newline.indexOf("(")+1, newline.indexOf(")"));
                items=newline.split(",");
                if(items.length!=3)
                    continue;
                concept=this.findConcept(items[0].trim());
                if(concept==null){
                    concept=new Concept(items[0].trim());
                    this.concepts.add(concept);
                }
                concept.updateFeature(items[1].trim(), items[2].trim());
            }
            this.print();
            
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
}

class Concept{//each concept is a row in the volcabulary matrix
    String name;
    LinkedList<Cell> cells;
    Concept(String name){
        this.name=name;
        this.cells=new LinkedList<Cell>();
    }
    void updateFeature(String pred, String obj){
        Cell cell;
        boolean updated=false;
        boolean samePred=false;
        for(int i=0;i<this.cells.size();i++){
            cell=cells.get(i);
            if(cell.feature.getPred().equals(pred) && cell.feature.getObj().equals(obj)){
                cell.value++;
                updated=true;
                break;
            }
            else if(cell.feature.getPred().equals(pred) && !samePred)
            {
                samePred=true;
            }
            else if(!cell.feature.getPred().equals(pred) && samePred){
                cells.add(i,new Cell(new Feature(pred, obj)));
                updated=true;
                break;
            }
        }
        if(!updated)
            cells.add(new Cell(new Feature(pred, obj)));
    }
}

class Cell{//one object represents a cell in the matrix
    Feature feature;
    int value;
    Cell(Feature feature){
        this.feature=feature;
        value=1;
    }
}

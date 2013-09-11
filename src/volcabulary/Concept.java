/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

import java.util.LinkedList;

/**
 *
 * @author lzhang90
 */
public class Concept{//each concept is a row in the volcabulary matrix
    public String name;
    public LinkedList<Cell> cells;
    public boolean ignore=false;//true means that this concept is too general(or called book keeping concept) and should be ignored in the process of generation.
    Concept(String name){
        this.name=name;
        this.cells=new LinkedList<Cell>();
    }
    void addACell(String pred,String obj, int num){
        this.cells.add(new Cell(new Feature(pred, obj),num));
    }
    public boolean hasFeature(Feature feature){
        for(int i=0;i<cells.size();i++){
            if(cells.get(i).feature.equals(feature))
                return true;
        }
        return false;
    }
    
    public boolean hasSimilarFeature(Feature feature){
        for(int i=0;i<cells.size();i++){
            if(cells.get(i).feature.similarTo(feature))
                return true;
        }
        return false;
    }
    
    public boolean hasPredInFeatures(String pred){
        for(int i=0;i<cells.size();i++){
            if(cells.get(i).feature.getPred().equals(pred))
                return true;
        }
        return false;
    }
    public LinkedList<String> ObjsForPred(String pred){
        LinkedList<String> objs=new LinkedList<String>();
        for(int i=0;i<cells.size();i++){
            if(cells.get(i).feature.getPred().equals(pred)){
                objs.add(cells.get(i).feature.getObj());
            }
        }
        return objs;
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
    boolean matchEntireCell(Cell cell){ //both predicate and object are equal
        Cell thiscell;
        for(int i=0;i<this.cells.size();i++){
            thiscell=this.cells.get(i);
            if(thiscell.feature.equals(cell.feature))
                return true;
        }
        return false;
        
    }
    public int calSimilarity(Concept concept){
        Cell cell;
        int sim=0;
        for(int i=0;i<concept.cells.size();i++){
            cell=concept.cells.get(i);
            if(this.matchEntireCell(cell))
                sim++;
        }
        return sim;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volcabulary;

/**
 *
 * @author lzhang90
 */
public class Cell{//one object represents a cell in the matrix
    Feature feature;
    int value;
    Cell(Feature feature){
        this.feature=feature;
        value=1;
    }
    Cell(Feature feature, int num){
        this.feature=feature;
        value=num;
    }
    
    public Feature getFeature(){
        return this.feature;
    }
    
    public boolean shareFeature(Cell cell){
        return this.feature.equals(cell.feature);
    }
}
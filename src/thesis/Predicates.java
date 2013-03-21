/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lzhang90
 */
public class Predicates {
    HashMap<String, Integer> predicatesMap=new HashMap<String, Integer>();
    
    void readPredicateFromFile(File file){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader in=new BufferedReader(fileReader);
            String newLine;
            String predicate;
            while((newLine=in.readLine())!=null)
            {
                if(!newLine.contains(","))
                    continue;
                predicate=newLine.split(",")[1];
                if(predicatesMap.containsKey(predicate))
                    predicatesMap.put(predicate, predicatesMap.get(predicate)+1);
                else
                    predicatesMap.put(predicate, 1);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void printPredicats(){
        Iterator<Map.Entry<String, Integer>> it;
        it = predicatesMap.entrySet().iterator();
        Map.Entry<String, Integer> entry;
        while(it.hasNext()){
            entry=it.next();
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
    
    void outputPredicates(String fileName){
        try {
            FileWriter fileout=new FileWriter(fileName);
            BufferedWriter out=new BufferedWriter(fileout);
            Iterator<Map.Entry<String, Integer>> it;
            it = predicatesMap.entrySet().iterator();
            Map.Entry<String, Integer> entry;
            while(it.hasNext()){
                entry=it.next();
                out.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            out.close();
            fileout.close();
        } catch (IOException ex) {
            Logger.getLogger(Predicates.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

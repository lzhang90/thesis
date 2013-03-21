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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lzhang90
 */
public class SchemaTranslation {
    FileReader fileReader;
    BufferedReader in;
    LinkedList<String> questions=new LinkedList<String>();
    HashMap<String, String> schema=new HashMap<String,String>();
    
    public SchemaTranslation(File file){
        try {
            fileReader = new FileReader(file);
            in=new BufferedReader(fileReader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SchemaDiff.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        schema.put("is_Part_a_part_of_the_Thing", "Is var0 a part of the var1?");
        schema.put("what_is_the_Part_of_the_Thing", "What is the var0 of the var1?");
        schema.put("what_is_the_difference_between_A_and_B", "What is the difference between var0 and var1?");
        
    }
    
    void translate(){
        try {
            String newLine;
            String[] questions;
            while((newLine=in.readLine())!=null){
                questions=newLine.split(" ");
                String qKey;
                String[] qVars;
                String question;
                String var;
                boolean skip;
                for(int i=0;i<questions.length;i++){
                    skip=false;
                    if(!questions[i].contains("("))
                        continue;
                    qKey=questions[i].substring(0,questions[i].indexOf("("));
                    qVars=questions[i].substring(questions[i].indexOf("(")+1,questions[i].indexOf(")")).split(",");
                    question=this.schema.get(qKey);
                    if(question==null)
                        continue;
                    for(int j=0;j<qVars.length;j++){
                        if(qVars[j].endsWith("entity"))
                            skip=true;
                        question=question.replaceAll("var"+j, qVars[j]);
                    }
                    question=question.replaceAll("_", " ");
                    if(!skip)
                        System.out.println(question);
                    
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(SchemaTranslation.class.getName()).log(Level.SEVERE, null, ex);
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
}

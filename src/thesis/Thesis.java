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
import java.util.Map.Entry;

/**
 *
 * @author Lishan
 */
public class Thesis {

    /**
     * @param args the command line arguments
     */
    
    public static void schemaDiff(){
            SchemaDiff shcemaDiff=new SchemaDiff("cell_wall","plasma_membrane");
         /*    File test=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\ASP_all files from KB\\Plasma-membrane.lp");
            shcemaDiff.readInstancesFromFile(test);
        */
       
        
        
        File dir=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\ASP_all files from KB");
        File[] files=dir.listFiles();
        for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory()){
                shcemaDiff.openFile(file);
                shcemaDiff.readInstancesFromFile(file);
                shcemaDiff.closeFile();
            }
                
        }
        //predicates.printPredicats();
        shcemaDiff.printOutInstances();
        
        for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory()){
                shcemaDiff.openFile(file);
                shcemaDiff.readPredicatesAndObj(file);
                shcemaDiff.closeFile();
            }
        }
        shcemaDiff.printPredicatesAndObjs();
        shcemaDiff.mergePairs();
        
    }
    public static void predicates(){
        File dir=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\ASP_all files from KB");
        File[] files=dir.listFiles();
        Predicates predicates=new Predicates();
        for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory())
                predicates.readPredicateFromFile(file);
        }
        //predicates.printPredicats();
        predicates.outputPredicates("predicates.csv");
    }
    
    public static void translate(){
        File file=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\questions.txt");
        System.out.println(file.canRead());
        SchemaTranslation translation=new SchemaTranslation(file);
        translation.translate();
        
    }
    
    public static void generateKB() throws IOException{
        File dir=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\ASP_all files from KB");
        File[] files=dir.listFiles();
        FileReader fileReader;
        FileWriter outFile= new FileWriter("C:\\Users\\lzhang90\\Dropbox\\bio kb\\bio_kb.lp");
        BufferedWriter out=new BufferedWriter(outFile);
        for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory()){
                fileReader=new FileReader(file);
                BufferedReader in=new BufferedReader(fileReader);
                char a;
                int i;
                while((i=in.read())!=-1){
                    a=(char)i;
                    out.append(a);
                }
                in.close();
                fileReader.close();
            }
        }
        out.close();
        outFile.close();
        //predicates.printPredicats();
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //schemaDiff();
        translate();
        //generateKB();
        
    }
}

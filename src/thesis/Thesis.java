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

/**
 *
 * @author Lishan
 */
public class Thesis {

    /**
     * @param args the command line arguments
     */
    
    //static String rootDir="C:\\Users\\lzhang90\\Dropbox\\bio kb\\";
     static String rootDir="C:\\Users\\Lishan\\Dropbox\\bio kb\\";
    
    public static void schemaDiff(){
            SchemaDiff shcemaDiff=new SchemaDiff("cell_wall","plasma_membrane");
        //SchemaDiff shcemaDiff=new SchemaDiff("monosaccharide","disaccharide");
         /*    File test=new File("C:\\Users\\lzhang90\\Dropbox\\bio kb\\ASP_all files from KB\\Plasma-membrane.lp");
            shcemaDiff.readInstancesFromFile(test);
        */
       
        File file=new File(rootDir+"bio_kb.lp");
        shcemaDiff.openFile(file);
        shcemaDiff.readInstancesFromFile(file);
        shcemaDiff.closeFile();
        shcemaDiff.openFile(file);
        shcemaDiff.readPredicatesAndObj(file);
        shcemaDiff.closeFile();
        
       /* File dir=new File(rootDir+"ASP_all files from KB");
        File[] files=dir.listFiles();
        for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory()){
                shcemaDiff.openFile(file);
                shcemaDiff.readInstancesFromFile(file);
                shcemaDiff.closeFile();
            }
                
        }*/
        //predicates.printPredicats();
        shcemaDiff.printOutInstances();
        
    /*    for(File file : files){
            System.out.println("Reading predictes from "+file.getName());
            if(!file.isDirectory()){
                shcemaDiff.openFile(file);
                shcemaDiff.readPredicatesAndObj(file);
                shcemaDiff.closeFile();
            }
        }
        * */
        shcemaDiff.printPredicatesAndObjs();
        shcemaDiff.mergePairs();
        shcemaDiff.genConditions();
        shcemaDiff.genCode();
        
    }
    public static void predicates(){
        File dir=new File(rootDir+"ASP_all files from KB");
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
        File file=new File(rootDir+"questions.txt");
        System.out.println(file.canRead());
        SchemaTranslation translation=new SchemaTranslation(file);
        translation.translate();
        
    }
    
    public static void generateKB() throws IOException{
        File dir=new File(rootDir+"ASP_all files from KB");
        File[] files=dir.listFiles();
        FileReader fileReader;
        FileWriter outFile= new FileWriter(rootDir+"bio_kb.lp");
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
        schemaDiff();
        //translate();
        //generateKB();
        
    }
}

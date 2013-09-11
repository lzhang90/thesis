/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.util.HashMap;
import java.util.LinkedList;
import volcabulary.Concept;
import volcabulary.Volcabulary;
import volcabulary.Feature;

/**
 *
 * @author lzhang90
 */
public class Schema_Diff {
    Volcabulary v;
    int threshold;
    int shareLevel=2;
    LinkedList<Feature> sharedFeature=new LinkedList<Feature>();
    
    public Schema_Diff(Volcabulary v){
        this.v=v;
    }
    void learnFromExample(String conceptName1, String conceptName2){
        Concept concept1,concept2;
        concept1=v.findConcept(conceptName1);
        concept2=v.findConcept(conceptName2);
        threshold=this.threshold=concept1.calSimilarity(concept2);
        System.out.println(threshold);
        
        for(int i=0;i<concept1.cells.size();i++){
            for(int j=0;j<concept2.cells.size();j++){
                if(concept1.cells.get(i).shareFeature(concept2.cells.get(j)))
                    sharedFeature.add(concept1.cells.get(i).getFeature());
            }
        }
    }
    
    void generateQues(){
       String[] egConcepts={"entity"};
       v.makeGeneralConstraints(egConcepts);
       Concept[] conceptArray=v.getConceptArray();
       this.genQuesByFeature(conceptArray);
       
    }
    
    private void genQuesByThreshold(Concept[] conceptArray){
        Concept concept1, concept2;
       for(int i=0;i<conceptArray.length;i++){
           concept1=conceptArray[i];
           if(concept1.ignore)
               continue;
           for(int j=0;j<conceptArray.length;j++){
               if(i==j)
                   continue;
               concept2=conceptArray[j];
               if(concept2.ignore)
                   continue;
               if(concept1.calSimilarity(concept2)>this.threshold)
                   System.out.println(concept1.name+"  "+concept2.name);
           }
       }
    }
    
    private void genQuesByFeature(Concept[] conceptArray){
        LinkedList<String> sharedPreds=new LinkedList<String>();
        HashMap<String, LinkedList<String>> predObjs=new HashMap<String, LinkedList<String>>();
        for(int m=0;m<this.sharedFeature.size();m++){
            if(!sharedPreds.contains(sharedFeature.get(m).getPred())){
                sharedPreds.add(sharedFeature.get(m).getPred());
                LinkedList<String> objs=new LinkedList<String>();
                objs.add(sharedFeature.get(m).getObj());
                predObjs.put(sharedFeature.get(m).getPred(), objs);
            }
            else{
                LinkedList<String> objs=predObjs.get(sharedFeature.get(m).getPred());
                objs.add(sharedFeature.get(m).getObj());
                predObjs.put(sharedFeature.get(m).getPred(), objs);
            }
        }
        
        
        Concept concept1, concept2;
        for(int i=0;i<conceptArray.length;i++){
           concept1=conceptArray[i];
           if(concept1.ignore)
               continue;
           for(int j=0;j<conceptArray.length;j++){
               if(i==j)
                   continue;
               concept2=conceptArray[j];
               if(concept2.ignore)
                   continue;
               if(shareLevel==0){ //only the shared predicate is found
                   int matched=0;
                   for(int m=0;m<sharedPreds.size();m++){
                       if(concept1.hasPredInFeatures(sharedPreds.get(m)) && concept2.hasPredInFeatures(sharedPreds.get(m)))
                           matched++;
                   }
                   if(matched==sharedPreds.size())
                       System.out.println("What is the difference between "+concept1.name+" and "+concept2.name+"?");
               }
               else if(shareLevel==1){ //the shared predicate should be found and the applied object for the corresponding predicate should exceed the number
                   int matched=0;
                   for(int m=0;m<sharedPreds.size();m++){
                       int numThreshold=predObjs.get(sharedPreds.get(m)).size();
                       LinkedList<String> concept1Objs=concept1.ObjsForPred(sharedPreds.get(m));
                       LinkedList<String> concept2Objs=concept2.ObjsForPred(sharedPreds.get(m));
                       for(int n=0;n<concept1Objs.size();n++){
                           if(concept2Objs.contains(concept1Objs.get(n)))
                               numThreshold--;
                       }
                       if(numThreshold<=0)
                           matched++;
                   }
                   if(matched==sharedPreds.size())
                       System.out.println("What is the difference between "+concept1.name+" and "+concept2.name+"?");
               }
               else if(shareLevel==2){
                   int matched=0;
                   int total=0;
                   for(int m=0;m<sharedPreds.size();m++){
                       LinkedList<String> objs=predObjs.get(sharedPreds.get(m));
                       for(int n=0;n<objs.size();n++){
                           Feature feature=new Feature(sharedPreds.get(m),objs.get(n));
                           total++;
                           if(concept1.hasSimilarFeature(feature) && concept2.hasSimilarFeature(feature))
                               matched++;
                       }
                   }
                   if(matched==total)
                        System.out.println("What is the difference between "+concept1.name+" and "+concept2.name+"?");
               }
               else if(shareLevel==3){
                   int matched=0;
                   int total=0;
                   for(int m=0;m<sharedPreds.size();m++){
                       LinkedList<String> objs=predObjs.get(sharedPreds.get(m));
                       for(int n=0;n<objs.size();n++){
                           Feature feature=new Feature(sharedPreds.get(m),objs.get(n));
                           total++;
                           if(concept1.hasFeature(feature) && concept2.hasFeature(feature))
                               matched++;
                       }
                   }
                   if(matched==total)
                        System.out.println("What is the difference between "+concept1.name+" and "+concept2.name+"?");
               }
           }
       }
    }
}


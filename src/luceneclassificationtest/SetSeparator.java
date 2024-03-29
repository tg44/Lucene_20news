package luceneclassificationtest;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Törcsi
 */
public class SetSeparator {
    private ArrayList<File> trainSet=new ArrayList<File>();
    private ArrayList<File> trySet=new ArrayList<File>();

    public ArrayList<File> getTrainSet() {
        return trainSet;
    }

    public ArrayList<File> getTrySet() {
        return trySet;
    }
    
    public void separatedSets(File parentDic){
        for(File separatedDic:parentDic.listFiles()){
            if(separatedDic.getName().contains("train")){
                for(File classDic:separatedDic.listFiles()){
                    for(File doc:classDic.listFiles()){
                        trainSet.add(doc);
                    }
                }
            }
            if(separatedDic.getName().contains("test")){
                for(File classDic:separatedDic.listFiles()){
                    for(File doc:classDic.listFiles()){
                        trySet.add(doc);
                    }
                }
            }
        }
    }
    
    public void separateSets(File parentDic, double trySetPercent){
        for(File classDic:parentDic.listFiles()){
            if(classDic.isDirectory()){
                int numberOfDocuments = classDic.listFiles().length;
                int numberOfTryDocs = (int)(numberOfDocuments*trySetPercent);
                int numberOfTrainDocs=numberOfDocuments-numberOfTryDocs;
                
                int i=0;
                
                for(File doc:classDic.listFiles()){
                    i++;
                    if(i<numberOfTrainDocs){
                        trainSet.add(doc);
                    } else {
                        trySet.add(doc);
                    }
                }
            }
        }
    } 
}

package luceneclassificationtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 *
 * @author Törcsi
 */
public class Parser {

    public static Document parseFile(File file) {
        Document retDoc = new Document();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            //head data
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    break;
                }

                String[] data = line.split(": ");
                if (data[0].equals("Newsgroups")) {
                    String[] groups=data[1].split(",");
                    for(String group:groups){
                        retDoc.add(new StringField(data[0], group, Field.Store.YES));
                    }
                } else if(data.length>1) {
                    retDoc.add(new StringField(data[0], data[1], Field.Store.YES));
                }
            }
            String text = "";
            while ((line = br.readLine()) != null) {
                text += line + "\n";
            }
            retDoc.add(new TextField("text", new StringReader(text)));
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        return retDoc;
    }
    
    public static String parseTestFile(File file) {
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            //head data
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    break;
                }
            }
            
            while ((line = br.readLine()) != null) {
                text += line + "\n";
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return text;
    }
}

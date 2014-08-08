package luceneclassificationtest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.classification.ClassificationResult;
import org.apache.lucene.classification.SimpleNaiveBayesClassifier;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 *
 * @author Törcsi
 */
public class LuceneClassificationTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*File f=new File("news20/20_newsgroup/alt.atheism/51121");
        Document doc=Parser.parseFile(f);
        
        System.out.print(doc);*/
        try{
            File f=new File("indexdir");
            f.mkdir();
            Directory dir = FSDirectory.open(f);
            
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_5_0);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_5_0, analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(dir, iwc);
            
            SetSeparator separator=new SetSeparator();
            separator.separateSets(new File("news20/20_newsgroup"), 0.2);
            List<File> trainSet=separator.getTrainSet();
            for(File train:trainSet){
                Document doc=Parser.parseFile(train);
                writer.addDocument(doc);
            }
            writer.commit();
            writer.close();
            
            List<File> testSet=separator.getTrySet();
            DirectoryReader reader = DirectoryReader.open(dir);
            SimpleNaiveBayesClassifier SNBC=new SimpleNaiveBayesClassifier();
            SNBC.train(SlowCompositeReaderWrapper.wrap(reader), "text", "Newsgroups", analyzer);
            
            for(File testDoc:testSet){
                String text=Parser.parseTestFile(testDoc);
                Document doc=Parser.parseFile(testDoc);
                String[] waited=doc.getValues("Newsgroups");
                ClassificationResult<BytesRef> classval=SNBC.assignClassNormalized(text);
                System.out.println(testDoc.getName()+" "+waited[0].equals(classval.getAssignedClass().utf8ToString())+" "+waited[0]+" "+classval.getAssignedClass().utf8ToString());
            }
            
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

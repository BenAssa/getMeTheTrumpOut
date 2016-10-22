import controllers.alchemy;
import models.AlchemyModel;

import java.io.*;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ben on 10/19/16.
 */
public class Prepare
{
static alchemy alcController= new alchemy();
static HashMap<String,String> sentences=new HashMap<String,String>();
static HashMap<String,String> words=new HashMap<String,String>();
static HashMap<String,String> relations=new HashMap<String,String>();


public static boolean insertTwoColumns(HashMap<String,String> map,String table,Connection c,String titles,boolean secondIsInt)
{

    Statement stmt = null;

    Set<String> keys=      map.keySet();
    for (String key : keys) {
        try
        {


            stmt = c.createStatement();
            String sql = "INSERT INTO "+table+" ("+titles+") ";
            if (secondIsInt)
                    sql+= "VALUES ("+key.replaceAll("'","#")+", "+map.get(key).replaceAll("'","#")+");";
            else
                sql+= "VALUES ('"+key.replaceAll("'","#")+"', "+map.get(key).replaceAll("'","#")+");";
            System.err.println(sql);
            stmt.executeUpdate(sql);
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return false;
        }


    }
    return true;


}


static void procLine(String line){
if   (line.length()<2)
    return;
    //res+=
    //res+=getWords(type.toLowerCase(),type + "GetRankedConcepts" ,target,"concepts");
    String senId=""+sentences.size();
    sentences.put(line,senId);
    return;
            /*
    String res= AlchemyModel.getWords("text","TextGetRankedKeywords" , "&text=" +URLEncoder.encode(line),"keywords","text");
     res+= AlchemyModel.getWords("text","TextGetRankedConcepts" , "&text=" +URLEncoder.encode(line),"concepts","text");
    res+= AlchemyModel.getWords("text","TextGetRankedNamedEntities" , "&text=" +URLEncoder.encode(line),"entities","text");
    res+= AlchemyModel.getWords("text","TextGetRankedTaxonomy" , "&text=" +URLEncoder.encode(line),"taxonomy","label");
String[] ress=res.split("\n");
    for (int i=0;i<ress.length;i++)
    {
String word= ress[i];
        if (word.length()>2)
        {
            String wordId;
            if (words.containsKey(word))
                wordId = words.get(word);
            else
            {
                System.err.println("="+word);
                wordId = "" + words.size();
                words.put(word, wordId);
            }
            relations.put(wordId,senId);
        }else
            System.err.println("=="+word);



    }
*/
}
public static void main(String[] args)
{
    FileInputStream fin;

    try {
    fin = new FileInputStream("poppy.txt");
    try (InputStream is = fin) {
        BufferedReader lines = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        //moving the pointer a line forward
        String line;
        lines.readLine();
        int i=0;
        while (true) {

            i++;
            line = lines.readLine();
            if (line == null)
                break;
            procLine(line)    ;    };
    }
    // Close our input stream
    fin.close();
    //HashMap<Integer,String> sentences=new HashMap<Integer,String>();
    //for testing
    // sentences.put(1,"one");
    //sentences.put(2,"two");

    Connection c = null;

                //Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://ec2-54-163-239-218.compute-1.amazonaws.com:5432/d80ffi19ifac15?sslmode=require",
                                "vabjhytzljjsqq", "K3KiwOY7nPVb3Wy2RCA10K8F9H");
       // insertTwoColumns(words,"key_words",c,"text,id",false);
       // insertTwoColumns(relations,"key_word_to_sentences",c,"key_word_id,sentence_id",true);
        insertTwoColumns(sentences,"sentences",c,"text,id",false);


    } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");

}

}
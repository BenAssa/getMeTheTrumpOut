import controllers.alchemy;
import models.AlchemyModel;
import models.REDB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ben on 10/19/16.
 */
public class add
{
static alchemy alcController= new alchemy();
static HashMap<String,String> sentences=new HashMap<String,String>();
static HashMap<String,String> words=new HashMap<String,String>();
static HashMap<String,String> relations=new HashMap<String,String>();

private static HashMap<String,String> loadWords(Connection c)
{
    HashMap<String, String> words2 = new HashMap<String, String>();
    String sql = "select * from key_words";

    Statement stmt = null;

    try
    {


        //Class.forName("org.postgresql.Driver");

        stmt = c.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next() == true)
        {
            words2.put(resultSet.getString("text"), resultSet.getString("id"));
        }
        return words2;


    } catch (Exception e)
    {

        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
    }
    System.exit(1);
return null;
}

private static HashMap<String,String> loadSentences(Connection c,int start, int end)
{
    HashMap<String, String> words2 = new HashMap<String, String>();
    String sql = "select * from sentences where id>start and id<end";

    Statement stmt = null;

    try
    {


        //Class.forName("org.postgresql.Driver");

        stmt = c.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next() == true)
        {
            words2.put(resultSet.getString("text"), resultSet.getString("id"));
        }
        return words2;


    } catch (Exception e)
    {

        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
    }
    System.exit(1);
    return null;
}


public static boolean insertIntoSentences(HashMap<String,String> map,String table,Connection c,String titles)
{
    Statement stmt = null;
    Set<String> keys=      map.keySet();
    for (String key : keys) {
        try
        {
            stmt = c.createStatement();
            String sql = "INSERT INTO "+table+" ("+titles+") ";
            sql+= "VALUES ('"+key.replaceAll("'","#")+"', "+map.get(key).replaceAll("'","#")+",4);";

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


static void procLine(String line,String id){
if   (line.length()<2)
    return;
    //res+=
    //res+=getWords(type.toLowerCase(),type + "GetRankedConcepts" ,target,"concepts");

if (false) return;

    String line2=line.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();

    String wordsArry[]=line2.split(" ");
    for (int i=0;i<wordsArry.length;i++)
    {
        String word= wordsArry[i];
        if (word.length()>2)
        {
            String wordId;
            if (words.containsKey(word))
                wordId = words.get(word);
            else
            {
                System.err.println("="+word);
                wordId = "" + words.size();

                words.put(word,wordId);
            }
            int value=word.length()+1;
            if (value>15)
                value=15;
            relations.put( wordId+","+ id,""+value);
        }
    }
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

                words.put(word,wordId);
            }
            int value=15;

            if (word.length()>8)
                value=30;
            relations.put( wordId+","+ id,""+value);
        }else
            System.err.println("=="+word);



    }

}

public static void runFromDB(){

    String t="Can you please adjust to the world wide web";
    procLine(t,"1")    ;





}
public static void main(String[] args)
{
    runFromDB();
    //if (true)
    //    return;
    FileInputStream fin;
    REDB.initilize(false);

    try {

        String sql="";

            Statement stmt = null;


            Connection c = REDB.c();
            stmt = c.createStatement();
            sql="select (select count(*) from key_words_tmp)-(select count(*) from key_words)";

        ResultSet resultSet=stmt.executeQuery(sql);
        resultSet.next();
        if (resultSet.getInt(0)>0)


            words=loadWords(REDB.c());


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
            String senId=""+sentences.size();
            sentences.put(line,senId);
            procLine(line,senId)    ;    };
    }
    // Close our input stream
    fin.close();

        insertTwoColumns(words,"key_words",REDB.c(),"text,id",false);
        insertTwoColumns(relations,"key_word_to_sentences",REDB.c(),"key_word_id,sentence_id,level",true);
        insertIntoSentences(sentences,"sentences",REDB.c(),"text,id,level");


    } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");

}

}
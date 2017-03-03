package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import akka.actor.FSM;
import play.*;
import play.mvc.*;
import play.libs.Json;
import play.libs.Json.*;
import play.data.Form;
import play.db.jpa.*;
import java.net.*;
import java.io.*;
import models.*;
import views.html.*;
import org.json.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;
import java.net.URLConnection;

import java.net.URL;
import java.io.*;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import models.REDB;

public class Poppy extends Controller
{

boolean initilized=false;

private void initilize()
{

    if (!initilized)
        try
        {

            REDB.initilize(false);

            initilized=true;
        }
        catch (Exception e){}


}
public Result getSentence()
{
    initilize();
    String sentence=request().body().asJson().findPath("sentence").textValue();

    String res= AlchemyModel.getWords("text","TextGetRankedKeywords" , "&text=" +URLEncoder.encode(sentence),"keywords","text");
    res+= AlchemyModel.getWords("text","TextGetRankedConcepts" , "&text=" +URLEncoder.encode(sentence),"concepts","text");
    res+= AlchemyModel.getWords("text","TextGetRankedNamedEntities" , "&text=" +URLEncoder.encode(sentence),"entities","text");
    res+= AlchemyModel.getWords("text","TextGetRankedTaxonomy" , "&text=" +URLEncoder.encode(sentence),"taxonomy","label");
    String[] ress=res.replaceAll("'","#").split("\n");
    String user="none-yet";
    String sqlGet="";
    String sqlPut="";
    try
    {

        Statement stmt = null;


        Connection c = REDB.c();
        stmt = c.createStatement();


        stmt = c.createStatement();

        sqlGet="(select s.text from sentences  s inner join key_word_to_sentences kts on kts.sentence_id=s.id inner join key_words kw";
        sqlGet+=" on kw.id=kts.key_word_id  where kw.text in ('all encompasing cathulu'";

    for (int i=0;i<ress.length;i++)
        sqlGet+=",'"+ress[i]+"'";
        sqlGet+=") group by s.text order by sum(kts.level)*sum(kts.level)*random() desc limit 1 ) union all\n" +
            " (select text  from sentences order by random() limit 1);";


            //Class.forName("org.postgresql.Driver");

             ResultSet resultSet=stmt.executeQuery(sqlGet);
                    resultSet.next();
            String sentenceOut=resultSet.getString(1).replaceAll("#","'");
if (resultSet.next())
{
    sqlPut="insert into calls(text,username,time_of,answer) values('"+sentence.replaceAll("'","#")+"','"+user+"',now(),'"+sentenceOut+"')";
        stmt.executeUpdate(sqlPut);


            return ok(sentenceOut+"\n\n\nSentence\n\n"+sqlGet );
    }
            else
{
    sqlPut="insert into calls(text,username,time_of,answer) values('"+sentence.replaceAll("'","#")+"','"+user+"',now(),'RANDOM:"+sentenceOut+"')";
    stmt.executeUpdate(sqlPut);
    return ok(sentenceOut + "\n\n\nRandom\n\n" + sqlGet);

}

}catch (Exception e) {
    e.printStackTrace();
    System.err.println(e.getClass().getName()+": "+e.getMessage());
    return ok("not ok at all, something is wrong \n the sql is \n "+sqlGet +"\n"+sqlPut);
}


    }


}



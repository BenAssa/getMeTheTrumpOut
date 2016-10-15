package controllers;

import java.util.List;

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
import javax.net.ssl.HttpsURLConnection;


public class EmployeeController extends Controller
{
String apikey = "kpbjvxm6z5zu94xza25nvvaa";

public Result getVin(String vin)
{
    // return ok(index.render("API REST for JAVA Play Framework"));
    //vin=2G1FC3D33C9165616
    String str = "lol";
    try
    {
        URL url = new URL("https://api.edmunds.com/api/vehicle/v2/vins/" + vin + "?fmt=json&api_key=" + apikey + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        str = reader.readLine();
        JSONObject obj = new JSONObject(str);
        int highway = obj.getJSONObject("MPG").getInt("highway");


        return ok("" + highway);


    } catch (Exception e)
    {
        return ok("Huston we have an issue " + str + "\n\n\n\n" + e.getMessage());
    }

}

//  " &url=www.ibm.com/us-en"
public Result getKeyWordsFromURL(String url)
{
    return getkeywords(url, " &url=" + "www.ibm.com/us-en");

}

public Result getKeyWordsFromText(String text)
{
    //String t = URLEncoder.encode(text);
    return getkeywords("Text", "&text=" + text);

}


public Result getkeywords(String type, String target)
{
    String items[] = {"Keywords", "Concepts"};
    // https://gateway-a.watsonplatform.net/calls/text/TextGetRankedConcepts?model=en-news&o
    // return ok(index.render("API REST for JAVA Play Framework"))
    String result = "";
    String res = "";
    String page = "";

    for (int i = 0; i < 2; i++)
    {
        String url = "https://gateway-a.watsonplatform.net/calls/" + type.toLowerCase() + "/" + type + "GetRanked" + items[i] + "?model=en-news&outputMode=json";
        url += target;
        url += "&apikey=0886d8f9911f566c0292a0953042eda953fa384d";

        try
        {
            res+="\n"+items[i]+"::::::\n";
            URL url2 = new URL(url);
            //"https://api.edmunds.com/v1/api/tco/usedtruecosttoownbystyleidandzip/101172637/90019?fmt=json&api_key=kpbjvxm6z5zu94xza25nvvaa\n");

            HttpsURLConnection con = (HttpsURLConnection) url2.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String line = in.readLine();
            int lines = 0;
            page = "";
            while (line != null && lines < 1000)
            {
                lines++;
                page += line;
                line = in.readLine();
            }           // BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

            JSONObject obj = new JSONObject(page);
            JSONArray words = obj.getJSONArray(items[i].toLowerCase());
            for (int j = 0; j < words.length(); j++)
            {
                res += "\n" + words.getJSONObject(j).getString("text");

            }


        } catch (Exception e)
        {
            return ok("API REST for JAVA Play Framework  " + page + "\n\n123\n\n" +res+"\n|\n"+ e.getMessage());
        }
    }
    return ok(res);


}
}

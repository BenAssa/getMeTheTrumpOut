package controllers;

import models.AlchemyModel;
import play.mvc.*;

import java.io.*;

import org.json.*;

import java.net.URL;


public class alchemy extends Controller
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
    return getkeywords(url, " &url=" + url);

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
    res+=AlchemyModel.getWords(type.toLowerCase(),type + "GetRankedKeywords" ,target,"keywords","text");
    res+= AlchemyModel.getWords(type.toLowerCase(),type + "GetRankedConcepts" ,target,"concepts","text");

    return ok(res);


}
}

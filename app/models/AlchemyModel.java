package models;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by ben on 10/21/16.
 */
public  class AlchemyModel
{

public static String getWords(String type,String wordType,String target, String name, String label)
{
    String res;//type + "GetRanked" + items[i]
    String url = "https://gateway-a.watsonplatform.net/calls/" + type + "/" + wordType + "?model=en-news&outputMode=json";
    url += target;
    url += "&apikey=d0999248af06b16f7078f57a60130ff451038e17";
    //"0886d8f9911f566c0292a0953042eda953fa384d";
    //f060d576eefdd97341bdeeb4bc4205a703e5f9a0
    try
    {
        String page = "";
        res="";
        URL url2 = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) url2.openConnection();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);
        String line = in.readLine();
        int lines = 0;
        while (line != null && lines < 1000)
        {
            lines++;
            page += line;
            line = in.readLine();
        }
System.err.println(page);
        JSONObject obj = new JSONObject(page);
        JSONArray words = obj.getJSONArray(name);
        for (int j = 0; j < words.length(); j++)
        {
            res += "\n" + words.getJSONObject(j).getString(label);

        }


    } catch (Exception e)
    {
        return "\nerror\n"+url+"\n";
    }
    return res;

}
}

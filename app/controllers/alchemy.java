package controllers;

import models.AlchemyModel;
import play.mvc.*;

import java.io.*;

import org.json.*;

import java.net.URL;


public class alchemy extends Controller
{


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

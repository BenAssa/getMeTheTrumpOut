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


public class EmployeeController extends Controller {
    String apikey="kpbjvxm6z5zu94xza25nvvaa";
    public Result getVin(String vin) {
       // return ok(index.render("API REST for JAVA Play Framework"));
        //vin=2G1FC3D33C9165616
        String str="lol";
        try {
    URL url = new URL("https://api.edmunds.com/api/vehicle/v2/vins/"+vin+"?fmt=json&api_key="+apikey+"\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

 str=reader.readLine();
    JSONObject obj = new JSONObject(str);
            int highway = obj.getJSONObject("MPG").getInt("highway");


        return ok(""+highway);



    }
    catch (Exception e ) {
        return ok("Huston we have an issue "+ str+"\n\n\n\n" +e.getMessage());
    }

    }

    public Result getMilesAndDestination(String zipmpg) {
        int zip=0;
        int mpg=1;
        String zip2="d";
        String crossing1="Eastport,ID|Poker+Creek,AK|point+roberts,WA|metaline+Falls,wa|roosville,Mt|raymond,MT|portal,ND|Dunseith,ND|Warrod,mn|grand+portage,MN|detroit,Mi|lewiston,NY";
        String crossing2="Mooers,ny|Alburg,Vt|pittsburg,NH|jackman,me|madawaska,me";
        try {
            zip2=zipmpg.split("X")[0];
            zip=Integer.parseInt(zipmpg.split("X")[0]);
            mpg=Integer.parseInt(zipmpg.split("X")[1]);

        }
        catch (Exception e ) {
            return forbidden(" failed we need zipXmpg , you gave us"+zipmpg +"   ||" +zip+" \n\n\n\n"+e.getMessage());
        }
        // return ok(index.render("API REST for JAVA Play Framework"));
       String url=" https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
        url+=zip;
        url+="&destinations="+crossing1+"|"+crossing2;
        url+="&key=AIzaSyA8W8_BJB_UPv87hTFU_PeXGMRiA3yx-6o";

        String str="";
        try {
            URL url2 = new URL(url);
                    //"https://api.edmunds.com/v1/api/tco/usedtruecosttoownbystyleidandzip/101172637/90019?fmt=json&api_key=kpbjvxm6z5zu94xza25nvvaa\n");

            HttpsURLConnection con = (HttpsURLConnection)url2.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            String line= in.readLine();
int lines=0;
            while (line!=null && lines<1000){
                lines++;
                str+=line;
                line=in.readLine();
            }           // BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

           JSONObject obj = new JSONObject(str);
            JSONArray destinations = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            String res="";
            int minMeters=40*1000*1000; //(circ of earth)
            String closestPont="";
            for (int i=0;i<destinations.length();i++)
            {
                int distance=destinations.getJSONObject(i).getJSONObject("distance").getInt("value");
if (distance<minMeters)
{
    minMeters=distance;


    closestPont=obj.getJSONArray("destination_addresses").getString(i);
}
            }


            int miles=minMeters/1609;
            String crossing_point= closestPont;
            int gallons=miles/mpg;
            double costPerGallon=2.5;
            int cost=(int)(gallons*costPerGallon);

            return ok("miles:"+miles+",gallons:"+gallons+",cost:"+cost+",crossing at:"+crossing_point);



        }
        catch (Exception e ) {
            return ok("API REST for JAVA Play Framework  "+ str+"\n\n\n\n" +e.getMessage());
        }
    }





}

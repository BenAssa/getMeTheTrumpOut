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

public class EmployeeController extends Controller {
    String apikey="kpbjvxm6z5zu94xza25nvvaa";
    public Result getVin(String vin) {
       // return ok(index.render("API REST for JAVA Play Framework"));

        String str="lol";
        try {
    URL url = new URL("https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key="+apikey+"\n");
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
        int mpg=0;
        String zip2="d";
        try {
            zip2=zipmpg.split("X")[0];
            zip=Integer.parseInt(zipmpg.split("X")[0]);
            mpg=Integer.parseInt(zipmpg.split("X")[1]);

        }
        catch (Exception e ) {
            return forbidden(" failed we need zipXmpg , you gave us"+zipmpg +"   ||" +zip+" \n\n\n\n"+e.getMessage());
        }
        // return ok(index.render("API REST for JAVA Play Framework"));
       String url=" httpts://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
        //url+=zip;
       // 90029&destinations=Point+Roberts|Eastport,ID&key=AIzaSyA8W8_BJB_UPv87hTFU_PeXGMRiA3yx-6o

        String str="lol";
        /*try {
            URL url2 = new URL("https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key="+apikey+"\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            str=reader.readLine();
            JSONObject obj = new JSONObject(str);
            int highway = obj.getJSONObject("MPG").getInt("highway");


            return ok(""+highway);



        }
        catch (Exception e ) {
            return ok("API REST for JAVA Play Framework  "+ str+"\n\n\n\n" +e.getMessage());
        }*/
        int miles=1000;
        String crossing_point= "eastpoint";
        int gallons=40;
        int cost=500;

        return ok("miles:"+miles+",gallons:"+gallons+",cost:"+cost+",crossing at:"+crossing_point);

    }



}

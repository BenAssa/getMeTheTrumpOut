import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ben on 10/19/16.
 */
public class Prepare
{

public static boolean insertIdAndText(HashMap<Integer,String> map,String table,Connection c)
{

    Statement stmt = null;

    Set<Integer> keys=      map.keySet();
    for (int key : keys) {
        try
        {


            stmt = c.createStatement();
            String sql = "INSERT INTO "+table+" (id,text) "
                    + "VALUES ("+key+", '"+map.get(key)+"');";
            stmt.executeUpdate(sql);
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return false;
        }


    }
    return true;


}
public static void main(String[] args)
{

    HashMap<Integer,String> sentences=new HashMap<Integer,String>();
    //for testing
    // sentences.put(1,"one");
    //sentences.put(2,"two");

    Connection c = null;
            try {
                Class.forName("org.postgresql.Driver");

                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://ec2-54-163-239-218.compute-1.amazonaws.com:5432/d80ffi19ifac15?sslmode=require",
                                "vabjhytzljjsqq", "K3KiwOY7nPVb3Wy2RCA10K8F9H");
                insertIdAndText(sentences,"sentences",c);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");

}

}

package models;
import java.util.HashMap;

/**
 * Created by ben on 11/23/16.
 */
public class TableDef
{
public int colNum;
static public class ColDef {
    int orderInFile;
    boolean isString;
    String nameInTable="";
    boolean nullable;
    boolean constant=false;
    String value;

    public  ColDef(boolean isStr,String name,boolean canBeNull)
    {
        nullable=canBeNull;
        isString=isStr;
        nameInTable=name;
    }


    public  ColDef(String name, String text)
    {
        constant=true;
        value=text;
        nameInTable=name.replaceAll("'", "''");
    }
}
public String targetTable;
public HashMap<Integer,ColDef> cols= new HashMap<Integer,ColDef>();


}

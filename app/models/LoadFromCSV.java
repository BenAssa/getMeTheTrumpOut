package models;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 11/22/16.
 */
public  class LoadFromCSV
{
private String firstLine;
TableDef tableDef;
Connection c;
List<String> problemRows;
List<String> rows;
String errorFirstLine="insert into bad_rows(row,linenum,error,date_of_error) values";

public LoadFromCSV(TableDef tableD,Connection conc,List<String> problemRow)
{
    rows= new ArrayList<String>();
    tableDef=tableD;
    problemRows=problemRow;
    c=conc;
}

void procLine(String line)
{
    String erer="";
    try
    {
        if (line.replaceAll(",","").length()==0)
        {
            return;

        }
        line=line.replaceAll("'", "''");
        List<String> cols = MyKongCSV.parseLine(line);
        if (cols.size()<tableDef.cols.size())
            return ;
        String row="(";
        for (int i=0; i<tableDef.colNum;i++)
        {
            if (tableDef.cols.get(i) != null)
            {
                if (tableDef.cols.get(i).constant)
                {
                    row = row + "'" + tableDef.cols.get(i).value + "',";
                    continue;
                }
                if (!tableDef.cols.get(i).nullable && getOrNull(cols.get(i), true).equals("null"))
                {

                    return;
                }
                erer=i+"   "+cols.get(i);

                if (!tableDef.cols.get(i).isString && !StringUtils.isNumericSpace(cols.get(i)))
                {

                    return;
                }

                row = row + getOrNull(cols.get(i), tableDef.cols.get(i).isString) + ",";
            }
        }

        row = row.substring(0, row.length() - 1) + ")";



        rows.add(row);


    } catch (Exception e)
    {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.err.println(erer+":"+line);


        problemRows.add("('"+line+"',0,'"+e.getClass().getName() + ": " + e.getMessage()+"',now())");

    }

}


public  String getOrNull(String str,boolean string)
{
    if (str.length()>0)
        if (string)
            return "'"+str+"'";
        else return
                str;
    return "null";
}

public  boolean insertRows(String start,List<String> rows, Connection c, Boolean verbose )
{
    Statement stmt = null;
    String sql = start;
    for (String row : rows)
        sql=sql+row+",";
    sql=sql.substring(0,sql.length()-1);
    if (verbose)
        System.out.println(sql);
    try
    {
        stmt = c.createStatement();
        stmt.executeUpdate(sql);

    }catch (Exception e) {
               e.printStackTrace();
        System.err.println(sql+"\n"+e.getClass().getName()+": "+e.getMessage());

        return false;
    }
    return true;
}



public  void run(String file)
{
    int goodrows=0;
    int badrows=0;
    firstLine="INSERT INTO "+tableDef.targetTable+"(";
    for (int i=0; i<tableDef.colNum;i++)
        if (tableDef.cols.get(i) != null)
            firstLine=firstLine+tableDef.cols.get(i).nameInTable+",";
    firstLine=firstLine.substring(0,firstLine.length()-1);
    firstLine=firstLine+") values ";

    FileInputStream fin;
    System.out.println("proccessing");
    if (false)
        return;
    try {

        fin = new FileInputStream(file);
        System.out.println("file loaded");

        try (InputStream is = fin)
        {
            BufferedReader lines = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //moving the pointer a line forward
            String line;
            lines.readLine();
            int i = 0;
            while (true)
            {
                i++;
                line = lines.readLine();
                if (line == null)
                    break;
                procLine( line);
                if (rows.size() >49 )
                {
                    System.out.println(""+i+"    "+rows.size() );
                    goodrows=goodrows+rows.size();
                    insertRows(firstLine,rows, c,false);
                    rows = new ArrayList<String>();
                }

            }
        }
        badrows=badrows+problemRows.size();
        goodrows=goodrows+rows.size();
        if (rows.size() > 0)
            insertRows(firstLine,rows, c,true);
         // Close our input stream
        fin.close();

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
    }
    System.out.println("Opened database successfully ");
    System.out.println("rows loaded:"+goodrows);
    System.out.println("rows failed:"+badrows);


}

}


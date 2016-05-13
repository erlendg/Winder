package com.eim.winder.unusedcomponents;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Mari on 13.05.2016.
 * Makes a runnable .sql file with create table and inserts for the Locations table
 * to prepopulate it with data from yr.no
 */
public class LocationSQLMaker {
    public static void main(String[] args) throws Exception{
        String filein = "C:/Users/Mari/AndroidStudioProjects/noreg.txt";
        String fileout = "C:/Users/Mari/AndroidStudioProjects/noreg.sql";

        FileInputStream fstream = new FileInputStream(filein);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        PrintWriter out = new PrintWriter(fileout);

        String strLine;
        String ut;

        out.println("create table locations(id integer primary key autoincrement, name text, type text, municipality text, county text, xmlurl text);");

        br.readLine();
        while ((strLine = br.readLine()) != null)   {
            if(strLine.contains("'")){
                strLine = strLine.replace("'", "\\'");
                //System.out.println("Apostrof i linje: " + strLine);
            } else {
                String[] cd = strLine.split("\t", -1);
                System.out.println (cd[1] + ", " + cd[6] + ", " + cd[7] +", " +cd[cd.length-2]);
                ut = "insert into locations (name, type, municipality, county, xmlurl) values(\'";
                ut += cd[1] + "\', \'"+ cd[4] + "\', \'"  + cd[6] + "\', \'" + cd[7] +"\', \'" +cd[cd.length-2] + "\');";
                out.println(ut);
            }
        }
        out.close();
        br.close();
    }
}

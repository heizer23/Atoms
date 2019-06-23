package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import app.lerner2.projects.my.lerner4.Data.DatabaseBase;
import app.lerner2.projects.my.lerner4.MySingleton;


public class DatabaseHelper extends DatabaseBase {

    public static final String KEY_NEXT = "Next";
    public static final String TABLE_LINKS = "links";
    public static final String KEY_ERST = "erst";
    public static final String KEY_ZWEIT = "zweit";


    protected Activity act;
    protected Context ourContext;




    public DatabaseHelper(Context c, Activity act) {
        super(c, act);
        ourContext = c;
        this.act = act;
       // res.getString(R.string.PathCrashReport)

    }

    public void linkItems(int erst, int zweit){
        open();
        ContentValues values = new ContentValues();
        values.put("erst", erst);
        values.put("zweit", zweit);
        addItem("links", values);
        close();
    }

    public int[] getLinks(int erst){

        int[] dataResult;
        ArrayList<Integer[]> dataArr = new ArrayList<>();
        String[] auswahl = {KEY_ERST, KEY_ZWEIT, KEY_NEXT};

        open();

        Cursor c = mySQLDB.query(TABLE_LINKS, auswahl, "erst = " + erst, null, null, null, null, null);

        int iErst = c.getColumnIndex(KEY_ERST);
        int iZweit = c.getColumnIndex(KEY_ZWEIT);
        int iNext = c.getColumnIndex(KEY_NEXT);


        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Integer[] data = new Integer[3];
                    data[0] = c.getInt(iErst);
                    data[1] = c.getInt(iZweit);
                    data[2] = c.getInt(iNext);
                    dataArr.add(data);
                } while (c.moveToNext());
            }
        }
        dataResult = new int[dataArr.size()];
        for(int i=0;i<dataArr.size();i++){
            dataResult[i] = (int)dataArr.get(i)[1];
        }

        close();
        return dataResult;
    }


}


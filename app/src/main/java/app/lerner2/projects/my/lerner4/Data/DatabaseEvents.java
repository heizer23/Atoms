package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import app.lerner2.projects.my.lerner4.MySingleton;

public class DatabaseEvents extends DatabaseHelper {

    static final String TABLE_EVENTS = "Events";

    public static final String KEY_DATUM = "Datum";
    public static final String KEY_ITEM = "Item";
    public static final String KEY_INFO = "Info";

    public static final String KEY_NEXT = "Next";
    public static final String KEY_COUNTER = "Counter";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ORT = "Ort";
    public static final String KEY_LASTDATE = "LastDate";

    String[] columnsLektion = { KEY_ROWID, KEY_NEXT, KEY_SCORE, KEY_DATUM, KEY_ITEM, KEY_COUNTER,  KEY_ORT};

    public DatabaseEvents(Context c, Activity act) {
        super(c, act);
    }

    public int getCount(){
        int count;
        String[] column = {"_id"};
        Cursor c = mySQLDB.query(TABLE_EVENTS, column, "next>0" ,
                null, null, null, null, null);
        count = c.getCount();
        c.close();
        return count;
    }

    public void saveResults(int id, double score, long next, long last, int counter){

        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        values.put(KEY_LASTDATE, longTemp);
        values.put(KEY_NEXT, next);
        values.put(KEY_COUNTER, counter);
        values.put(KEY_LASTDATE, last);
        update(TABLE_EVENTS, id, values);
    }

    public void saveOrt(int id, String ort){
        open();
        ContentValues values = new ContentValues();
        values.put(KEY_ORT, ort);
        update(TABLE_EVENTS,id, values);
        close();
    }

    public int nextFrage(){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;
        // todo auswahl über rounds
        // select fragenId, (timestamp+vorschub) as nextTime from rounds where nextTime < 1562461490 order by nextTime desc

        String sqlString = String.format("Select _id from Events where next < %d order by next desc", longTemp);
        Cursor cursor = mySQLDB.rawQuery(sqlString, null);
        int iFragenId = cursor.getColumnIndex("_id");
        cursor.moveToFirst();
        int fragenId = cursor.getInt(iFragenId);
        return fragenId;
    }

    public int[] getCountStats(){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;
        String sqlQuery= "SELECT count(item)as Anzahl FROM events where counter >0 union all SELECT count(item) FROM events where next > %d";
        sqlQuery = String.format(sqlQuery, longTemp);
        return getIntsFromSQL(sqlQuery);
    }

    public Cursor getNextEvents(){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;
        String sqlString = String.format("Select _id, Item, Next from Events where next > %d order by next asc", longTemp);
        Cursor cursor = mySQLDB.rawQuery(sqlString, null);
        return cursor;

    }

    public int[] getRundenInfo(){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;

        String sqlString = "SELECT count(_id) from events where counter > 0 and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d and next < %d \n" +
                "union all \n" +
                "SELECT count(_id) from events where next > %d ";

        sqlString = String.format(sqlString, longTemp,
                longTemp, longTemp+60,
                longTemp+60, longTemp+60*60,
                longTemp+60*60, longTemp+60*60*24,
                longTemp+60*60*24, longTemp+60*60*24*30,
                longTemp+60*60*24*30, longTemp+60*60*24*365,
                longTemp+60*60*24*365);

        return getIntsFromSQL(sqlString);
    }

    public String[] getFrageInfos(int fragenId){
        String[] fragenFelder = {"_id", "Item", "Datum", "Next", "Score", "Counter", "Ort"};
        Cursor c = mySQLDB.query(TABLE_EVENTS, columnsLektion, "_id = "+ fragenId, null,
                null, null, null);
        String[] results = getFirstOfCursor(c, fragenFelder);
        return results;
    }

    public String[] getItemSQL(String whereSQL, String order) {
        String[] data = null;
        int next;
        int nextDifference = 0;
        int count = 0;
        try {
            Log.i("DbBase getPoints", "neue Frage1");
            Cursor c = mySQLDB.query(TABLE_EVENTS,columnsLektion, whereSQL ,null, null, null, order, "40");
            int iRow = c.getColumnIndex(KEY_ROWID);
            int iItem = c.getColumnIndex(KEY_ITEM);
            int iDate = c.getColumnIndex(KEY_DATUM);
            int iScore = c.getColumnIndex(KEY_SCORE);
            int iNext = c.getColumnIndex(KEY_NEXT);
            int iCounter = c.getColumnIndex(KEY_COUNTER);
            int iOrt = c.getColumnIndex(KEY_ORT);
            count = c.getCount();

            if(count==0){
                String sqlSetBack = "update Events set Next = 1 WHERE _id IN (SELECT _id FROM Events WHERE Next <= 0 ORDER BY Score desc LIMIT 6)";
                mySQLDB.execSQL(sqlSetBack);

                c = mySQLDB.query(TABLE_EVENTS,columnsLektion, "Next >0" ,null, null, null, "next", "40");
                MySingleton.getInstance().addAktiviert(6);
            }


            c.moveToFirst();
            data = new String[7];
            data[0] = c.getString(iRow);
            data[1] = c.getString(iItem);
            data[2] = c.getString(iDate);
            data[3] = c.getString(iNext);
            data[4] = c.getString(iScore);
            data[5] = c.getString(iCounter);
            data[6] = c.getString(iOrt);
            next= c.getInt(iNext);

            c.moveToLast();
            int lastNext =c.getInt(iNext);
            nextDifference = lastNext- next;
            c.close();

            //  arrangeQuestions(count, nextDifference, next);

        } catch (Exception e) {
            String error = e.toString();
            Log.i("DbBase getPoints", error);
        }
        return data;
    }

    public Cursor getCursor( String whereSQL, String order) {
        return super.getCursor(TABLE_EVENTS, columnsLektion, whereSQL, order);
    }

    public void newTable(String dateiNamein) {
        final String dateiName = dateiNamein;


        Runnable runnable = new Runnable() {
            public void run() {
                Time now = new Time();
                now.setToNow();
                double timeStamp = now.toMillis(true) / 1000;
                open();
                String tableId = TABLE_EVENTS;
                Vector<String[]> datenhalter = new Vector<String[]>();
                try {
                    File f = new File(Environment.getExternalStorageDirectory()
                            + "/Quizzer/Lektionen/" + dateiName);
                    BufferedReader bf = new BufferedReader(
                            new InputStreamReader(new FileInputStream(f),
                                    "UTF-8"));

                    String line = "";
                    while ((line = bf.readLine()) != null) {
                        String[] splittedLine2 = line.split("::");
                        datenhalter.add(splittedLine2);
                    }
                    bf.close();


                    for (int j = 0; j < datenhalter.size(); j++) {
                        ContentValues initialValues = new ContentValues();
                        initialValues.put(KEY_DATUM, datenhalter.get(j)[0].trim());
                        initialValues.put(KEY_ITEM, datenhalter.get(j)[3].trim());
                        initialValues.put(KEY_INFO, datenhalter.get(j)[2].trim());
                        initialValues.put(KEY_ORT, datenhalter.get(j)[2].trim());
                        initialValues.put(KEY_NEXT, 0);
                        initialValues.put(KEY_COUNTER, -timeStamp);
                        initialValues.put(KEY_SCORE, 0);
                        try {
                            initialValues.put(KEY_ORT, datenhalter.get(j)[1].trim());
                        } catch (Exception e) {
                        }
                        mySQLDB.insert(tableId, null, initialValues);
                    }
                } catch (Exception e) {
                    String dialogText = e.toString();
                    Log.i("DatDb newTable", dialogText);
                }
                close();
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    public int[] getCompares(String relation, int Datum){
        String order;
        if(relation.equals("<")){
            order = "desc";
        }else{
            order = "asc";
        }
        String sqlQuery= "select * from Events where Datum %s %d order by Datum %s limit 2";
        sqlQuery = String.format(sqlQuery, relation, Datum, order);
        return getIntsFromSQL(sqlQuery);
    }
}

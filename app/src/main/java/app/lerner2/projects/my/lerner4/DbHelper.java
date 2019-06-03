package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;


public class DbHelper extends DatabaseBase {

    public static final String KEY_DATUM = "Datum";
    public static final String KEY_ITEM = "Item";
    public static final String KEY_INFO = "Info";

    public static final String KEY_NEXT = "Next";
    public static final String KEY_COUNTER = "Counter";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ORT = "Ort";
    public static final String KEY_LASTDATE = "LastDate";
    public static final String TABLE_LINKS = "links";
    public static final String KEY_ERST = "erst";
    public static final String KEY_ZWEIT = "zweit";


    protected Activity act;
    protected Context ourContext;

    String[] columnsLektion = { KEY_ROWID, KEY_NEXT, KEY_SCORE, KEY_DATUM, KEY_ITEM, KEY_COUNTER,  KEY_ORT};


    public DbHelper(Context c, Activity act) {
        super(c, act);
        ourContext = c;
        this.act = act;
       // res.getString(R.string.PathCrashReport)

    }

    public Cursor getCursor(String whereSQL, String order) {
        Cursor c = mySQLDB.query(TABLE_EVENTS, columnsLektion, whereSQL, null,
                null, null, order);
        return c;
    }



    public int getCount(){
        int count;
        String[] column = {"_id"};
        Cursor c = mySQLDB.query(TABLE_EVENTS, column, "next>0" ,null, null, null, null, null);
        count = c.getCount();
        c.close();
        return count;
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

    public int saveResultsAlt(int id, double score, long next, int counter){


        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true);

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        values.put(KEY_LASTDATE, longTemp);
        values.put(KEY_NEXT, next);
        values.put(KEY_COUNTER, counter);
        update(id, values);

        open();
        Cursor c = mySQLDB.rawQuery("Select * from events where Next > 0 AND Next < " + next , null);
        int position = (1+c.getCount());
        close();
        return position;
    }



    public void saveResults(int id, double score, long vorschub, int counter){
      open();

        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true);

        ContentValues values = new ContentValues();
        values.put("fragenId", id);
        values.put("Score", score);
        values.put("nextTime", longTemp+(vorschub));
        values.put("lastTime", longTemp);
      //  values.put("counter", counter);
        long nextTime = longTemp+(vorschub*1000);
        saveResultsAlt(id, score, nextTime, counter);
      //  addRunde(values);
    }

    public void putInQuestions(){
        int[] ids = {24, 125, 93, 150, 264, 266};

        for (int i = 0; i < ids.length; i++) {

            saveResults(ids[i], 1, 20*i,1);

        }

        ids[0] = 2;

    }


    public int nextFrage(){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true);

        open();

        String sqlString = String.format("Select _id from Events where next < %d order by next desc", longTemp);
        Cursor cursor = mySQLDB.rawQuery(sqlString, null);
        int iFragenId = cursor.getColumnIndex("_id");
        cursor.moveToFirst();
        int fragenId = cursor.getInt(iFragenId);
        close();
        return fragenId;
    }


    public void saveOrt(int id, String ort){
        open();
        ContentValues values = new ContentValues();
        values.put(KEY_ORT, ort);
        update(id, values);
        close();
    }


    public void update(int id, ContentValues values) {
        ourHelper = new DbHelper(ourContext);
        mySQLDB = ourHelper.getWritableDatabase();
        mySQLDB.update(TABLE_EVENTS, values, KEY_ROWID + "=" + id, null);
        mySQLDB.close();
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

    public void runSQL(String sqlString){
        try {
            mySQLDB.execSQL(sqlString);
        } catch (SQLException e) {
            String fehler = e.toString();
            e.printStackTrace();
        }
    }

    public void linkItems(int erst, int zweit){
        open();
        ContentValues values = new ContentValues();
        values.put("erst", erst);
        values.put("zweit", zweit);
        addItem("links", values);
        close();
    }

    public void addRunde( ContentValues values){
        open();
        addItem("runden", values);
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

    public Cursor getJointCursor(String orderBy){
        String selectQuery = "SELECT * FROM Events JOIN runden ON  Events.[_id] = runden.fragenId order by " + orderBy ;
        Cursor cursor = mySQLDB.rawQuery(selectQuery, null);
        return cursor;
    }



}


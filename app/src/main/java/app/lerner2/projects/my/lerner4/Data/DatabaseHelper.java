package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;


public class DatabaseHelper extends DatabaseBase {

    public static final String KEY_NEXT = "Next";
    public static final String TABLE_LINKS = "links";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_DEST = "dest";
    public static final String KEY_VERBINDUNG = "verbindung";
    public static final String KEY_LINK = "link";
    public static final String KEY_COUNTER= "counter";

    protected Activity act;
    protected Context ourContext;




    public DatabaseHelper(Context c, Activity act) {
        super(c, act);
        ourContext = c;
        this.act = act;
       // res.getString(R.string.PathCrashReport)

    }

    public void linkItems(int source, int dest){
        open();
        ContentValues values = new ContentValues();
        values.put("source", source);
        values.put("dest", dest);
        addItem("links", values);
        close();
    }

    public void linkItemsPlus(int source, int dest, String verbindung, String link){
        open();
        ContentValues values = new ContentValues();
        values.put(KEY_SOURCE, source);
        values.put(KEY_DEST, dest);
        values.put(KEY_VERBINDUNG, verbindung);
        values.put(KEY_LINK, link);
        addItem("links", values);
        close();
    }

    public int[] getLinkIdsForQuestion(int questionId){
        int result[];
        String sql = "select dest as id from links where source = %d " +
                " UNION " +
                "select source as id from links where dest = %d";

        sql = String.format(sql, questionId, questionId);
        open();
        result = getIntsFromSQL(sql);
        close();
        return result;
    }

    public void deleteDeletedLinksHack(){
        String sql = "SELECT l._id\n" +
                "  FROM links l\n" +
                " WHERE NOT EXISTS (SELECT NULL \n" +
                "                     FROM Events e\n" +
                "                    WHERE l.source = e._id)\n" +
                "union\n" +
                "SELECT l._id\n" +
                "  FROM links l\n" +
                " WHERE NOT EXISTS (SELECT NULL \n" +
                "                     FROM Events e\n" +
                "                    WHERE l.dest = e._id)";

        int[] ids = getIntsFromSQL(sql);

        for(int id : ids){
            deleteLinks(id);
        }

    }

    private void deleteLinks(int id){
        String sql = "DELETE FROM links where _id = %d";
        sql = String.format(sql, id);
        runSQL(sql);
    }


    public int[] getLinks(int source){

        int[] dataResult;
        ArrayList<Integer[]> dataArr = new ArrayList<>();
        String[] auswahl = {KEY_SOURCE, KEY_DEST};

        open();

        Cursor c = mySQLDB.query(TABLE_LINKS, auswahl, "source = " + source, null, null, null, null, null);

        int iSource = c.getColumnIndex(KEY_SOURCE);
        int iDest = c.getColumnIndex(KEY_DEST);


        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Integer[] data = new Integer[3];
                    data[0] = c.getInt(iSource);
                    data[1] = c.getInt(iDest);
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


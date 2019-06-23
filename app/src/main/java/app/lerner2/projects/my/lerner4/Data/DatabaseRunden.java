package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.Time;

import app.lerner2.projects.my.lerner4.MySingleton;

public class DatabaseRunden extends DatabaseHelper {

    public static final String TABLE_RUNDEN = "rounds";

    public static final String KEY_FRAGENID = "fragenId";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_VORSCHUB = "vorschub";
    public static final String KEY_FACTOR_LINEAR = "factorLin";
    public static final String KEY_FACTOR_EXP = "factorExp";
    public static final String KEY_SCORE = "score";


    public DatabaseRunden(Context c, Activity act) {
        super(c, act);
        ourContext = c;
        this.act = act;
    }

    public void addRound(int fragenId, long vorschub, double score){
        Time now = new Time();
        now.setToNow();
        long longTemp = now.toMillis(true)/1000;

        ContentValues values = new ContentValues();
        values.put(KEY_FRAGENID, fragenId);
        values.put(KEY_TIMESTAMP, longTemp);
        values.put(KEY_SCORE, score);
        values.put(KEY_VORSCHUB, vorschub);
        values.put(KEY_FACTOR_LINEAR, MySingleton.getInstance().getVorschubLin());
        values.put(KEY_FACTOR_EXP, MySingleton.getInstance().getVorschubExp());
        values.put(KEY_SCORE, score);
        addItem(TABLE_RUNDEN, values);
    }

}

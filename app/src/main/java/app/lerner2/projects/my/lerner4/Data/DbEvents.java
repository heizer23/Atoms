package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.Context;

public class DbEvents extends DbHelper {

    static final String TABLE_EVENTS = "Events";

    public static final String KEY_DATUM = "Datum";
    public static final String KEY_ITEM = "Item";
    public static final String KEY_INFO = "Info";

    public static final String KEY_NEXT = "Next";
    public static final String KEY_COUNTER = "Counter";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ORT = "Ort";
    public static final String KEY_LASTDATE = "LastDate";


    public DbEvents(Context c, Activity act) {
        super(c, act);
    }
}

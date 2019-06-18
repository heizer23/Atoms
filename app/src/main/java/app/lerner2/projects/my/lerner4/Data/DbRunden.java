package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

public class DbRunden extends DbHelper {

    public DbRunden(Context c, Activity act) {
        super(c, act);
        ourContext = c;
        this.act = act;
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


}

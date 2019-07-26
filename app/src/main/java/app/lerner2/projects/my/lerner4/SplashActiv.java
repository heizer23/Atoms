package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;
import app.lerner2.projects.my.lerner4.Data.DatabaseHelper;


public class SplashActiv extends Activity {
    private Context ourContext;
    private Activity act = this;
    private SharedPreferences myPrefs;
    private Resources res;
    private int reset = 0;   //    true = 1
    private final int activityId = 1; // 1 =standard -- 2 = sonder
    // 1: MainActivity
    // 2: Overview

    @Override
    protected void onCreate(Bundle TravisLovesBacon) {

        super.onCreate(TravisLovesBacon);
        res = getResources();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        ourContext = this.getBaseContext();
        myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);




        if (reset==1)
            Toast.makeText(getApplicationContext(), "RESET", Toast.LENGTH_LONG)
                    .show();

        Thread timer = new Thread() {
            public void run() {
                DatabaseEvents dbEvents = new DatabaseEvents(ourContext, act);
                dbEvents.checkDB();
                if (myPrefs.getBoolean("firstStart", true) || reset==1) {

                    dbEvents.createNewDataBase();
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putBoolean("firstStart", false);
                    prefsEditor.commit();
                   // dbEvents.checkDB();
                }
                switch (activityId) {
                    case 1:
                        //todo mach das besser
                        DatabaseHelper dbh = new DatabaseHelper(ourContext, act);
                        dbh.deleteDeletedLinksHack();

                      //  dbEvents.checkDB();
                        dbEvents.open();
                        MySingleton.getInstance().setCount(dbEvents.getCount());

                        DatabaseEvents dbe = new DatabaseEvents(ourContext, act);

                        dbe.open();
                        int temp = dbe.getCount();
                        dbEvents.close();
                        Intent openLerner1 = new Intent("lerner.lerner.QUIZZER");
                        startActivity(openLerner1);
                        break;
                    case 2:
                        dbEvents.open();
                        Cursor cursor = dbEvents.getCursor("_id < 10 ", "_id");
                        String[] fields = {"_id", "Item"};
                        String[][] data = dbEvents.getAllOfCursor(cursor,fields);
                        int nla = 2;
                        break;

                }

            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

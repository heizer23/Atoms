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

import app.lerner2.projects.my.lerner4.Data.DbHelper;


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
                DbHelper dbHelper = new DbHelper(ourContext, act);
                dbHelper.checkDB();
                if (myPrefs.getBoolean("firstStart", true) || reset==1) {

                    dbHelper.createNewDataBase();
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putBoolean("firstStart", false);
                    prefsEditor.commit();
                   // dbHelper.checkDB();
                }
                switch (activityId) {
                    case 1:
                      //  dbHelper.checkDB();
                        dbHelper.open();
                        MySingleton.getInstance().setCount(dbHelper.getCount());
                        dbHelper.close();
                        Intent openLerner1 = new Intent("lerner.lerner.QUIZZER");
                        startActivity(openLerner1);
                        break;
                    case 2:
                        dbHelper.open();
                        Cursor cursor = dbHelper.getCursor("_id < 10 ", "_id");
                        String[] fields = {"_id", "Item"};
                        String[][] data = dbHelper.getAllOfCursor(cursor,fields);
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

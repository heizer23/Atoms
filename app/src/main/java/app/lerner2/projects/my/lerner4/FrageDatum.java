package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

import app.lerner2.projects.my.lerner4.Data.DbHelper;

public class FrageDatum {

    private DbHelper dbHelper;

    private int id;
    private String item;
    private int datum;
    private Long next;
    private double score;
    private int counter;
    private String status = "unbeantwortet";


    public FrageDatum(Context c, Activity act) {

        dbHelper = new DbHelper(c,act);
        dbHelper.open();
        id = dbHelper.nextFrage();
        String[] values = dbHelper.getFrageInfos(id);
        dbHelper.close();

        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);

    }

    public void calcResults(boolean richtig){
        MathStuff Ms = new MathStuff();
        long vorschub;
        if(richtig){
            status = "richtig";
            score = score+1;
        }else{
            status = "falsch";
            score = 0;
        }
        counter = counter+1;
        vorschub =  Ms.getVorschub(score, counter);

        Time now = new Time();
        now.setToNow();
        long thisTime = now.toMillis(true)/1000;
        next = thisTime + vorschub;
        dbHelper.saveResults(id, score, next,thisTime, counter);
    }


    public int getDatum() {
        return datum;
    }

    public String getItem(){
        return item;
    }

    public int getId(){
        return id;
    }

    public double getScore() {
        return score;
    }

    public int getCounter() {
        return counter;
    }

    public Long getNext() {
        return next;
    }

    public String getStatus() {
        return status;
    }
}

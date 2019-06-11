package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

import java.io.UnsupportedEncodingException;

import app.lerner2.projects.my.lerner4.Data.DbHelper;

public class FrageDatum {

    private DbHelper dbHelper;

    private int id;
    private String item;
    private int datum;
    private Long next;
    private double score;
    private int counter;
    private String url;
    private String status = "unbeantwortet";
    private int[] metaStats;
    private String feedbackString;
    private String frageModus = "compare";
    public Logic logic;


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
        url = values[6];
        if(frageModus.equals("compare")){
            logic = new LogicCompare(this);
        }else{
            logic = new LogicMC10(this);
        }
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

    public String getFrageModus() {
        return frageModus;
    }

    public int[] getMetaStats() {
        dbHelper.open();
        metaStats = dbHelper.getCountStats();
        dbHelper.close();
        return metaStats;
    }

    public String[] getUrl() {
        String[] result = new String[2];
        if(url ==null){
            url = item.replace(" ", "%");
            url = "https://www.google.com/search?q=" + url;
        }

        result[0] = ""+id;
        result[1] = url;
        return result;
    }

    public String getFeedbackString() {
        return feedbackString;
    }

    public void setFeedbackString(String feedbackString) {
        this.feedbackString = feedbackString;
    }
}

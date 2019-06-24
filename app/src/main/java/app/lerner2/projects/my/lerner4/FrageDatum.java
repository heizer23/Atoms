package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;
import app.lerner2.projects.my.lerner4.Data.DatabaseRunden;

public class FrageDatum {

    private DatabaseEvents dbEvents;
    private DatabaseRunden dbRunden;
    private Context c;
    private Activity act;
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


    public FrageDatum(Context c, Activity act, Integer idFrage) {
        this.c = c;
        this.act = act;
        dbEvents = new DatabaseEvents(c,act);
        dbRunden = new DatabaseRunden(c, act);
        dbEvents.open();
        if (idFrage == null){
            this.id = dbEvents.nextFrage();
        }else{
            this.id = idFrage;
        }
        String[] values = dbEvents.getFrageInfos(id);
        dbEvents.close();

        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);
        url = values[6];
        if(counter >6 && score ==-1){
        //if(true){
            frageModus= "compare";
            logic = new LogicCompare(this);
        }else{
            frageModus= "multipleChoice";
            logic = new LogicMC10(this);
        }
    }

    public FrageDatum(int id, FrageDatum parentFrage) {
        dbEvents = new DatabaseEvents(parentFrage.getC(),parentFrage.getAct());
        dbEvents.open();
        String[] values = dbEvents.getFrageInfos(id);
        dbEvents.close();

        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);
        url = values[6];
        frageModus = "supplement";
    }

    public List<Integer> getComparableIds(){
        List<Integer> results = new ArrayList<>();
        int[] temp;
        dbEvents.open();
        temp = dbEvents.getCompares("<", datum);
        for(int i = 0; i< temp.length; i++){
            results.add(temp[i]);
        }
        temp = dbEvents.getCompares(">", datum);
        for(int i = 0; i< temp.length; i++){
            results.add(temp[i]);
        }
        dbEvents.close();
        Collections.shuffle(results);
        return results;
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
        dbEvents.saveResults(id, score, next,thisTime, url, counter);
        dbRunden.addRound(id, vorschub,score);
    }

    public void saveInfos(){
        dbEvents.saveInfos(id, score, item, datum, url);
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

    public String getFrageModus() {
        return frageModus;
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

    public void setUrl(String url) {
        dbEvents.saveOrt(id, url);
        this.url = url;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setDatum(int datum) {
        this.datum = datum;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Context getC() {
        return c;
    }

    public Activity getAct() {
        return act;
    }
}

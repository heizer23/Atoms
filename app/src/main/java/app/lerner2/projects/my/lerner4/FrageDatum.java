package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.Context;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;
import app.lerner2.projects.my.lerner4.Data.DatabaseHelper;
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
    private long lastDate;
    private long deltaNextLastdate;
    private String status = "unbeantwortet";
    private boolean randomId = false;
    private int[] metaStats;
    private String feedbackString;
    private String frageModus = "compare";
    private int[] actHistogram;
    public Logic logic;


    public FrageDatum(Context c, Activity act, Integer idFrage) {
        this.c = c;
        this.act = act;
        dbEvents = new DatabaseEvents(c,act);
        dbRunden = new DatabaseRunden(c, act);
        actHistogram  = dbEvents.getRundenInfo();
        dbEvents.open();
        // wird eine speziefische Frage aufgerufen bsp von ItemViewAct?
        if (idFrage == null){
            double randDouble = Math.random();
            randomId = (actHistogram[0]<1) && (randDouble > 0.8);
            this.id = dbEvents.nextFrage(randomId);
        }else{
            this.id = idFrage;
        }
        String[] values = dbEvents.getFrageInfos(id);
        dbEvents.close();

        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        String temp = values[5];
        counter = Integer.parseInt(temp);
        url = values[6];
        if(counter >6 && score ==-1){
        //if(true){
            frageModus= "compare";
            logic = new LogicCompare(this);
        }else{
            frageModus= "multipleChoice";
            logic = new LogicMC10(this);
        }
        lastDate = Long.parseLong(values[7]);
       // deltaNextLastdate = next - lastDate;
        deltaNextLastdate = MySingleton.getInstance().getNow()- lastDate;

    }

    public FrageDatum(int id, FrageDatum parentFrage) {
        //todo brauche ich hier parentFrage?
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

    public int getLinkedQuestion(){
        int[] idArray;
        int result =-1;
        DatabaseHelper dbHelper = new DatabaseHelper(c,act);
        idArray = dbHelper.getLinkIdsForQuestion(id);
        if(idArray.length>0){
            result = idArray[0];
        }
        return result;
    }

    public void calcResults(boolean richtig){
        Time time = new Time();
        time.setToNow();
        long now = time.toMillis(true)/1000;
        long delta = 0;
        MathStuff Ms = new MathStuff();
        long vorschub;
        if(richtig){
            if(!randomId){
                 score = score+1;
            }

            if(counter == 0){
                vorschub = 60*60;
                delta = vorschub;
            }else{
                if(randomId){
                    vorschub = next - lastDate;
                }else{
                    double wissensQuotient = score+((score+1)/(counter+5));
                    long timeDelta = now - lastDate;
                    vorschub = timeDelta+ (long)score* MySingleton.getInstance().getVorschubLin();
                }
                delta = vorschub;
            }
        }else{
            score = 0;
            if(counter == 0){
                delta = 0;
            }else{
                delta = -(now - lastDate);
            }
            vorschub = 0;
        }
        counter = counter+1;

        if(status.equals("unbeantwortet")){
            next = now + vorschub;
        }else if(status.equals("eingeschoben")){
           // next = next;
            vorschub = -vorschub;
        }

        MySingleton.getInstance().setDelta(delta);
        dbEvents.saveResults(id, score, next,now, url, counter);
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

    public int[] getActHistogram() {
        return actHistogram;
    }

    public boolean isRandomId() {
        return randomId;
    }

    public long getDeltaNextLastdate() {
        return deltaNextLastdate;
    }
}

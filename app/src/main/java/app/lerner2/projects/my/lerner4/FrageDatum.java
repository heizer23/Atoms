package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.Context;

public class FrageDatum {

    private DbHelper dbHelper;

    private int id;
    private String item;
    private int datum;
    private Long next;
    private double score;
    private int counter;



    public FrageDatum(int id, Context c, Activity act) {
        dbHelper = new DbHelper(c,act);
        dbHelper.open();
        String[] values = dbHelper.getFrageInfos(id);
        dbHelper.close();

        this.id =  id;
        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);

    }

    public double[] calcResults(double save){
        MathStuff Ms = new MathStuff();
        int position;
        long vorschub =  Ms.getVorschub(score, counter);
        position = 1;

        if(score<0)score = 0;
        dbHelper.saveResults(id, score, vorschub, counter);


        // MySingleton.getInstance().vorschubText = "(" +  score*20+ " + " +  Math.round(Math.pow(2, score)*1000)/1000 + ") * "+ Math.round(Math.pow(verlaufFaktor, 3)*1000)/1000.0 + " + 2 = " + vorschub ;
        MySingleton.getInstance().vorschubText = "Platzhalter";

        double[] results = new double[5];
        results[0] = score;
        results[1] = counter;
        results[2] = 99;
        results[3] = vorschub;
        results[4] = position;

        return results;
    }


    public void resetScore() {
        this.score = 0;
    }
    public void addScore() {
        this.score = score+1;
    }
    public void addCounter() {
        this.counter = counter+1;
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


}

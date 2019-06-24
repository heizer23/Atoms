package app.lerner2.projects.my.lerner4;

import android.text.format.Time;
import android.util.Log;

public class MathStuff {

    public MathStuff(){

    }

    public String getTimings(long nextTime){
        Time now = new Time();
        now.setToNow();
        long longNow = now.toMillis(true)/1000;
        long diffSec = (nextTime- longNow);

        long years = diffSec / (12*30*60*60*24);
        diffSec = diffSec % (12*30*60*60*24);
        long month = diffSec / (30*60*60*24);
        diffSec = diffSec % (30*60*60*24);
        long days = diffSec / (60*60*24);
        diffSec = diffSec % (60*60*24);
        long hours = diffSec / (60*60);
        diffSec = diffSec % (60*60);
        long mins = diffSec / 60;
        diffSec = diffSec % 60;
        long secs = diffSec;

        String timeID ="t";
        long timeInt=0;
        if (years > 0){
            timeInt = years;
            timeID = " y";
        }else if(month > 0){
            timeInt = month;
            timeID = " m";
        }else if(days > 0){
            timeInt = days;
            timeID = " d";
        }else if(hours > 0){
            timeInt = hours;
            timeID = " h";
        }else if(mins > 0){
            timeInt = mins;
            timeID = " m";
        }else{
            timeInt = secs;
            timeID = " s";
        }

        return timeInt + timeID;

    }

    public long getVorschubalt(double score, int counter){
     //   double verlaufFaktor = Math.round(score/counter*10)/10.0;
        double verlaufFaktor = 1;
        long vorschub;
        int lin = MySingleton.getInstance().getVorschubLin();
        double exp = MySingleton.getInstance().getVorschubExp()/10;

        if(verlaufFaktor<0.1)verlaufFaktor=0.1;
        if (score <1){
            vorschub = 0;
        }else{
            vorschub = 10*(long)(Math.pow(verlaufFaktor, 3)*(score*lin/2)+( Math.pow(exp, score)));
        }
        Log.d("vs", "VS " + vorschub);
        return vorschub;
    }

    public long getVorschub(double score, int counter){
        //   double verlaufFaktor = Math.round(score/counter*10)/10.0;
        double verlaufFaktor = 1;
        long vorschub;
        int lin = MySingleton.getInstance().getVorschubLin();
        double exp = MySingleton.getInstance().getVorschubExp()/10;

        if(verlaufFaktor<0.1)verlaufFaktor=0.1;
        if (score <1){
            vorschub = 0;
        }else{
            vorschub = 10*(long)(Math.pow(verlaufFaktor, 3)*(score*lin/2)+( Math.pow(exp, score)));
        }
        Log.d("vs", "VS " + vorschub);
        return vorschub;
    }


}

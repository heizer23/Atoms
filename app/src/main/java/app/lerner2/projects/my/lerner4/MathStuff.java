package app.lerner2.projects.my.lerner4;

import android.text.format.Time;

public class MathStuff {

    public MathStuff(){

    }

    public String getTimings(long nextTime){
        Time now = new Time();
        now.setToNow();
        long longNow = now.toMillis(true);
        long diffSec = (nextTime- longNow)/1000;

        int days = (int)diffSec / (60*60*24);
        diffSec = diffSec % (60*60*24);
        int hours = (int)diffSec / (60*60);
        diffSec = diffSec % (60*60);
        int mins = (int)diffSec / 60;
        diffSec = diffSec % 60;
        int secs = (int)diffSec;

        String timeID ="t";
        int timeInt=0;
        if (days > 0){
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

    public long getVorschub(double score, int counter){
        double verlaufFaktor = Math.round(score/counter*10)/10.0;
        if(verlaufFaktor<0.1)verlaufFaktor=0.1;
        //todo an Settings anpassen
        return 2000*(int)(Math.pow(verlaufFaktor, 3)*(score*20+ Math.pow(2, score)));
    }

}

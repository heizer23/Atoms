package app.lerner2.projects.my.lerner4;

import android.text.format.Time;
import android.util.Log;

public class MathStuff {

    public MathStuff(){

    }



    public long[] getTimingDigits(long seconds){
        long[] digits = new long[6];
        long years = seconds / (12*30*60*60*24);
        seconds = seconds % (12*30*60*60*24);
        long month = seconds / (30*60*60*24);
        seconds = seconds % (30*60*60*24);
        long days = seconds / (60*60*24);
        seconds = seconds % (60*60*24);
        long hours = seconds / (60*60);
        seconds = seconds % (60*60);
        long mins = seconds / 60;
        seconds = seconds % 60;
        long secs = seconds;

        digits[0] = years;
        digits[1] = month;
        digits[2] = days;
        digits[3] = hours;
        digits[4] = mins;
        digits[5] = secs;

        return digits;
    }

    public long[] getTimingRelativeDigits(long nextTime){
        Time now = new Time();
        now.setToNow();
        long longNow = now.toMillis(true)/1000;
        long diffSec = (nextTime- longNow);
        return getTimingDigits(diffSec);
    }

    public String getTimingRelative(long nextTime){
        Time now = new Time();
        now.setToNow();
        long longNow = now.toMillis(true)/1000;
        long diffSec = (nextTime- longNow);
        return getTimingAbsolute(diffSec);
    }

    public String getTimingAbsolute(long seconds){
        long[] digits = getTimingDigits(seconds);
        String[] periode = {"y ", "m ", "d ", "h ", "m ", "s " };
        String actualPeriode1 = "nn";
        long time1 = 0;
        String actualPeriode2 = "nn";
        long time2 = 0;
        String result = "";

        for(int i = 0; i<6; i++){
            if(digits[i]>0 && i<5){
                actualPeriode1 = periode[i];
                time1 = digits[i];
                actualPeriode2 = periode[i+1];
                time2 = digits[i];
                i = 1000;
                result = actualPeriode1 + time1 +", " + actualPeriode2 + time2;
            }else{
                actualPeriode1 = periode[i];
                time1 = digits[i];
                result = actualPeriode1 + time1;
            }
        }

        return result;
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

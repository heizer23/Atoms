package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;


public class LogicCompare {

    Activity act;
    Context c;
    private String[] buttenTexts = new String[10];
    private FrageDatum lastFrage;
    private FrageDatum actFrage;
    private int eingrenzungen;
    private int rahmen= 1000;
    int lowestDate;

    public LogicCompare(Context c, Activity act) {
        this.c = c;
        this.act = act;
    }

    public String neueFrage(){
        if (!(actFrage == null)){
            lastFrage = actFrage;
        }

        actFrage = new FrageDatum(c, act);

        eingrenzungen = 3;
        rahmen= 1000;
        return actFrage.getItem();
    }

    public String getItem() {
        return null;
    }

    public String[] getButtonTexts() {
        int[] wert = new int[12];
        int multi;
        int minimum;
        int maximum;
        int datumStart;
        int datum= actFrage.getDatum();

        if (datum > 1900) {
            rahmen = 100;
            eingrenzungen = 2;
        }
        String[] buttTextsSorted = new String[10];

        datumStart = 0;
        if (datum < 0) {
            multi = (int) ((datum + 1) / rahmen) - 1;
        } else {
            multi = (int) (datum / rahmen);
        }
        minimum = datumStart+(rahmen * multi); // achtung BC
        maximum = (int) minimum + rahmen - 1;

        if ((maximum - minimum) > 10) {
            wert[0] = minimum;
            for (int i = 1; i < 11; i++) {
                wert[i] = (int) (minimum + (rahmen * i / 10));
            }
            for (int i = 0; i < 10; i++) {
                String text = ("" + wert[i] + " - " + (wert[i + 1] - 1));
                buttenTexts[i] = text;
            }

        } else { // nur noch 10 alternativen
            for (int i = 0; i < 10; i++) {
                int tempText = minimum + i;
                buttenTexts[i] = ""+ tempText;
            }
            // finished = true;
        }
        int index = 0;
        for(int i = 0; i<10;i++){

            if(i%2==0){
                buttTextsSorted[index] = buttenTexts[i];
                index++;
            }else{
                buttTextsSorted[index+4] = buttenTexts[i];
            }
        }

        return buttTextsSorted;
    }

    public String[] getRundenInfo(){
        MathStuff ms = new MathStuff();
        String[] result = new String[8];
        if (!(actFrage == null)) {
            result[0] = "Next: " + ms.getTimings(actFrage.getNext());
            result[1] = "Score: " + actFrage.getScore();
            result[2] = "Counter";
            result[3] = ""+actFrage.getCounter();
            int[] metaStats = actFrage.getMetaStats();
            result[4] = "g "  + metaStats[0];
            result[5] = "p "  + metaStats[1];
            String tempStatus = actFrage.getStatus();
            if(tempStatus.equals("richtig")){
                result[6] = "" + actFrage.getDatum() + "  ist korrekt";
            }else{
                result[6] = "" + lowestDate + "  ist falsch - Es wäre " + actFrage.getDatum() + " gewesen";
            }
        }else{
            result[0] = "Next: ";
            result[1] = "Score: " ;
            result[2] = "Counter";
            result[3] = "";
            result[4] = "";
            result[5] = "";
            result[6] = "";
        }
        result[7] = "";
        return result;
    }

    public double[] checkAnswer( String answer){
        double[] result = new double[2];
        int eingrenz = 1;
        int datum= actFrage.getDatum();
        String[] splitAnswer = answer.split(" - ");
        lowestDate = Integer.parseInt(splitAnswer[0]);
        int datumsDelta = lowestDate - datum;

        if(splitAnswer.length > 1) {
            int highestDate = Integer.parseInt(splitAnswer[1]);
            if (lowestDate > datum || highestDate < datum) {
                actFrage.calcResults(false);
            } else {
                eingrenz = 0;
                rahmen = rahmen / 10;
            }
        }else{
                if (lowestDate == datum){
                    actFrage.calcResults(true);
                }else{
                    actFrage.calcResults(false);
                }
        }

        result[0] = eingrenz;
        result[1] = datumsDelta;

        return result;
    }

    public String[] getUrl(){
        return actFrage.getUrl();
    }

}

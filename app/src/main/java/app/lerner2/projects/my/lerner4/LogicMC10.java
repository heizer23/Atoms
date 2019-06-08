package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Random;


public class LogicMC10{

    Activity act;
    Context c;
    private String[] buttenTexts = new String[10];
    private FrageDatum lastFrage;
    private FrageDatum actFrage;
    private int eingrenzungen;
    private int rahmen= 1000;

    public LogicMC10(Context c, Activity act) {
        this.c = c;
        this.act = act;
    }

    public String neueFrage(){
        if (!(lastFrage == null)){
            MySingleton.getInstance().solution = ""+lastFrage.getDatum();
        }

        actFrage = new FrageDatum(c, act);

        eingrenzungen = 3;
        rahmen= 1000;
        return actFrage.getItem();
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

    public double[] checkAnswer(String answer) {
        double[] eingrenz_richtig = new double[7]; // eingrenzung[1] ---- 0 richtig, -1 zu früh, 1 zu spät
        double[] infos;
        int datum= actFrage.getDatum();
        eingrenzungen++;
        eingrenz_richtig[0] = 0;
        eingrenz_richtig[1] = 0;

        int[] guess = new int[2];

        String[] saSplit = answer.split(" - ");

        guess[0]  = Integer.parseInt(saSplit[0]);

        if(saSplit.length > 1){ //Eingrenzung
            guess[1]  = Integer.parseInt(saSplit[1]);

            if(guess[0]>datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                actFrage.resetScore();
            }else if(guess[1]<datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                actFrage.resetScore();
            }else{
                eingrenz_richtig[0] = 0;
                eingrenz_richtig[1] = guess[0]-datum;
                rahmen = rahmen / 10;
            }
        }else{      // Final
            if(guess[0]>datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                actFrage.resetScore();
            }else if(guess[0]<datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                actFrage.resetScore();
            }else{
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                actFrage.addScore();
            }
        }

        if(eingrenz_richtig[0]==1) actFrage.addCounter();

        infos = actFrage.calcResults(eingrenz_richtig[0]);

        for(int i=0; i<infos.length;i++){
            eingrenz_richtig[2+i] = infos[i];
        }

        return eingrenz_richtig;
    }

}

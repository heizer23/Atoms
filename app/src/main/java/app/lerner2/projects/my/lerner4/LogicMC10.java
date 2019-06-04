package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Random;


public class LogicMC10{

    private Activity act;
    private Context ourContext;
    private int rahmen = 1000;
    private int datum;
    private int counter;
    private String item;
    private String ort;
    private double score;
    private long next;
    private String[] buttenTexts = new String[10];
    private int id;
    private DbHelper dbHelper;

    private int eingrenzungen = 3;
    public LogicMC10(Context c, Activity act) {
        ourContext = c;
        this.act = act;
        dbHelper = new DbHelper(c, act);

    }

    public String neueFrage(int fragenId){
        MySingleton.getInstance().solution = ""+datum;


        dbHelper.open();
        String[] values = dbHelper.getFrageInfos(fragenId);
        dbHelper.close();

        id = Integer.parseInt(values[0]);
        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);
        ort= values[6];
        rahmen = 1000;
        if (datum > 1900) {
            rahmen = 100;
            eingrenzungen=2;
        }
        return item;
    }

    public String[] getButtonTexts() {
        int[] wert = new int[12];
        int multi;
        int minimum;
        int maximum;
        int datumStart;

        String[] buttTextsSorted = new String[10];


        //auf 100 bzw 1000 abgerundeter Startwert

        datumStart = datum-1000;
        Random r = new Random();
        // datumStart = r.nextInt(datumStart);
        //todo hier start für random start
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
                score = 0;
            }else if(guess[1]<datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                score = 0;
            }else{
                eingrenz_richtig[0] = 0;
                eingrenz_richtig[1] = guess[0]-datum;
                if(rahmen==100)score = score +1.0;
                rahmen = rahmen / 10;
            }
        }else{      // Final
            if(guess[0]>datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                score = 0;
            }else if(guess[0]<datum){
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                score = 0;
            }else{
                eingrenz_richtig[0] = 1;
                eingrenz_richtig[1] = guess[0]-datum;
                score = score +1.0;
            }
        }

        if(eingrenz_richtig[0]==1)counter = counter + 1;

        infos = calcResults(eingrenz_richtig[0]);

        for(int i=0; i<infos.length;i++){
            eingrenz_richtig[2+i] = infos[i];
        }

        return eingrenz_richtig;
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

}

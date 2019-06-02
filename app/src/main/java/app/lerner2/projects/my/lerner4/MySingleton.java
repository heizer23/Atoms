package app.lerner2.projects.my.lerner4;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/*
 txt datei mit string string dupeln wird eingelesen
 dupel werden als string/string in shared preferences gespeichert
 alle get und set funktionen schreiben in diese sharedPref. mgl booleschen wert mit dazu um neue werte zu identifizieren
 in den getMethoden vermerken, fall ein wert irgendwo direkt aus den shared preferencen gezogen wird (damit auch da geloescht werden kann)
 */

public class MySingleton {
    private static MySingleton instance;
    public String[] statisicstAuswahl = {"false","false","false","false","false","false",
    "false","false","false","false","false","false","false","false"};
    private int deltaUWF = 0;
    private int rundeGlob = 0;
    private int VorschubLin = 4;
    private int VorschubExp = 4;
    private int onlyTop2 = 0;
    private int onlyBottom2 = 0;
    private int vorschubGrenze = 10;
    private Vector<String[]> sqlQueries = new Vector<String[]>();

    public String vorschubText = "";

    // Infostrings

    private int aktiviert = 0;



    private int next = 0;
    public String solution = "-";
    private int count = 0;
    public int maxDifference = 0;
    public double score = 0;

    public int getAktiviert() {
        return aktiviert;
    }
    public void addAktiviert(int aktiviert) {
        count = count + aktiviert;
        this.aktiviert = this.aktiviert + aktiviert;
    }

    public int getNext() {
        return next;
    }
    public void setNext(int next) {
        this.next = next;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }




    private MySingleton() {
        // Constructor hidden because this is a singleton
    }

    public static MySingleton getInstance() {
        // Return the instance
        if (instance == null) {
            // Create the instance
            instance = new MySingleton();
            instance.readData();
        }
        return instance;
    }


    public String getInfo(String sButtText){
        String infoText = "Aktiviert: " + aktiviert + " Next: " + next;
        infoText = infoText + "\n" + vorschubText;
        return infoText;
    }


    public int getOnlyTop2() {
        return onlyTop2;
    }


    public int getVorschubGrenze() {
        return vorschubGrenze;
    }

    public void setVorschubGrenze(int vorschubGrenze) {
        this.vorschubGrenze = vorschubGrenze;
    }

    public void setOnlyTop2(int onlyTop2) {
        this.onlyTop2 = onlyTop2;
    }

    public int getOnlyBottom2() {
        return onlyBottom2;
    }

    public void setOnlyBottom2(int onlyBottom2) {
        this.onlyBottom2 = onlyBottom2;
    }


    public int getRundeGlob() {
        return rundeGlob;
    }

    public int getDeltaUWF() {
        return deltaUWF;
    }

    public void setDeltaUWF(int deltaUWF) {
        deltaUWF = deltaUWF;
    }

    public int getVorschubExp() {
        return VorschubExp;
    }

    public void setVorschubExp(int vorschubExp) {
        VorschubExp = vorschubExp;
    }

    public int getVorschubLin() {
        return VorschubLin;
    }

    public void setVorschubLin(int vorschubLin) {
        VorschubLin = vorschubLin;
    }


    private void readData() {
        Vector<String[]> datenhalter = new Vector<String[]>();
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/Quizzer/" + "config.txt");
            // new BufferedReader(new FileReader(f));
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f),
                            "UTF-8"));
            String line = "";
            while ((line = bf.readLine()) != null) {
                String[] splittedLine2 = line.split("::");
                datenhalter.add(splittedLine2);
            }
            bf.close();

            if(datenhalter.size()>0){
                deltaUWF = Integer.parseInt(datenhalter.get(1)[1]);
                rundeGlob = (int) Double.parseDouble(datenhalter.get(2)[1]);
                VorschubLin = Integer.parseInt(datenhalter.get(3)[1]);
                VorschubExp = Integer.parseInt(datenhalter.get(4)[1]);
                onlyBottom2 = Integer.parseInt(datenhalter.get(5)[1]);
                onlyTop2 = Integer.parseInt(datenhalter.get(6)[1]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        datenhalter = new Vector<String[]>();
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/Quizzer/" + "queries.txt");
            // new BufferedReader(new FileReader(f));
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f),
                            "UTF-8"));
            String line = "";
            while ((line = bf.readLine()) != null) {
                String[] splittedLine2 = line.split("::");
                sqlQueries.add(splittedLine2);
            }
            bf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRundeGlobal() {
        rundeGlob = rundeGlob + 1;
    }

    public void saveConfiguration() {
        try {
            //Hack um bei nicht vorhandenen werten catch zu provozieren
            String temp = ""+rundeGlob;
            File myFile = new File("/sdcard/Quizzer/config.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);


            myOutWriter.append("1: deltaUWF::"+ deltaUWF + "\n");
            myOutWriter.append("2: rundeGlob::"+rundeGlob + "\n");
            myOutWriter.append("3: VorschubLin::"+VorschubLin + "\n");
            myOutWriter.append("4: VorschubExp::"+VorschubExp + "\n");
            myOutWriter.append("5: onlyBottom2::"+ onlyBottom2 + "\n");
            myOutWriter.append("6: onlyTop2::"+ onlyTop2 + "\n");
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            String temp = e.toString();
            e.printStackTrace();
        }


    }

}

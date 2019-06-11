package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;


public class LogicCompare implements Logic {

    private FrageDatum actFrage;

    public LogicCompare(FrageDatum actFrage) {
        this.actFrage = actFrage;
    }

    public String[] getButtonTexts() {
        String[] buttTextsSorted = new String[10];
        int index = 0;
        for(int i = 0; i<10;i++){

            if(i%2==0){
                buttTextsSorted[index] = "Früher";
                index++;
            }else{
                buttTextsSorted[index+4] = "Später";
            }
        }
        return buttTextsSorted;
    }

    public boolean qualifyAnswer(String answer){
        return false;
    }

    public void checkAnswer( String answer){
        if (1000 == actFrage.getDatum()){
            actFrage.calcResults(true);
        }else{
            actFrage.calcResults(false);
        }
    }
}


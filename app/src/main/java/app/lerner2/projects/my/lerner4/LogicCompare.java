package app.lerner2.projects.my.lerner4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LogicCompare implements Logic {

    private FrageDatum actFrage;
    private FrageDatum actCompareFrage;
    private List<FrageDatum> compareFragen = new ArrayList();
    private List<Integer> questionIds = new ArrayList();


    public LogicCompare(FrageDatum actFrage) {
        this.actFrage = actFrage;
        fillupFragen();
    }

    public String getQuestion(){
        Collections.shuffle(compareFragen);
        actCompareFrage = compareFragen.get(0);
        return actFrage.getItem() + "\n \n war ??? als \n \n" + actCompareFrage.getItem();
    }

    private void fillupFragen(){
        List<Integer> compIds  = actFrage.getComparableIds();

        while(compIds.size()>0){
            compareFragen.add(new FrageDatum(compIds.get(0), actFrage));
            compIds.remove(0);
        }
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
        if (checkAnswerInternal(answer)){
            compareFragen.remove(actCompareFrage);
        }
        return compareFragen.size()>1;
    }

    public void checkAnswer( String answer){
        if (checkAnswerInternal(answer)){
            actFrage.calcResults(false);
        }else{
            actFrage.calcResults(false);
        }
    }

    private boolean checkAnswerInternal(String answer){
        //todo hier noch als Option "gleich" berücksichtigen
        boolean isCorrect = false;

                if(answer.equals("Früher") && actFrage.getDatum()<= actCompareFrage.getDatum() ||
                answer.equals("Später") && actFrage.getDatum()> actCompareFrage.getDatum()){
                    isCorrect = true;
                    actFrage.setFeedbackString("Richtig: \n"+ actCompareFrage.getItem() + " war " + actCompareFrage.getDatum());
                }else{
                    actFrage.setFeedbackString("Falsch: \n" + actCompareFrage.getItem() + " war " + actCompareFrage.getDatum());
                };
        return isCorrect;
    }
}


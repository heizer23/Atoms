package app.lerner2.projects.my.lerner4;


public class LogicMC10 implements Logic{
    private String[] buttenTexts = new String[10];
    private int eingrenzungen;
    private int rahmen= 1000;

    private FrageDatum actFrage;

    public LogicMC10(FrageDatum actFrage) {
        this.actFrage = actFrage;
        eingrenzungen = 3;
        rahmen= 1000;
    }

    public String getQuestion(){
        return actFrage.getItem();
    }
    public String[] getButtonTexts() {
        int[] wert = new int[12];
        int multi;
        int minimum;
        int maximum;
        int datumStart;
        int datum = actFrage.getDatum();
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



    public boolean qualifyAnswer(String answer){
        int datum= actFrage.getDatum();
        int lowestDate;
        boolean eingrenzung = false;
        String[] splitAnswer = answer.split(" - ");
        if(splitAnswer.length > 1) {
            lowestDate = Integer.parseInt(splitAnswer[0]);
            int highestDate = Integer.parseInt(splitAnswer[1]);
            if (!(lowestDate > datum || highestDate < datum)) {
                rahmen = rahmen / 10;
                eingrenzung = true;
                actFrage.setFeedbackString("Korrekt");
            } else {
                eingrenzung = false;
            }
        }
        return eingrenzung;
    }

    public void checkAnswer( String answer){
        String[] splitAnswer = answer.split(" - ");
        int lowestDate = Integer.parseInt(splitAnswer[0]);

        if (lowestDate == actFrage.getDatum()){
            actFrage.calcResults(true);
            actFrage.setFeedbackString("" + actFrage.getDatum() + "  ist korrekt");
        }else{
            actFrage.calcResults(false);
            actFrage.setFeedbackString("" + lowestDate + "  ist falsch - Es wäre " + actFrage.getDatum() + " gewesen");
        }
    }
}

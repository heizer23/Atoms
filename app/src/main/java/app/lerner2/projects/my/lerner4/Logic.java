package app.lerner2.projects.my.lerner4;

public interface Logic {

    void neueFrage();
    String getItem();
    String[] getButtonTexts();
    String[] getRundenInfo();
    double[] checkAnswer( String answer);
    String[] getUrl();
}

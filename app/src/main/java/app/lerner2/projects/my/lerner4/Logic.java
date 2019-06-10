package app.lerner2.projects.my.lerner4;

public interface Logic {

    void neueFrage();
    String getItem();
    String[] getButtonTexts();
    String[] getRundenInfo();
    boolean checkAnswer( String answer);
    String[] getUrl();
}

package app.lerner2.projects.my.lerner4;

public interface Logic {
    String[] getButtonTexts();
    boolean qualifyAnswer(String answer);
    void checkAnswer( String answer);
}

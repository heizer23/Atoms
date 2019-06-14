package app.lerner2.projects.my.lerner4;

public interface Logic {
    String getQuestion();
    String[] getButtonTexts();
    boolean qualifyAnswer(String answer);
    void checkAnswer( String answer);
}

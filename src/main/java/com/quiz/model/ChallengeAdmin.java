package com.quiz.model;


public class ChallengeAdmin {
    private String levelTitle;
    private String challengeText;
    private String expectedOutput;

    // Constructeur avec les 3 champs
    public ChallengeAdmin(String levelTitle, String challengeText, String expectedOutput) {
        this.levelTitle = levelTitle;
        this.challengeText = challengeText;
        this.expectedOutput = expectedOutput;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public String getChallengeText() {
        return challengeText;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }
}


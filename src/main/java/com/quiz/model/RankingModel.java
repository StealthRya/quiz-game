package com.quiz.model;

public class RankingModel {
    private String userPseudo;
    private int userScore;

    public RankingModel(String userPseudo,int userScore){
        this.userPseudo = userPseudo;
        this.userScore = userScore;
    }


    public String getUserPseudo(){
        return userPseudo;
    }


    public int getUserScore(){
        return userScore;
    }
}

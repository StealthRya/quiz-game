package com.quiz.model;

public class Challenges {
    public int id;
    public String questionText;
    public String expectedOutput;

    public Challenges() {}

    @Override
    public String toString() {
        return "Challenges{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                '}';
    }
}

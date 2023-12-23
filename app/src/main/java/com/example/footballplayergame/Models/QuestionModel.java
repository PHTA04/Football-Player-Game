package com.example.footballplayergame.Models;

public class QuestionModel {
    private String question, optionA, optionB, optionC, optionD, correctionAnswer, ImageQues;
    private int SetNum;

    public QuestionModel() {
    }

    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, String correctionAnswer, String ImageQues, int SetNum) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctionAnswer = correctionAnswer;
        this.ImageQues = ImageQues;
        this.SetNum = SetNum;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectionAnswer() {
        return correctionAnswer;
    }

    public void setCorrectionAnswer(String correctionAnswer) {
        this.correctionAnswer = correctionAnswer;
    }

    public String getImageQues() {
        return ImageQues;
    }

    public void setImageQues(String ImageQues) {
        this.ImageQues = ImageQues;
    }

    public int getSetNum() {
        return SetNum;
    }

    public void setSetNum(int SetNum) {
        this.SetNum = SetNum;
    }
}

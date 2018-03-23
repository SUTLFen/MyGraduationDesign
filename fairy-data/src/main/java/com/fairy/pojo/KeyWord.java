package com.fairy.pojo;

import com.fairy.hotword.Result;

public class KeyWord implements Comparable<KeyWord>{

    private String word;
    private Float score;

    public KeyWord() {
    }

    public KeyWord(Result s) {
       this.word = s.getTerm();
       this.score = s.getScore();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return this.word + ": " + this.score;
    }

    @Override
    public int compareTo(KeyWord keyWord02) {
        return keyWord02.getScore().compareTo(this.score);
    }
}

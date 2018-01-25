package com.fairy.pojo;

import com.fairy.hotword.Result;

public class KeyWord {

    private String text;
    private double size;

    public KeyWord() {
    }

    public KeyWord(Result s) {
       this.text = s.getTerm();
       this.size = s.getScore();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}

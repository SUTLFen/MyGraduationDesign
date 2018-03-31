package com.fairy.data.wordcloud;

/**
 * 词云中的关键词
 */
public class CloudWord {
    private String text;
    private int size;

    public CloudWord() {
    }

    public CloudWord(String text, int size) {
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

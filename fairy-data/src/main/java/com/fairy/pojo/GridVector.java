package com.fairy.pojo;

public class GridVector extends Grid{
    public int[] vectors;
    public float entropy;

    public GridVector() {
    }

    public int[] getVectors() {
        return vectors;
    }

    public float getEntropy() {
        return entropy;
    }

    public void setEntropy(float entropy) {
        this.entropy = entropy;
    }

    public void setVectors(int[] vectors) {
        this.vectors = vectors;
    }
}

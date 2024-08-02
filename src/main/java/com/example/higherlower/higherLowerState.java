package com.example.higherlower;

public class higherLowerState {
    private static higherLowerState instance = new higherLowerState();
    private int highScore;

    private higherLowerState() {
        // Private constructor to prevent instantiation
    }

    public static higherLowerState getInstance() {
        return instance;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

}

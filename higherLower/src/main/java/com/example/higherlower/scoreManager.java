package com.example.higherlower;

public class scoreManager {
    private int currentScore;
    private int highScore;

    public scoreManager() {
        this.currentScore = 0;
        this.highScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void updateScore(boolean correct) {
        if (correct) {
            currentScore++;
            if (currentScore > highScore) {
                highScore = currentScore;
            }
        } else {
            currentScore = 0; // Reset score on incorrect answer
        }
    }
}


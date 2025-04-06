package com.example.madassignment;

import java.io.Serializable;

public enum DifficultyLevel implements Serializable {
    EASY(1, 50),
    MEDIUM(1, 99),
    HARD(100, 999);

    public final int min;
    public final int max;

    DifficultyLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
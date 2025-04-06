package com.example.madassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    public static final String DIFFICULTY_PREF = "DifficultyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);
    }

    public void onDifficultySelected(View view) {
        DifficultyLevel difficulty = DifficultyLevel.EASY;
        int id = view.getId();

        if (id == R.id.btnMedium) {
            difficulty = DifficultyLevel.MEDIUM;
        } else if (id == R.id.btnHard) {
            difficulty = DifficultyLevel.HARD;
        }

        if (id == R.id.btnEasy || id == R.id.btnMedium || id == R.id.btnHard) {
            SharedPreferences prefs = getSharedPreferences(DIFFICULTY_PREF, MODE_PRIVATE);
            prefs.edit().putString("difficulty", difficulty.name()).apply();

            Log.d("DIFFICULTY_DEBUG", "[Difficulty Selection] Chosen: " + difficulty);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
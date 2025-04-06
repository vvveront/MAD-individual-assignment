package com.example.madassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DifficultyLevel difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(DifficultyActivity.DIFFICULTY_PREF, MODE_PRIVATE);
        String difficultyName = prefs.getString("difficulty", DifficultyLevel.EASY.name());
        difficulty = DifficultyLevel.valueOf(difficultyName);

        Log.d("DIFFICULTY_DEBUG", "[MainActivity] Difficulty: " + difficulty);
    }

    public void openCompare(View view) {
        Intent intent = new Intent(this, CompareNumbersActivity.class);
        intent.putExtra("DIFFICULTY", difficulty);
        startActivity(intent);
    }

    public void openOrder(View view) {
        startActivity(new Intent(this, OrderNumbersActivity.class)
                .putExtra("DIFFICULTY", difficulty));
    }

    public void openCompose(View view) {
        startActivity(new Intent(this, ComposeNumbersActivity.class)
                .putExtra("DIFFICULTY", difficulty));
    }

    public void openSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
package com.example.madassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
    }

    public void onLearnClick(View view) {
        startActivity(new Intent(this, VideoLearningActivity.class));
    }

    public void onPlayClick(View view) {
        startActivity(new Intent(this, DifficultyActivity.class));
    }

    public void openVideoLearning(View view) {
        startActivity(new Intent(this, VideoLearningActivity.class));
    }

}
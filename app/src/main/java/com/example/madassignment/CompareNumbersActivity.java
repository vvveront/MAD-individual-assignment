package com.example.madassignment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {

    private TextView num1, num2, scoreView;
    private ImageView symbolIndicator;
    private int score = 0;
    private Random random = new Random();
    private DifficultyLevel difficulty;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        difficulty = (DifficultyLevel) getIntent().getSerializableExtra("DIFFICULTY");
        if (difficulty == null) {
            Toast.makeText(this, "Difficulty not set!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        generateNumbers();
    }

    private void initializeViews() {
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        scoreView = findViewById(R.id.score);
        symbolIndicator = findViewById(R.id.symbol_indicator);
    }

    private void generateNumbers() {
        int min = difficulty.min;
        int max = difficulty.max;

        int first = random.nextInt(max - min + 1) + min;
        int second = random.nextInt(max - min + 1) + min;

        if(random.nextFloat() < 0.25) {
            second = first;
        }

        num1.setText(String.valueOf(first));
        num2.setText(String.valueOf(second));
    }

    public void onGreaterClick(View view) { checkComparison(">"); }
    public void onEqualClick(View view) { checkComparison("="); }
    public void onSmallerClick(View view) { checkComparison("<"); }

    private void checkComparison(String selectedSymbol) {
        int a = Integer.parseInt(num1.getText().toString());
        int b = Integer.parseInt(num2.getText().toString());

        String correctSymbol = getCorrectSymbol(a, b);
        boolean isCorrect = selectedSymbol.equals(correctSymbol);

        updateScoreAndFeedback(isCorrect, a, b, correctSymbol);
        animateSymbolIndicator(isCorrect);
    }

    private String getCorrectSymbol(int a, int b) {
        if(a == b) return "=";
        return a > b ? ">" : "<";
    }

    private void updateScoreAndFeedback(boolean isCorrect, int a, int b, String symbol) {
        String feedback;
        if (isCorrect) {
            score += 5;
            feedback = "Correct! " + a + " " + symbol + " " + b;
            playSound(R.raw.correct);
            generateNumbers();
        } else {
            score = Math.max(0, score - 2);
            feedback = "Oops! Correct is " + a + " " + symbol + " " + b;
            playSound(R.raw.wrong);
        }

        scoreView.setText("Stars: " + score);
        Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();
    }

    private void playSound(int soundRes) {
     if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, soundRes);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
        mediaPlayer.start();
    }

    private void animateSymbolIndicator(boolean isCorrect) {
        int drawableId = isCorrect ? R.drawable.ic_correct : R.drawable.ic_wrong;
        symbolIndicator.setImageResource(drawableId);
        symbolIndicator.setVisibility(View.VISIBLE);

        symbolIndicator.animate()
                .scaleX(1.2f).scaleY(1.2f)
                .setDuration(300)
                .withEndAction(() -> symbolIndicator.animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(200)
                        .withEndAction(() -> symbolIndicator.setVisibility(View.INVISIBLE))
                );
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
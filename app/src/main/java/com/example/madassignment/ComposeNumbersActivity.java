package com.example.madassignment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.Random;

public class ComposeNumbersActivity extends AppCompatActivity {

    private enum Operation { ADD, SUB, MUL, DIV }
    private Operation currentOperation = Operation.ADD;
    private TextView targetNumberView, scoreView, tvOperator;
    private EditText etNum1, etNum2;
    private int targetNumber, score = 0;
    private DifficultyLevel difficulty;
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;
    private Button[] operationButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);

        difficulty = (DifficultyLevel) getIntent().getSerializableExtra("DIFFICULTY");
        if (difficulty == null) {
            Toast.makeText(this, "Difficulty not set!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupOperationButtons();
        generateNewTarget();
    }

    private void initializeViews() {
        targetNumberView = findViewById(R.id.tv_target);
        scoreView = findViewById(R.id.tv_score);
        etNum1 = findViewById(R.id.et_num1);
        etNum2 = findViewById(R.id.et_num2);
        tvOperator = findViewById(R.id.tv_operator);
        correctSound = MediaPlayer.create(this, R.raw.correct);

        InputFilter[] filters = {
                new InputFilterMinMax(difficulty.min, difficulty.max),
                new InputFilter.LengthFilter(3)
        };
        etNum1.setFilters(filters);
        etNum2.setFilters(filters);

        findViewById(R.id.tutorial_icon).setOnClickListener(this::showSymbolTutorial);
    }

    private void setupOperationButtons() {
        operationButtons = new Button[]{
                findViewById(R.id.btn_add),
                findViewById(R.id.btn_subtract),
                findViewById(R.id.btn_multiply),
                findViewById(R.id.btn_divide)
        };

        for(Button btn : operationButtons) {
            btn.setOnClickListener(v -> {
                currentOperation = Operation.values()[Arrays.asList(operationButtons).indexOf(v)];
                updateOperationUI();
                generateNewTarget();
                updateOperatorDisplay();
            });
        }
        updateOperationUI();
        updateOperatorDisplay();
    }

    private void generateNewTarget() {
        Random random = new Random();
        int min = difficulty.min;
        int max = difficulty.max;

        switch (currentOperation) {
            case ADD:
                targetNumber = random.nextInt(max - min + 1) + min;
                break;
            case SUB:
                int a = random.nextInt(max - min + 1) + min;
                int b = random.nextInt(a - min + 1) + min;
                targetNumber = a - b;
                break;
            case MUL:
                int sqrtMax = (int) Math.sqrt(max);
                int x = random.nextInt(sqrtMax - min + 1) + min;
                int y = random.nextInt((max / x) - min + 1) + min;
                targetNumber = x * y;
                break;
            case DIV:
                int divisor = random.nextInt((max / 2) - min + 1) + min;
                targetNumber = random.nextInt((max / divisor) - min + 1) + min;
                int dividend = targetNumber * divisor;
                targetNumber = dividend / divisor;
                break;
        }
        targetNumberView.setText(String.valueOf(targetNumber));
    }

    private void updateOperationUI() {
        for(int i=0; i<operationButtons.length; i++) {
            operationButtons[i].setAlpha(currentOperation.ordinal() == i ? 1f : 0.6f);
        }
    }

    private void updateOperatorDisplay() {
        if (SettingsActivity.useWordsInsteadOfSymbols(this)) {
            String[] wordOps = getResources().getStringArray(R.array.operation_words);
            tvOperator.setText(wordOps[currentOperation.ordinal()]);
        } else {
            String[] symbolOps = {"+", "-", "×", "÷"};
            tvOperator.setText(symbolOps[currentOperation.ordinal()]);
        }
    }

    public void showSymbolTutorial(View v) {
        new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.how_to_play)
                .setMessage(R.string.symbol_explanations)
                .setPositiveButton(R.string.got_it, null)
                .show();
    }

    private void resetFieldErrors() {
        etNum1.setBackgroundResource(R.drawable.edittext_bg);
        etNum2.setBackgroundResource(R.drawable.edittext_bg);

        etNum1.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) resetFieldErrors();
        });

        etNum2.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) resetFieldErrors();
        });
    }


    private void showErrorOnFields() {
        etNum1.setBackgroundResource(R.drawable.error_background);
        etNum2.setBackgroundResource(R.drawable.error_background);
        etNum1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        etNum2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    public void onCheckClick(View v) {
        resetFieldErrors();

        String input1 = etNum1.getText().toString().trim();
        String input2 = etNum2.getText().toString().trim();

        if(input1.isEmpty() || input2.isEmpty()) {
            showErrorOnEmptyFields();
            Toast.makeText(this, "Please fill in both numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int num1 = Integer.parseInt(input1);
            int num2 = Integer.parseInt(input2);

            if (num1 < 0 || num1 > difficulty.max ||
                    num2 < 0 || num2 > difficulty.max) {
                showErrorOnFields();
                Toast.makeText(this, "Numbers must be between 0 and " +
                        difficulty.max, Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isCorrect = false;
            switch(currentOperation) {
                case ADD:
                    isCorrect = (num1 + num2 == targetNumber);
                    break;
                case SUB:
                    isCorrect = (num1 - num2 == targetNumber);
                    break;
                case MUL:
                    isCorrect = (num1 * num2 == targetNumber);
                    break;
                case DIV:
                    if(num2 == 0) {
                        showErrorOnFields();
                        Toast.makeText(this, "Can't divide by zero!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isCorrect = (num1 / num2 == targetNumber) && (num1 % num2 == 0);
                    break;
            }

            if(isCorrect) {
                handleCorrectAnswer();
            } else {
                handleIncorrectAnswer();
            }

        } catch (NumberFormatException e) {
            showErrorOnFields();
            Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            showErrorOnFields();
            Toast.makeText(this, "Invalid calculation!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorOnEmptyFields() {
        if(etNum1.getText().toString().trim().isEmpty()) {
            etNum1.setBackgroundResource(R.drawable.error_background);
        }
        if(etNum2.getText().toString().trim().isEmpty()) {
            etNum2.setBackgroundResource(R.drawable.error_background);
        }
    }

    private void handleCorrectAnswer() {
        score += 10;
        scoreView.setText("Stars: " + score);
        playSound(R.raw.correct);
        generateNewTarget();
        etNum1.setText("");
        etNum2.setText("");
        resetFieldErrors();
        Toast.makeText(this, "Well Done! 😊", Toast.LENGTH_SHORT).show();
    }

    private void handleIncorrectAnswer() {
        playSound(R.raw.wrong);
        etNum1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        etNum2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        Toast.makeText(this, "Try Again! 💪", Toast.LENGTH_SHORT).show();
    }

    private void playSound(int soundRes) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, soundRes);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("Audio", "Error playing sound", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
package com.example.madassignment;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class OrderNumbersActivity extends AppCompatActivity {
    private final List<MediaPlayer> activeMediaPlayers = new ArrayList<>();
    private TextView questionTitle, questionText;
    private LinearLayout numberPool;
    private DifficultyLevel difficulty;
    private boolean isAscending;
    private final int[] targetBoxIds = {R.id.target1, R.id.target2, R.id.target3};
    private final ArrayList<Integer> currentNumbers = new ArrayList<>();
    private ArrayList<Integer> originalOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        difficulty = (DifficultyLevel) getIntent().getSerializableExtra("DIFFICULTY");
        initializeViews();
        setupDragListeners();
        generateNewQuestion();
    }

    private void initializeViews() {
        numberPool = findViewById(R.id.number_pool);
        questionTitle = findViewById(R.id.question_title);
        questionText = findViewById(R.id.question_text);
    }

    private void setupDragListeners() {
        for(int id : targetBoxIds) {
            findViewById(id).setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DROP:
                            handleDrop(event, (TextView) v);
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            v.setBackgroundResource(R.drawable.target_hover);
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundResource(R.drawable.empty_box_bg);
                            return true;
                    }
                    return true;
                }
            });
        }
    }

    private void handleDrop(DragEvent event, TextView targetBox) {
        View draggedView = (View) event.getLocalState();
        TextView draggedNumber = (TextView) draggedView;

        if(targetBox.getText().toString().isEmpty()) {
            targetBox.setText(draggedNumber.getText());
            targetBox.setTag(draggedNumber.getTag());
            targetBox.setBackgroundResource(R.drawable.filled_box_bg);
            draggedNumber.setVisibility(View.INVISIBLE);
            checkOrder();
        }
    }

    private void generateNewQuestion() {
        resetGameState();
        generateNumbers();
        createDraggableNumbers();
        updateQuestionText();
    }

    private void generateNumbers() {
        currentNumbers.clear();
        Random rand = new Random();

        while(currentNumbers.size() < 3) {
            int num = rand.nextInt(difficulty.max - difficulty.min + 1) + difficulty.min;
            if(!currentNumbers.contains(num)) currentNumbers.add(num);
        }

        isAscending = new Random().nextBoolean();
        originalOrder = new ArrayList<>(currentNumbers);
        Collections.sort(currentNumbers);
        if(!isAscending) Collections.reverse(currentNumbers);
    }

    private void createDraggableNumbers() {
        numberPool.removeAllViews();
        ArrayList<Integer> shuffled = new ArrayList<>(originalOrder);
        Collections.shuffle(shuffled);

        for(int num : shuffled) {
            TextView tv = createNumberView(num);
            numberPool.addView(tv);
        }
    }

    private TextView createNumberView(int number) {
        TextView tv = new TextView(this);
        tv.setText(String.valueOf(number));
        tv.setTag(number);
        tv.setTextSize(24);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                120,
                100
        );
        params.setMargins(12, 12, 12, 12);

        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.draggable_bg);
        tv.setPadding(16, 16, 16, 16);

        tv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("number", ((TextView) v).getText());
                v.startDragAndDrop(data, new View.DragShadowBuilder(v), v, 0);
                return true;
            }
            return false;
        });

        return tv;
    }

    private void updateQuestionText() {
        if(isAscending) {
            questionTitle.setText("ASCENDING ORDER");
            questionText.setText("Drag numbers from SMALLEST to LARGEST");
        } else {
            questionTitle.setText("DESCENDING ORDER");
            questionText.setText("Drag numbers from LARGEST to SMALLEST");
        }
    }

    private void checkOrder() {
        ArrayList<Integer> placedNumbers = new ArrayList<>();
        for (int id : targetBoxIds) {
            TextView box = findViewById(id);
            Object tag = box.getTag();
            if (tag == null) return;
            placedNumbers.add((Integer) tag);
        }

        boolean correct = true;
        for(int i=0; i<placedNumbers.size()-1; i++) {
            if(isAscending ? placedNumbers.get(i) > placedNumbers.get(i+1)
                    : placedNumbers.get(i) < placedNumbers.get(i+1)) {
                correct = false;
                break;
            }
        }

        if(correct) {
            playSound(R.raw.correct, this::clearBoxesAndGenerateNewQuestion);
            Toast.makeText(this, "Correct! Well done!", Toast.LENGTH_SHORT).show();
        } else {
            playSound(R.raw.wrong, this::resetToOriginalOrder);
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearBoxesAndGenerateNewQuestion() {

        for(int id : targetBoxIds) {
            TextView box = findViewById(id);
            box.setText("");
            box.setTag(null);
            box.setBackgroundResource(R.drawable.empty_box_bg);
        }

        new Handler(Looper.getMainLooper()).postDelayed(this::generateNewQuestion, 500);
    }

    private void resetToOriginalOrder() {
        numberPool.animate()
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction(() -> {
                    numberPool.setAlpha(1f);
                });
    }

    private void playSound(int soundRes, Runnable onCompletion) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, soundRes);
            if (mediaPlayer != null) {
                synchronized (activeMediaPlayers) {
                    activeMediaPlayers.add(mediaPlayer);
                }

                mediaPlayer.setOnCompletionListener(mp -> {
                    synchronized (activeMediaPlayers) {
                        activeMediaPlayers.remove(mp);
                    }
                    mp.release();
                    new Handler(Looper.getMainLooper()).post(onCompletion);
                });
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("Audio", "Error playing sound", e);
            new Handler(Looper.getMainLooper()).post(onCompletion);
        }
    }

    @Override
    protected void onDestroy() {
        synchronized (activeMediaPlayers) {
            for (MediaPlayer mp : activeMediaPlayers) {
                try {
                    if (mp.isPlaying()) mp.stop();
                    mp.release();
                } catch (Exception e) {
                    Log.e("MediaCleanup", "Error releasing media", e);
                }
            }
            activeMediaPlayers.clear();
        }
        super.onDestroy();
    }

    private void resetGameState() {
        numberPool.removeAllViews();
        originalOrder.clear();
    }
}

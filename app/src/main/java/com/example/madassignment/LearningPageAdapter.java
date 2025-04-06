package com.example.madassignment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class LearningPageAdapter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_list);

        RecyclerView recyclerView = findViewById(R.id.video_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<VideoItem> videos = Arrays.asList(
                new VideoItem(
                        "YhOf0H_gLP8",
                        "Video: Comparing Numbers",
                        "Learn about more and less through video examples",
                        CompareNumbersActivity.class
                ),
                new VideoItem(
                        "4cnXDt7TgTg",
                        "Video: Ordering Numbers",
                        "Understand number sequences visually",
                        OrderNumbersActivity.class
                ),
                new VideoItem(
                        "G05AgnEGmgw",
                        "Video: Composing Numbers",
                        "Master number combinations with video guidance",
                        ComposeNumbersActivity.class
                )
        );

        recyclerView.setAdapter(new VideoListAdapter(this, videos));
    }
}

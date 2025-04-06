package com.example.madassignment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class VideoPlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_player);

        String videoId = getIntent().getStringExtra("video_id");
        Class<?> targetClass = (Class<?>) getIntent().getSerializableExtra("target_class");

        if (videoId != null && targetClass != null) {
            YoutubeVideoFragment fragment = YoutubeVideoFragment.newInstance(videoId, targetClass);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
    }
}

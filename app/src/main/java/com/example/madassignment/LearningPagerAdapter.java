package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LearningPagerAdapter extends FragmentStateAdapter {
    private final String[] videoIds;
    private final String[] titles;

    public LearningPagerAdapter(FragmentActivity fa) {
        super(fa);
        this.videoIds = fa.getResources().getStringArray(R.array.video_ids);
        this.titles = fa.getResources().getStringArray(R.array.learning_tabs);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return LearningVideoFragment.newInstance(videoIds[position], titles[position]);
    }

    @Override
    public int getItemCount() {
        return videoIds.length;
    }

    public String getPageTitle(int position) {
        return titles[position];
    }
}
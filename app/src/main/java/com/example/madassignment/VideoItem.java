package com.example.madassignment;

public class VideoItem {
    public String videoId;
    public String title;
    public String description;
    public Class<?> targetActivity;

    public VideoItem(String videoId, String title, String description, Class<?> targetActivity) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.targetActivity = targetActivity;
    }
}

package com.example.madassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private final Context context;
    private final List<VideoItem> videoItems;

    public VideoListAdapter(Context context, List<VideoItem> videoItems) {
        this.context = context;
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem item = videoItems.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    // Must be static and extend RecyclerView.ViewHolder
    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, description;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            description = itemView.findViewById(R.id.video_description);
        }
    }
}
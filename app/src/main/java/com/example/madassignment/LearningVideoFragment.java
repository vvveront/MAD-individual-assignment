package com.example.madassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LearningVideoFragment extends Fragment {

    private static final String ARG_VIDEO_ID = "video_id";
    private static final String ARG_TITLE = "title";

    public static LearningVideoFragment newInstance(String videoId, String title) {
        LearningVideoFragment fragment = new LearningVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_ID, videoId);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_video, container, false);
        WebView webView = view.findViewById(R.id.web_view);
        TextView titleView = view.findViewById(R.id.video_title);

        String videoId = getArguments() != null ? getArguments().getString(ARG_VIDEO_ID) : "";
        String title = getArguments() != null ? getArguments().getString(ARG_TITLE) : "";

        titleView.setText(title);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        String html = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +
                "?autoplay=0&controls=1\" frameborder=\"0\" allowfullscreen></iframe>";

        webView.loadData(html, "text/html", "utf-8");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WebView webView = requireView().findViewById(R.id.web_view);
        webView.destroy();
    }
}
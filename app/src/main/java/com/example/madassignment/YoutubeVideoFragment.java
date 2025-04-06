package com.example.madassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class YoutubeVideoFragment extends Fragment {
    private static final String ARG_VIDEO_ID = "video_id";
    private static final String ARG_TARGET_ACTIVITY = "target_activity";

    public static YoutubeVideoFragment newInstance(String videoId, Class<?> targetActivity) {
        YoutubeVideoFragment fragment = new YoutubeVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_ID, videoId);
        args.putSerializable(ARG_TARGET_ACTIVITY, targetActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_video, container, false);
        WebView webView = view.findViewById(R.id.web_view);
        Button practiceButton = view.findViewById(R.id.btn_practice);

        String videoId = requireArguments().getString(ARG_VIDEO_ID);
        Class<?> targetActivity = (Class<?>) requireArguments().getSerializable(ARG_TARGET_ACTIVITY);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        webSettings.setAllowFileAccess(false);
        webSettings.setAllowContentAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);

        webSettings.setDatabaseEnabled(false);
        webSettings.setDomStorageEnabled(false);
        webSettings.setGeolocationEnabled(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, android.webkit.WebResourceRequest request) {
                if (!request.getUrl().getHost().contains("youtube.com")) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        String html = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +
                "?autoplay=0&controls=1\" frameborder=\"0\" allowfullscreen></iframe>";

        webView.loadData(html, "text/html", "utf-8");

        practiceButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), targetActivity)));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WebView webView = requireView().findViewById(R.id.web_view);
        webView.destroy();
    }
}
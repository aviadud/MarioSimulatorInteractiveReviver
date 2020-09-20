package com.example.mariosimulatorinteractivereviver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoView extends YouTubeBaseActivity {
    private static final String TAG = "VideoView";

    private YouTubePlayerView youTubePlayerView;
    private Button playButton;
    private EditText videoIdEditText;
    private EditText millisecondsEditText;

    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayer.OnInitializedListener youtubeInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        playButton = findViewById(R.id.playButton);
        videoIdEditText = findViewById(R.id.editTextVideoId);
        millisecondsEditText = findViewById(R.id.editTextMilliseconds);

        youtubeInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Youtube player initialization succeeded");
                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.loadVideo("EFeeKPXC-HA", 66000);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Youtube player initialization failed");
            }
        };

        youTubePlayerView.initialize(Constants.YOUTUBE_API_KEY, youtubeInitializedListener);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "play button clicked");
                mYouTubePlayer.loadVideo(videoIdEditText.getText().toString(), Integer.parseInt(millisecondsEditText.getText().toString()));
            }
        });

    }
}
package com.example.mariosimulatorinteractivereviver;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mariosimulatorinteractivereviver.utilities.DataLoader;
import com.example.mariosimulatorinteractivereviver.utilities.NavigationDataBase;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends YouTubeBaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TIMER_DELAY_MILLIS = 250;

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private RelativeLayout buttonsLayout;
    private ArrayList<Button> optionsButtons;
    private boolean actionTime;
    private NavigationDataBase navigationDataBase;
    private NavigationDataBase.Scene currentScene;
    private NavigationDataBase.Scene.ControlTime currentControlTime;
    private int currentControlTimeIndex;

    private String currentVideoId;
    private int currentTimeStamp;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateButtonsLayout();
            timerHandler.postDelayed(this, TIMER_DELAY_MILLIS);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYouTubePlayerView = findViewById(R.id.mainYoutubePlayerView);
        buttonsLayout = findViewById(R.id.buttonsView);

        try {
            JSONObject databaseJson = new JSONObject(DataLoader.jsonFilePathToString(getResources().openRawResource(R.raw.simulator_navigation)));
            navigationDataBase = new NavigationDataBase(databaseJson);
            currentScene = navigationDataBase.getScene(0);
            currentVideoId = currentScene.getVideoId();
            currentTimeStamp = 0;
            currentControlTimeIndex = 0;
            currentControlTime = currentScene.getControlTimes().get(currentControlTimeIndex);
            actionTime = currentControlTime.getTimeStampStart() <= currentTimeStamp;
            if (actionTime){
                optionsButtons = DataLoader.addOptionsButtonsToLayout(currentControlTime, buttonsLayout, this);
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            Log.d(TAG, "Can't load database");
        }


        mYouTubePlayerView.initialize(Constants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Youtube player initialization succeeded");
                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.loadVideo(currentVideoId, currentTimeStamp);
                mYouTubePlayer.setPlaybackEventListener(playbackEventListener);
                updateButtonsLayout();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Youtube player initialization failed");
            }
        });

        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                updateButtonsLayout();
            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {
                if (i < currentControlTime.getTimeStampStart() ||
                        (currentControlTime.getTimeStampEnd() != NavigationDataBase.Scene.ControlTime.END_OF_VIDEO_TIME_STAMP && currentControlTime.getTimeStampEnd() <= i)) {
                    updateButtonsLayout();
                }
            }
        };

    }

    private void updateButtonsLayout() {
        if (mYouTubePlayer != null) {
            int currentTimeMillis = mYouTubePlayer.getCurrentTimeMillis();
            // remove buttons if the current video time is out of range of current time control.
            if (actionTime &&
                    (currentTimeMillis < currentControlTime.getTimeStampStart() ||
                            (currentControlTime.getTimeStampEnd() != NavigationDataBase.Scene.ControlTime.END_OF_VIDEO_TIME_STAMP && currentControlTime.getTimeStampEnd() <= currentTimeMillis))){
                for (Button button : optionsButtons) {
                    buttonsLayout.removeView(button);
                }
                actionTime = false;
            }
            // check if
            ArrayList<NavigationDataBase.Scene.ControlTime> controlTimes = currentScene.getControlTimes();
            for (NavigationDataBase.Scene.ControlTime controlTime : controlTimes) {
                if (controlTime.getTimeStampStart() <= currentTimeMillis &&
                        (currentTimeMillis < controlTime.getTimeStampEnd() ||
                                controlTime.getTimeStampEnd() == NavigationDataBase.Scene.ControlTime.END_OF_VIDEO_TIME_STAMP)) {
                    if(!currentControlTime.equals(controlTime)) {
                        actionTime = true;
                        currentControlTime = controlTime;
                        optionsButtons = DataLoader.addOptionsButtonsToLayout(currentControlTime, buttonsLayout, this);
                    }
                    break;
                }
            }
        }
    }

    private void loadScene(int sceneId){
        if (navigationDataBase != null && mYouTubePlayer != null){
            if (0 <= sceneId && sceneId < navigationDataBase.getNumberOfScenes()){
                currentScene = navigationDataBase.getScene(sceneId);
                currentVideoId = currentScene.getVideoId();
                mYouTubePlayer.loadVideo(currentVideoId);
                currentTimeStamp = 0;
                currentControlTimeIndex = 0;
                currentControlTime = currentScene.getControlTimes().get(currentControlTimeIndex);
                actionTime = currentControlTime.getTimeStampStart() <= currentTimeStamp;
                if (actionTime){
                    optionsButtons = DataLoader.addOptionsButtonsToLayout(currentControlTime, buttonsLayout, this);
                }
            }
            else {
                Log.d(TAG, "Invalid scene ID given to load");
            }
        }
        else {
            Log.d(TAG, "can't load scene/video because Navigation DataBase or YouTubePlayer aren't initiated");
        }

    }


}
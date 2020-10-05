package com.example.mariosimulatorinteractivereviver;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

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
    private boolean youTubePlayerInitiated = false;
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
            int sceneId = 0;
            if (savedInstanceState != null){
                sceneId = savedInstanceState.getInt("currentSceneId");
            }
            currentScene = navigationDataBase.getScene(sceneId);
            currentVideoId = currentScene.getVideoId();
            if (savedInstanceState != null) {
                currentTimeStamp = savedInstanceState.getInt("currentTimeStamp");
            }
            else{
                currentTimeStamp = 0;
            }
            currentControlTimeIndex = 0;
            currentControlTime = currentScene.getControlTimes().get(currentControlTimeIndex);
            actionTime = currentControlTime.getTimeStampStart() <= currentTimeStamp;
            if (actionTime){
                optionsButtons = addOptionsButtons(currentControlTime);
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
                youTubePlayerInitiated = true;
                mYouTubePlayer.loadVideo(currentVideoId, currentTimeStamp);
                mYouTubePlayer.setPlaybackEventListener(playbackEventListener);
                updateButtonsLayout();
                timerHandler.postDelayed(timerRunnable, TIMER_DELAY_MILLIS);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentScene != null && youTubePlayerInitiated){
            outState.putInt("currentSceneId", currentScene.getId());
            outState.putInt("currentTimeStamp", mYouTubePlayer.getCurrentTimeMillis());
        }
        if (optionsButtons != null){
            removeOptionsButtons();
        }
    }

    private void updateButtonsLayout() {
        if (youTubePlayerInitiated) {
            int currentTimeMillis = mYouTubePlayer.getCurrentTimeMillis();
            // remove buttons if the current video time is out of range of current time control.
            if (actionTime &&
                    (currentTimeMillis < currentControlTime.getTimeStampStart() ||
                            (currentControlTime.getTimeStampEnd() != NavigationDataBase.Scene.ControlTime.END_OF_VIDEO_TIME_STAMP && currentControlTime.getTimeStampEnd() <= currentTimeMillis))){
                removeOptionsButtons();
                actionTime = false;
            }
            // check if the current video time is in some control time range and update.
            ArrayList<NavigationDataBase.Scene.ControlTime> controlTimes = currentScene.getControlTimes();
            for (NavigationDataBase.Scene.ControlTime controlTime : controlTimes) {
                if (controlTime.getTimeStampStart() <= currentTimeMillis &&
                        (currentTimeMillis < controlTime.getTimeStampEnd() ||
                                controlTime.getTimeStampEnd() == NavigationDataBase.Scene.ControlTime.END_OF_VIDEO_TIME_STAMP)) {
                    if(!currentControlTime.equals(controlTime) || !actionTime) {
                        actionTime = true;
                        currentControlTime = controlTime;
                        optionsButtons = addOptionsButtons(currentControlTime);
                    }
                    break;
                }
            }
        }
    }

    private void loadScene(int sceneId){
        if (navigationDataBase != null && youTubePlayerInitiated){
            if (0 <= sceneId && sceneId < navigationDataBase.getNumberOfScenes()){
                if (optionsButtons != null){
                    removeOptionsButtons();
                }
                currentScene = navigationDataBase.getScene(sceneId);
                currentVideoId = currentScene.getVideoId();
                currentTimeStamp = 0;
                mYouTubePlayer.loadVideo(currentVideoId, currentTimeStamp);
                currentControlTimeIndex = 0;
                currentControlTime = currentScene.getControlTimes().get(currentControlTimeIndex);
                actionTime = currentControlTime.getTimeStampStart() <= currentTimeStamp;
                if (actionTime){
                    optionsButtons = addOptionsButtons(currentControlTime);
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


    private ArrayList<Button> addOptionsButtons(final NavigationDataBase.Scene.ControlTime controlTime){
        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.button_margin);
        ArrayList<Button> result = new ArrayList<>(controlTime.getNumberOfOptions());
        for (int i = 0; i < controlTime.getNumberOfOptions(); i++) {
            Button currentButton = new Button(this);
            currentButton.setId(i);
            currentButton.setText(controlTime.getOptionButton(i));
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadScene(controlTime.getOptionSceneId(v.getId()));
                }
            });

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }
            else{
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i-1);
            }
            //layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

            buttonsLayout.addView(currentButton, layoutParams);
            result.add(currentButton);
        }
        return result;
    }

    private void removeOptionsButtons(){
        if (optionsButtons != null) {
            for (Button button : optionsButtons) {
                buttonsLayout.removeView(button);
            }
            optionsButtons = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
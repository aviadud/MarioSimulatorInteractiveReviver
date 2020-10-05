package com.example.mariosimulatorinteractivereviver;

import android.os.Bundle;
import android.util.Log;
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

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer mYouTubePlayer;
    private RelativeLayout buttonsLayout;
    private NavigationDataBase navigationDataBase;
    private NavigationDataBase.Scene currentScene;

    private String currentVideoId;
    private int currentTimeStamp;


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
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Youtube player initialization failed");
            }
        });

        ArrayList<NavigationDataBase.Scene.ControlTime> controlTimes = currentScene.getControlTimes();
        for (int i = 0; i < controlTimes.size(); i++) {
                /*
                JSONObject currentControlTime = jsonArray.getJSONObject(i);
                JSONArray buttonsNames = currentControlTime.getJSONArray("options");
                for (int j=0; j < buttonsNames.length(); j++){
                    Button currentButton = new Button(this);
                    currentButton.setText(buttonsNames.getJSONObject(j).getString("name"));
                    RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    buttonsLayout.addView(currentButton, buttonLayoutParams);
                }
                 */
        }

    }

}
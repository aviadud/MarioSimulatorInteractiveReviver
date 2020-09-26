package com.example.mariosimulatorinteractivereviver;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mariosimulatorinteractivereviver.utilities.DataLoader;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends YouTubeBaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer mYouTubePlayer;
    private RelativeLayout buttonsLayout;
    private JSONObject database;
    private JSONObject sceneData;

    private String currentVideoId;
    private int currentTimeStamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYouTubePlayerView = findViewById(R.id.mainYoutubePlayerView);
        buttonsLayout = findViewById(R.id.buttonsView);

        currentTimeStamp = 0; //remove later

        try {
            database = new JSONObject(DataLoader.JsonFilePathToString(getResources().openRawResource(R.raw.simulator_navigation)));
            sceneData = database.getJSONObject(getString(R.string.database_entry_point));
            currentVideoId = sceneData.getString(getString(R.string.database_video_id_key));
        }
        catch (IOException | JSONException ex){
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

        try {
            JSONArray jsonArray = sceneData.getJSONArray("controlTime");
            for (int i=0;i < jsonArray.length(); i++){
                JSONObject currentControlTime = jsonArray.getJSONObject(i);
                JSONArray buttonsNames = currentControlTime.getJSONArray("options");
                for (int j=0; j < buttonsNames.length(); j++){
                    Button currentButton = new Button(this);
                    currentButton.setText(buttonsNames.getJSONObject(j).getString("name"));
                    RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    buttonsLayout.addView(currentButton, buttonLayoutParams);
                }
            }
        }
        catch (JSONException ex){

        }

    }

}
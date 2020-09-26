package com.example.mariosimulatorinteractivereviver.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NavigationDataBase {
    static final private String[] BUTTONS_LIST = {"z", "a", "left", "none", "r", "b", "up", "right", "down"};

    public static class Scene{
        public static class ControlTime{
            private int timeStampStart;
            private int timeStampEnd;
            private int numberOfOptions;
            private String[] optionName;
            private int[] optionSceneId;
            private String[] optionButton;
        }
        private String videoId;
        private int id;
        private int playlistIndex;
        private String name;
        private int toadsDestroyed;
        private boolean gameOver;
        private ArrayList<ControlTime> controlTimes;

        public Scene(JSONObject sceneData){
            try {
                videoId = sceneData.getString("videoId");
                name = sceneData.getString("name");
                id = sceneData.getInt("id");
                playlistIndex = sceneData.getInt("playlistIndex");
                toadsDestroyed = sceneData.getInt("toadsDestroyed");
                gameOver = sceneData.getBoolean("gameOver");


            }
            catch (JSONException ex){

            }

        }

        public int getId() {
            return id;
        }

        public int getPlaylistIndex() {
            return playlistIndex;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getName() {
            return name;
        }

        public int getToadsDestroyed() {
            return toadsDestroyed;
        }

        public boolean isGameOver() {
            return gameOver;
        }

        public ArrayList<ControlTime> getControlTimes() {
            return controlTimes;
        }
    }

    private int numberOfScenes;
    private ArrayList<Scene> scenes;

    public NavigationDataBase(JSONObject database){
        numberOfScenes = database.length();
        scenes = new ArrayList<>(numberOfScenes);
        try {
            for (Iterator<String> it = database.keys(); it.hasNext(); ) {
                String sceneKey = it.next();
                Scene scene = new Scene(database.getJSONObject(sceneKey));
                scenes.set(scene.id, scene);
            }
        }
        catch (JSONException ex){

        }

    }

    public int getNumberOfScenes() {
        return numberOfScenes;
    }

    public Scene getScene(int id){
        return scenes.get(id);
    }
}


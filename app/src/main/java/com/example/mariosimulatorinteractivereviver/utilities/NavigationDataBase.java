package com.example.mariosimulatorinteractivereviver.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigationDataBase {
    static final private String[] BUTTONS_LIST = {"z", "a", "left", "none", "r", "b", "up", "right", "down"};

    public static class Scene {
        public static class ControlTime {
            private int timeStampStart;
            private int timeStampEnd;
            private int numberOfOptions;
            private String[] optionName;
            private int[] optionSceneId;
            private String[] optionButton;

            public ControlTime(JSONObject controlTimeData) throws JSONException {
                timeStampStart = timeStampToMillisecond(controlTimeData.getString("timeStampStart"));
                timeStampEnd = timeStampToMillisecond(controlTimeData.getString("timeStampEnd"));
                JSONArray options = controlTimeData.getJSONArray("options");
                numberOfOptions = options.length();
                optionName = new String[numberOfOptions];
                optionSceneId = new int[numberOfOptions];
                optionButton = new String[numberOfOptions];
                for (int i = 0; i < numberOfOptions; i++) {
                    JSONObject optionData = options.getJSONObject(i);
                    optionName[i] = optionData.getString("name");
                    optionSceneId[i] = optionData.getInt("scene");
                    optionButton[i] = optionData.getString("button");
                }
            }

            public int getTimeStampStart() {
                return timeStampStart;
            }

            public int getTimeStampEnd() {
                return timeStampEnd;
            }

            public int getNumberOfOptions() {
                return numberOfOptions;
            }

            public String getOptionName(int index){
                return optionName[index];
            }

            public int getOptionSceneId(int index){
                return optionSceneId[index];
            }

            public String getOptionButton(int index){
                return optionButton[index];
            }
        }

        private String videoId;
        private int id;
        private int playlistIndex;
        private String name;
        private int toadsDestroyed;
        private boolean gameOver;
        private ArrayList<ControlTime> controlTimes;

        public Scene(JSONObject sceneData) throws JSONException {
            videoId = sceneData.getString("videoId");
            name = sceneData.getString("name");
            id = sceneData.getInt("id");
            playlistIndex = sceneData.getInt("playlistIndex");
            toadsDestroyed = sceneData.getInt("toadsDestroyed");
            gameOver = sceneData.getBoolean("gameOver");
            JSONArray controlTimesData = sceneData.getJSONArray("controlTime");
            controlTimes = new ArrayList<>();
            for (int i = 0; i < controlTimesData.length(); i++) {
                controlTimes.add(new ControlTime(controlTimesData.getJSONObject(i)));
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
    private Scene scenes[];

    public NavigationDataBase(JSONObject database) throws JSONException {
        numberOfScenes = database.length();
        scenes = new Scene[numberOfScenes];

        for (Iterator<String> it = database.keys(); it.hasNext(); ) {
            String sceneKey = it.next();
            Scene scene = new Scene(database.getJSONObject(sceneKey));
            scenes[scene.getId()] = scene;
        }


    }

    public int getNumberOfScenes() {
        return numberOfScenes;
    }

    public Scene getScene(int id) {
        return scenes[id];
    }

    public static int timeStampToMillisecond(String input) {
        final Pattern timeStampPattern = Pattern.compile("(\\d?\\d):(\\d\\d)");
        Matcher timeStampMatcher = timeStampPattern.matcher(input);
        if (timeStampMatcher.matches()) {
            return (Integer.parseInt(timeStampMatcher.group(1)) * 60 + Integer.parseInt(timeStampMatcher.group(2))) * 1000;
        } else {
            if (input.equals("inf")) {
                return -1;
            } else return 0;
        }
    }
}


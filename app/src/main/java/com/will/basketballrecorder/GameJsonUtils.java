package com.will.basketballrecorder;

import android.content.Context;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by willhennessy on 4/5/16.
 */
public class GameJsonUtils {

    /*
     *  Read the JSON file and return an ArrayList of all GameEvents in the specified game
     */
    public static ArrayList<GameEvent> initGameEvents(Context context, String gameName, String fileName) {
        ArrayList<GameEvent> result = new ArrayList<GameEvent>();
        try {
            // Check if file exists to read GameEvents
            File file = context.getFileStreamPath(fileName);
            if (!file.exists())
                return result;
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            // Each game in json
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonReader.nextName();
                String name = jsonReader.nextString();
                // Game currently being displayed
                if (Objects.equals(name, gameName)) {
                    jsonReader.nextName();
                    jsonReader.beginArray();
                    // Each event in a game
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        String action = jsonReader.nextString();
                        jsonReader.nextName();
                        float x = (float) jsonReader.nextDouble();
                        jsonReader.nextName();
                        float y = (float) jsonReader.nextDouble();
                        jsonReader.endObject();
                        GameEvent event = new GameEvent(action, x, y);
                        result.add(event);
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue();
                    jsonReader.skipValue();
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /*
     *  Write a GameEvent into the JSON file
     */
    public static void saveGameEvent(Context context, String gameName, String fileName, GameEvent event) {
        if (event.getAction() == null)
            return;
        try {
            // Check if file exists to write to, if not then create
            File file = context.getFileStreamPath(fileName);
            if (!file.exists()) {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                JsonWriter jsonWriter = new JsonWriter(osw);
                jsonWriter.beginArray();
                writeGame(gameName, event, jsonWriter);
                jsonWriter.endArray();
                jsonWriter.close();
                //Toast.makeText(context, "Action Saved!", Toast.LENGTH_SHORT).show();
                return;

            }
            boolean eventAdded = false;
            // Create temp file to write to while reading permenant file
            FileOutputStream fos = context.openFileOutput("tmp_" + fileName, Context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(osw);
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            jsonWriter.beginArray();
            // Each game in json
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonWriter.beginObject();
                String name = jsonReader.nextName();
                String game_value = jsonReader.nextString();
                jsonWriter.name(name).value(game_value);
                name = jsonReader.nextName();
                jsonWriter.name(name);
                jsonReader.beginArray();
                jsonWriter.beginArray();
                String value;
                // Each event in a game
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    jsonWriter.beginObject();
                    name = jsonReader.nextName();
                    value = jsonReader.nextString();
                    jsonWriter.name(name).value(value);
                    name = jsonReader.nextName();
                    Float dbl_value = (float) jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    name = jsonReader.nextName();
                    dbl_value = (float) jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    jsonReader.endObject();
                    jsonWriter.endObject();
                }
                // Add event to end of array
                if (Objects.equals(game_value, gameName)) {
                    writeEvent(event, jsonWriter);
                    eventAdded = true;
                }
                jsonReader.endArray();
                jsonWriter.endArray();
                jsonReader.endObject();
                jsonWriter.endObject();
            }
            // New game
            if (!eventAdded) {
                writeGame(gameName, event, jsonWriter);
            }
            jsonReader.endArray();
            jsonWriter.endArray();
            jsonReader.close();
            jsonWriter.close();
            // Change temp file to permenant file
            File oldfile = context.getFileStreamPath(fileName);
            File newfile = context.getFileStreamPath("tmp_" + fileName);
            newfile.renameTo(oldfile);
            //Toast.makeText(context, "Action Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Helper: Write a new game into JSON
     */
    private static void writeGame(String gameName, GameEvent event, JsonWriter jsonWriter) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(gameName);
            jsonWriter.name("events");
            jsonWriter.beginArray();
            writeEvent(event, jsonWriter);
            jsonWriter.endArray();
            jsonWriter.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Helper: Write a new event into the JSON file
     */
    private static void writeEvent(GameEvent event, JsonWriter jsonWriter) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("action").value(event.getAction());
            jsonWriter.name("x").value(event.getX());
            jsonWriter.name("y").value(event.getY());
            jsonWriter.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Read the JSON file get game names
     */
    public static ArrayList<String> getGameNames(Context context, String fileName) {
        ArrayList<String> gameNames = new ArrayList<String>();
        try {
            // Check if file exists to read GameEvents
            File file = context.getFileStreamPath(fileName);
            if (!file.exists())
                return gameNames;
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            // Each game in json
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonReader.skipValue();
                String name = jsonReader.nextString();
                gameNames.add(name);
                jsonReader.skipValue();
                jsonReader.skipValue();
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Reverse list so most recent games occur first
        Collections.reverse(gameNames);
        return gameNames;
    }

    /*
     *  Read the JSON file to get game stats
     */
    public static Map<String, Integer> getGameStats(Context context, String gameName, String fileName) {
        Map<String, Integer> gameStats = new HashMap<String, Integer>();
        try {
            // Check if file exists to read GameEvents
            File file = context.getFileStreamPath(fileName);
            if (!file.exists())
                return gameStats;
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            // Each game in json
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonReader.nextName();
                String name = jsonReader.nextString();
                // Game currently being displayed
                if (Objects.equals(name, gameName)) {
                    jsonReader.skipValue();
                    jsonReader.beginArray();
                    // Each event in a game
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        String action = jsonReader.nextString();
                        if (gameStats.containsKey(action))
                            gameStats.put(action, gameStats.get(action) + 1);
                        else
                            gameStats.put(action, 1);
                        jsonReader.skipValue();
                        jsonReader.skipValue();
                        jsonReader.skipValue();
                        jsonReader.skipValue();
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue();
                    jsonReader.skipValue();
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameStats;
    }
}

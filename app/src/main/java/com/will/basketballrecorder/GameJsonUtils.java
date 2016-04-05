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
            File file = context.getFileStreamPath(fileName);
            if (!file.exists())
                return result;
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                jsonReader.nextName();
                String name = jsonReader.nextString();
                System.out.println(name);
                if (Objects.equals(name, gameName)) {
                    jsonReader.nextName();
                    jsonReader.beginArray();
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
            File file = context.getFileStreamPath(fileName);
            if (!file.exists()) {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                JsonWriter jsonWriter = new JsonWriter(osw);
                jsonWriter.beginArray();
                jsonWriter.beginObject();
                jsonWriter.name("name").value(gameName);
                jsonWriter.name("events");
                jsonWriter.beginArray();
                jsonWriter.beginObject();
                jsonWriter.name("action").value(event.getAction());
                jsonWriter.name("x").value(event.getX());
                jsonWriter.name("y").value(event.getY());
                jsonWriter.endObject();
                jsonWriter.endArray();
                jsonWriter.endObject();
                jsonWriter.endArray();
                jsonWriter.close();
                //Toast.makeText(context, "Action Saved!", Toast.LENGTH_SHORT).show();
                return;

            }
            FileOutputStream fos = context.openFileOutput("tmp_" + fileName, Context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            JsonWriter jsonWriter = new JsonWriter(osw);
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            JsonReader jsonReader = new JsonReader(isr);
            jsonReader.beginArray();
            jsonWriter.beginArray();
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
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    jsonWriter.beginObject();
                    name = jsonReader.nextName();
                    value = jsonReader.nextString();
                    jsonWriter.name(name).value(value);
                    name = jsonReader.nextName();
                    Double dbl_value = jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    name = jsonReader.nextName();
                    dbl_value = jsonReader.nextDouble();
                    jsonWriter.name(name).value(dbl_value);
                    jsonReader.endObject();
                    jsonWriter.endObject();
                }
                if (Objects.equals(game_value, gameName)) {
                    jsonWriter.beginObject();
                    jsonWriter.name("action").value(event.getAction());
                    jsonWriter.name("x").value(event.getX());
                    jsonWriter.name("y").value(event.getY());
                    jsonWriter.endObject();
                }
                jsonReader.endArray();
                jsonWriter.endArray();
                jsonReader.endObject();
                jsonWriter.endObject();
            }
            jsonReader.endArray();
            jsonWriter.endArray();
            jsonReader.close();
            jsonWriter.close();
            File oldfile = context.getFileStreamPath(fileName);
            File newfile = context.getFileStreamPath("tmp_" + fileName);
            newfile.renameTo(oldfile);
            //Toast.makeText(context, "Action Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

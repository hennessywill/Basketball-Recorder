package com.will.basketballrecorder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class GameActivity extends FragmentActivity {

    private String gameName;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameName = getIntent().getStringExtra("game_name");
        fileName = getIntent().getStringExtra("file_name");
        setContentView(R.layout.activity_game);
    }

    public String getGameName() {
        return gameName;
    }

    public String getFileName() { return fileName; }

}

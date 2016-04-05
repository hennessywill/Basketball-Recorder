package com.will.basketballrecorder;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    private String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameName = getIntent().getStringExtra("game_name");
        setContentView(R.layout.activity_game);
    }

    public String getGameName() {
        return gameName;
    }

}

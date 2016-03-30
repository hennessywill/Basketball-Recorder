package com.will.basketballrecorder;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    // Actions:  shot, miss, penalty, assist,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}

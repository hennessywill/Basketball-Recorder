package com.will.basketballrecorder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Map;

public class GameActivity extends FragmentActivity {

    private String gameName;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameName = getIntent().getStringExtra("game_name");
        fileName = getIntent().getStringExtra("file_name");
        setContentView(R.layout.activity_game);

//        Map<String, Integer> gameStats = GameJsonUtils.getGameStats(getBaseContext(), gameName, fileName);
//        FGM_COUNT = getStat("score", gameStats);
//        FGM_ATTEMPTED = FGM_COUNT + getStat("miss", gameStats);
//        FGM_PERCENT = FGM_COUNT / FGM_ATTEMPTED;
//        ASSISTS = getStat("assist", gameStats);
//        REBOUNDS = getStat("rebound", gameStats);
//        STEALS = getStat("steal", gameStats);
//        FOULS = getStat("foul", gameStats);
//        POINTS_SCORED = FGM_COUNT * 2;
//
//
//        TextView points_made = (TextView) findViewById(R.id.points_scored);
//        points_made.setText(POINTS_SCORED+"");
//
//        TextView assists = (TextView) findViewById(R.id.assists);
//        assists.setText(ASSISTS+"");
//
//        TextView steals = (TextView) findViewById(R.id.steals);
//        steals.setText(STEALS+"");
    }

    public String getGameName() {
        return gameName;
    }

    public String getFileName() { return fileName; }

    private static int getStat(String action, Map<String, Integer> gameStats) {
        if (gameStats.containsKey(action))
            return gameStats.get(action);
        return 0;
    }


}

package com.will.basketballrecorder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

public class StatsActivity extends Activity {

    private int HOME_SCORE = 0;
    private int AWAY_SCORE = 0;

    //Temp
    String gameName = "game1";
    String fileName = "data.json";

    // Stats for player of interest
    private int POINTS_SCORED;
    private int FGM_COUNT; // FGM : Field Goals Made
    private int FGM_ATTEMPTED;
    private double FGM_PERCENT;
    private int FTM_COUNT = 0; // FTM: Free Throws Made
    private int FTM_ATTEMPTED = 0;
    private double FTM_PERCENT = 0.0;
    private int ASSISTS;
    private int BLOCKS = 0;
    private int TURNOVERS = 0;
    private int FOULS;
    private int REBOUNDS;
    private int STEALS;

    // TODO - Add Rebounds/Steals
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        TextView home_score = (TextView) findViewById(R.id.home_score);
        home_score.setText(HOME_SCORE);

        TextView away_score = (TextView) findViewById(R.id.away_score);
        away_score.setText(AWAY_SCORE);

        TextView points_made = (TextView) findViewById(R.id.points_scored);
        points_made.setText(POINTS_SCORED);

        TextView fgm_count = (TextView) findViewById(R.id.fgm_count);
        fgm_count.setText(FGM_COUNT);

        TextView ftm_count = (TextView) findViewById(R.id.ftm_count);
        ftm_count.setText(FTM_COUNT);

        TextView assists = (TextView) findViewById(R.id.assists);
        assists.setText(ASSISTS);

        TextView blocks = (TextView) findViewById(R.id.blocks);
        blocks.setText(BLOCKS);

        TextView fouls = (TextView) findViewById(R.id.fouls);
        fouls.setText(FOULS);

    }

    private void initStats() {
        Map<String, Integer> gameStats = GameJsonUtils.getGameStats(getBaseContext(), gameName, fileName);
        FGM_COUNT = getStat("score", gameStats);
        FGM_ATTEMPTED = FGM_COUNT + getStat("miss", gameStats);
        FGM_PERCENT = FGM_COUNT / FGM_ATTEMPTED;
        ASSISTS = getStat("assist", gameStats);
        REBOUNDS = getStat("rebound", gameStats);
        STEALS = getStat("steal", gameStats);
        FOULS = getStat("foul", gameStats);
        POINTS_SCORED = FGM_COUNT * 2;
    }

    private static int getStat(String action, Map<String, Integer> gameStats) {
        if (gameStats.containsKey(action))
            return gameStats.get(action);
        return 0;
    }

//    public StatsActivity(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setOnTouchListener(this);
//        // to later implement option to increment or decrement scores
//    }

}

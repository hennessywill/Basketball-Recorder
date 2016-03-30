package com.will.basketballrecorder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends Activity {

    private int HOME_SCORE = 0;
    private int AWAY_SCORE = 0;

    // Stats for player of interest
    private int POINTS_SCORED = 0;
    private int FGM_COUNT = 0; // FGM : Field Goals Made
    private int FGM_ATTEMPTED = 0;
    private double FGM_PERCENT = 0.0;
    private int FTM_COUNT = 0; // FTM: Free Throws Made
    private int FTM_ATTEMPTED = 0;
    private double FTM_PERCENT = 0.0;
    private int ASSISTS = 0;
    private int BLOCKS = 0;
    private int TURNOVERS = 0;
    private int FOULS = 0;

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

//    public StatsActivity(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setOnTouchListener(this);
//        // to later implement option to increment or decrement scores
//    }

}

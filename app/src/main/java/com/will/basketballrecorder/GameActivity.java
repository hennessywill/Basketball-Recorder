package com.will.basketballrecorder;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Map<String, Integer> gameStats = GameJsonUtils.getGameStats(getBaseContext(), gameName, fileName);
            int scoreInt = getStat("score", gameStats);
            String points = String.valueOf(2 * scoreInt);
            String scores = String.valueOf(scoreInt);
            String attempts = String.valueOf(scoreInt + getStat("miss", gameStats));
            String assists = String.valueOf(getStat("assist", gameStats));
            String rebounds = String.valueOf(getStat("rebound", gameStats));
            String steals = String.valueOf(getStat("steal", gameStats));

            ((TextView) findViewById(R.id.points)).setText(points);
            ((TextView) findViewById(R.id.percentage)).setText(scores + "/" + attempts);
            ((TextView) findViewById(R.id.assists)).setText(assists);
            ((TextView) findViewById(R.id.rebounds)).setText(rebounds);
            ((TextView) findViewById(R.id.steals)).setText(steals);
        }
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

    /*
     *  Create an intent to send the stats report via email
     */
    public void sendStatsEmail(View v) {
        String emailBody = generateEmailBody();
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Basketball Stats: "+gameName);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    /*
     *  Generates and returns an HTML string to send in the email body
     */
    private String generateEmailBody() {
        Map<String, Integer> gameStats = GameJsonUtils.getGameStats(getBaseContext(), gameName, fileName);
        int scoreInt = getStat("score", gameStats);
        String points = String.valueOf(2 * scoreInt);
        String scores  = String.valueOf(scoreInt);
        String attempts = String.valueOf(scoreInt + getStat("miss", gameStats));
        String assists = String.valueOf(getStat("assist", gameStats));
        String rebounds = String.valueOf(getStat("rebound", gameStats));
        String steals = String.valueOf(getStat("steal", gameStats));

        return new StringBuilder()
                .append("Basketbal Statistics Summary\n")
                .append(gameName)
                .append("\n\nPoints: ")
                .append(points)
                .append("\nFG Percentage: ")
                .append(scores+"/"+attempts)
                .append("\nAssists: ")
                .append(assists)
                .append("\nRebounds: ")
                .append(rebounds)
                .append("\nSteals: ")
                .append(steals)
                .toString();
    }

}

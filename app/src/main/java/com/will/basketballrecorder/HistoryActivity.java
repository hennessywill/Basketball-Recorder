package com.will.basketballrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends Activity {

    private static final String FILE_NAME = "data.json";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            String title = getResources().getString(R.string.history_list_title);
            toolbar.setTitle(title);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GameActivity.class);
                String gameName = generateNewGameName();
                i.putExtra("game_name", gameName);
                i.putExtra("file_name", FILE_NAME);
                startActivity(i);
            }
        });

        mListView = (ListView) findViewById(R.id.files_list);
        loadGamesList();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gameName = (String) parent.getAdapter().getItem(position);
                Intent i = new Intent(view.getContext(), GameActivity.class);
                i.putExtra("game_name", gameName);
                i.putExtra("file_name", FILE_NAME);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGamesList();
    }

    /*
     *  Refresh the list of games by reading the JSON file
     */
    private void loadGamesList() {
        ArrayList<String> gameNames = GameJsonUtils.getGameNames(getBaseContext(), FILE_NAME);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, gameNames);
        mListView.setAdapter(adapter);
    }

    /*
     *  Generate a new game name using the current date and time
     */
    private String generateNewGameName() {
        SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy, h:mm a", getResources().getConfiguration().locale);
        return df.format(Calendar.getInstance().getTime());
    }
}

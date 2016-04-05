package com.will.basketballrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GameActivity.class);
                startActivity(i);
            }
        });

        String[] fileNames = readGameNames();
        ListView listView = (ListView) findViewById(R.id.files_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, fileNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gameName = (String) parent.getAdapter().getItem(position);
                Intent i = new Intent(view.getContext(), GameActivity.class);
                i.putExtra("game_name", gameName);
                startActivity(i);
            }
        });
    }

    // Read the saved text file and return a list of all stored game names
    private String[] readGameNames() {
        // TODO - read the games from the JSON into a String array
        String[] res = {"game1", "game2", "game3", "game4"};
        return res;
    }

}

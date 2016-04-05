package com.will.basketballrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {

    private String FILE_NAME = "data.json";

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

        ArrayList<String> gameNames = GameJsonUtils.getGameNames(getBaseContext(), FILE_NAME);
        ListView listView = (ListView) findViewById(R.id.files_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, gameNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}

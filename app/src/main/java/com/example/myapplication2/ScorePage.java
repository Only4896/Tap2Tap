package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ScorePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        SharedPreferences sharedPreferences = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<Map.Entry<String, ?>> list = new ArrayList<>(allEntries.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ?>>() {
            public int compare(Map.Entry<String, ?> o1, Map.Entry<String, ?> o2) {
                return Integer.compare(Integer.parseInt(o2.getValue().toString()), Integer.parseInt(o1.getValue().toString()));
            }
        });
        int numScores = Math.min(list.size(), 25);

        // Create the table view and add each score to a new row
        TableLayout tableLayout = findViewById(R.id.table_layout);
        for (int i = 0; i < numScores; i++) {
            TableRow row = new TableRow(this);
            TextView rankTextView = new TextView(this);
            rankTextView.setText(Integer.toString(i + 1));
            row.addView(rankTextView);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(list.get(i).getKey());
            row.addView(nameTextView);

            TextView scoreTextView = new TextView(this);
            scoreTextView.setText(list.get(i).getValue().toString());
            row.addView(scoreTextView);
            tableLayout.addView(row);
        }
    }
}

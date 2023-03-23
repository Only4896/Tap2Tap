package com.example.myapplication2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GamePage extends AppCompatActivity {

    private View[] circleViews;
    private View currentHighlightedView;
    int scores = 0;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        // Get references to the circle views
        circleViews = new View[] {
                findViewById(R.id.circle1),
                findViewById(R.id.circle2),
                findViewById(R.id.circle3),
                findViewById(R.id.circle4)
        };

        // Set click listeners for the circle views
        for (View circleView : circleViews) {
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == currentHighlightedView) {
                        // User touched the highlighted view correctly
                        unhighlightView(v);
                        highlightRandomView();
                        scores++;
                        TextView tv = (TextView) findViewById(R.id.scores);
                        tv.setText("Scores: " + scores);
                    }
                    // Ignore touch event if the view is not currently highlighted
                }
            });
        }


        // Highlight a random view initially
        highlightRandomView();

        // Set up the countdown timer for 5 seconds
        timer = new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView tv = (TextView) findViewById(R.id.countdown);
                tv.setText("Time left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // Disable all circle views and show a message that time is up
                for (View circleView : circleViews) {
                    circleView.setClickable(false);
                }
                Toast.makeText(GamePage.this, "Time is up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        //Level 2 button
        Button lvl2button = findViewById(R.id.ContinueButton);
        lvl2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamePage.this, GamePage2.class);
                intent.putExtra("Scores", scores);
                startActivity(intent);
            }
        });

        //Exit button
        Button exitbutton = findViewById(R.id.ExitButton);
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the user's score
                int userScore = scores;

                // Check if the user's score is in the top 25 highest scores
                SharedPreferences sharedPreferences = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
                Map<String, ?> allEntries = sharedPreferences.getAll();
                List<Integer> scoresList = new ArrayList<>();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    int score = Integer.parseInt(entry.getValue().toString());
                    scoresList.add(score);
                }
                scoresList.add(userScore);
                Collections.sort(scoresList, Collections.reverseOrder());
                if (scoresList.indexOf(userScore) <= 24) {
                    // Create a dialog box to prompt the user to enter their name and score
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePage.this);
                    builder.setTitle("Congratulations! Please enter your name...");

                    // Set up the layout for the dialog box
                    LinearLayout layout = new LinearLayout(GamePage.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    // Add an edit text for the user to enter their name
                    final EditText nameEditText = new EditText(GamePage.this);
                    nameEditText.setHint("Name");
                    layout.addView(nameEditText);

                    // Show the user's score in the dialog box
                    TextView scoreTextView = new TextView(GamePage.this);
                    scoreTextView.setText("  Score: " + userScore); // assumes that userScore is already defined
                    layout.addView(scoreTextView);

                    builder.setView(layout);

                    // Set up the buttons for the dialog box
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Retrieve the user's name from the edit text
                            String userName = nameEditText.getText().toString();

                            // Save the user's name and score to the shared preferences
                            SharedPreferences sharedPreferences = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(userName, Integer.toString(userScore));
                            editor.apply();

                            // Display the top 25 highest scores
                            Map<String, ?> allEntries = sharedPreferences.getAll();
                            List<Map.Entry<String, ?>> entriesList = new ArrayList<>(allEntries.entrySet());
                            Collections.sort(entriesList, new Comparator<Map.Entry<String, ?>>() {
                                @Override
                                public int compare(Map.Entry<String, ?> o1, Map.Entry<String, ?> o2) {
                                    int v1 = Integer.parseInt(o1.getValue().toString());
                                    int v2 = Integer.parseInt(o2.getValue().toString());
                                    return v2 - v1;
                                }
                            });

                            AlertDialog.Builder scoreTableBuilder = new AlertDialog.Builder(GamePage.this);
                            scoreTableBuilder.setTitle("Top 25 Highest Scores");
                            String scoreTableMessage = "";
                            int maxEntries = Math.min(entriesList.size(), 25);
                            for (int j = 0; j < maxEntries; j++) {
                                Map.Entry<String, ?> entry = entriesList.get(j);
                                scoreTableMessage += (j + 1) + "  --->  " + entry.getKey() + ": " + entry.getValue() + "\n";
                            }
                            scoreTableBuilder.setMessage(scoreTableMessage);
                            scoreTableBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(GamePage.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            scoreTableBuilder.show();
                        }
                    });

                    builder.setNegativeButton("Cancel", null);

                    // Show the dialog box
                    builder.show();
                } else {
                    // Show the dialog box with the user's score
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePage.this);
                    builder.setTitle("Oops! You're not in Top 25 highest score list...");
                    builder.setMessage("Your score: " + userScore);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GamePage.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });

    }

    private void highlightRandomView() {
        // Unhighlight the current view if there is one
        if (currentHighlightedView != null) {
            unhighlightView(currentHighlightedView);
        }

        // Select a random view to highlight
        Random random = new Random();
        int randomIndex = random.nextInt(circleViews.length);
        View viewToHighlight = circleViews[randomIndex];

        // Create a GradientDrawable with a circular shape and yellow color
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        int color = Color.parseColor("#FBEC5D");
        drawable.setColor(color);

        // Set the GradientDrawable as the background of the view
        viewToHighlight.setBackground(drawable);
        currentHighlightedView = viewToHighlight;
    }

    private void unhighlightView(View view) {
        // Create a GradientDrawable with a circular shape and gray color
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.GRAY);

        // Set the GradientDrawable as the background of the view
        view.setBackground(drawable);
        currentHighlightedView = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}

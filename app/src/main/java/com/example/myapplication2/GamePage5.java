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

public class GamePage5 extends AppCompatActivity {

    private View[] circleViews;
    private View currentHighlightedView;
    int scores5;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page5);

        // Get the integer value from the calling activity
        Intent intent = getIntent();
        scores5 = intent.getIntExtra("Scores", 0);

        // Get references to the circle views
        circleViews = new View[] {
                findViewById(R.id.circle1),
                findViewById(R.id.circle2),
                findViewById(R.id.circle3),
                findViewById(R.id.circle4),
                findViewById(R.id.circle5),
                findViewById(R.id.circle6),
                findViewById(R.id.circle7),
                findViewById(R.id.circle8),
                findViewById(R.id.circle9),
                findViewById(R.id.circle10),
                findViewById(R.id.circle11),
                findViewById(R.id.circle12),
                findViewById(R.id.circle13),
                findViewById(R.id.circle14),
                findViewById(R.id.circle15),
                findViewById(R.id.circle16),
                findViewById(R.id.circle17),
                findViewById(R.id.circle18),
                findViewById(R.id.circle19),
                findViewById(R.id.circle20),
                findViewById(R.id.circle21),
                findViewById(R.id.circle22),
                findViewById(R.id.circle23),
                findViewById(R.id.circle24),
                findViewById(R.id.circle25),
                findViewById(R.id.circle26),
                findViewById(R.id.circle27),
                findViewById(R.id.circle28),
                findViewById(R.id.circle29),
                findViewById(R.id.circle30),
                findViewById(R.id.circle31),
                findViewById(R.id.circle32),
                findViewById(R.id.circle33),
                findViewById(R.id.circle34),
                findViewById(R.id.circle35),
                findViewById(R.id.circle36)
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
                        scores5++;
                        TextView tv = (TextView) findViewById(R.id.scores5);
                        tv.setText("Scores: " + scores5);
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
                Toast.makeText(GamePage5.this, "Time is up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        //Exit button
        Button exitbutton = findViewById(R.id.ExitButton);
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the user's score
                int userScore = scores5;

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePage5.this);
                    builder.setTitle("Congratulations! Please enter your name...");

                    // Set up the layout for the dialog box
                    LinearLayout layout = new LinearLayout(GamePage5.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    // Add an edit text for the user to enter their name
                    final EditText nameEditText = new EditText(GamePage5.this);
                    nameEditText.setHint("Name");
                    layout.addView(nameEditText);

                    // Show the user's score in the dialog box
                    TextView scoreTextView = new TextView(GamePage5.this);
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

                            AlertDialog.Builder scoreTableBuilder = new AlertDialog.Builder(GamePage5.this);
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
                                    Intent intent = new Intent(GamePage5.this, MainActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePage5.this);
                    builder.setTitle("Oops! You're not in Top 25 highest score list...");
                    builder.setMessage("Your score: " + userScore);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GamePage5.this, MainActivity.class);
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

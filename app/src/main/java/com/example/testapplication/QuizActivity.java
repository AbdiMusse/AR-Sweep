package com.example.testapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    public static int SCORE = 0;
    private QuestionManager questionManager;
    private int counter = 30;
    private TextView textView;
    private ArrayList<Integer> colourCheck = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
    private Button highlightedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // This is the SCORE based on the questions that the user had
        textView = findViewById(R.id.score);
        textView.setText("Score is " + SCORE);
        // The timer for the challenge.
        final TextView counttime = findViewById(R.id.countdown);
        new CountDownTimer(counter * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counttime.setText("Timer left is " + counter);
                counter--;
            }

            @Override
            public void onFinish() {
                // This takes you to a new page with your result
                cancel();
                timedisplaypage(findViewById(android.R.id.content));
            }
        }.start();

        setQuestion();
    }

    public void timedisplaypage(View view) {
        Intent intent = new Intent(this, TimeDisplayActivity.class);
        startActivity(intent);
    }

    /**
     * Highlight/unhighlight the picked button and change the colour flag accordingly.
     * Unhighlight all the others buttons and reset their colour flag to zero.
     *
     * @param view the view that the button needs for onClick.
     */
    public void pickButton(View view) {
        switch (view.getId()) {
            case R.id.button1:
                changeBackground(findViewById(R.id.button1), 0);
                resetOtherButtons(findViewById(R.id.button2), 1);
                resetOtherButtons(findViewById(R.id.button3), 2);
                resetOtherButtons(findViewById(R.id.button4), 3);
                break;
            case R.id.button2:
                resetOtherButtons(findViewById(R.id.button1), 0);
                changeBackground(findViewById(R.id.button2), 1);
                resetOtherButtons(findViewById(R.id.button3), 2);
                resetOtherButtons(findViewById(R.id.button4), 3);
                break;
            case R.id.button3:
                resetOtherButtons(findViewById(R.id.button1), 0);
                resetOtherButtons(findViewById(R.id.button2), 1);
                changeBackground(findViewById(R.id.button3), 2);
                resetOtherButtons(findViewById(R.id.button4), 3);
                break;
            case R.id.button4:
                resetOtherButtons(findViewById(R.id.button1), 0);
                resetOtherButtons(findViewById(R.id.button2), 1);
                resetOtherButtons(findViewById(R.id.button3), 2);
                changeBackground(findViewById(R.id.button4), 3);
                break;
        }
    }

    /**
     * Checks the button the user clicked and see if it's correct and assign the SCORE accordingly.
     *
     * @param view the view that the onClick needs.
     */
    public void checkAnswer(View view) {
        // Check if the user has clicked on a button
        if (highlightedButton == null) {
            Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, result(highlightedButton), Toast.LENGTH_SHORT).show();

            // Set all the colours flag back to 0 so the colour is default
            for (int i = 0; i < colourCheck.size(); i++) {
                colourCheck.set(i, 0);
            }

            // Set the new questions
            setQuestion();
            // Reset the picked button
            highlightedButton = null;
        }
    }

    private void setQuestion() {
        TextView question = findViewById(R.id.question);
        Button option1 = findViewById(R.id.button1);
        option1.setBackgroundColor(Color.LTGRAY);
        Button option2 = findViewById(R.id.button2);
        option2.setBackgroundColor(Color.LTGRAY);
        Button option3 = findViewById(R.id.button3);
        option3.setBackgroundColor(Color.LTGRAY);
        Button option4 = findViewById(R.id.button4);
        option4.setBackgroundColor(Color.LTGRAY);

        questionManager = new QuestionManager();
        question.setText(questionManager.getQuestion());
        option1.setText(questionManager.pickOption());
        option2.setText(questionManager.pickOption());
        option3.setText(questionManager.pickOption());
        option4.setText(questionManager.pickOption());
    }

    private String result(Button button) {
        String buttonAnswer = button.getText().toString();
        AtomicReference<String> result = new AtomicReference<>("Wrong Answer.");
        questionManager.getDisplayOptions().forEach(s -> {
            if (buttonAnswer.equals(s.split(";")[1])
                    && s.split(";")[0].equals("true")) {
                SCORE = SCORE + 15;
                textView.setText("Score is : " + SCORE);
                result.set("Correct!!");
            }
        });
        return result.get();
    }

    private void changeBackground(Button button, int colourState) {
        if (colourCheck.get(colourState) % 2 == 0) {
            button.setBackgroundColor(Color.argb(106, 185, 111, 1));
            colourCheck.set(colourState, 1);
            highlightedButton = button;
        } else {
            button.setBackgroundColor(Color.LTGRAY);
            colourCheck.set(colourState, 0);
            highlightedButton = null;
        }
    }

    private void resetOtherButtons(Button button, int colourState) {
        button.setBackgroundColor(Color.LTGRAY);
        colourCheck.set(colourState, 0);
    }

}
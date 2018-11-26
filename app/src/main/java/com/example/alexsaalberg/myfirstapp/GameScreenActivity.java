package com.example.alexsaalberg.myfirstapp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameScreenActivity extends AppCompatActivity {

    TriviaGame triviaGame;
    View rootView;

    public static final int correctBackgroundColor = 0xFF81c784; // light greenish
    public static final int incorrectBackgroundColor = 0xFFFF5252; // light reddish


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Alex", "In the game!");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen);


        int numPlayers = 2;
        triviaGame = new TriviaGame(getApplicationContext(), numPlayers);

        for (int i = 1; i <= numPlayers; i++) {
            TriviaGame.Question question = triviaGame.getCurrentQuestion(i);
            updatePlayerQuestion(i, question);
        }


        ActionBar ab = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
        ab.hide();
    }

    public void answerQuestion(int player, int choice) {
        int playerId = player - 1; // Player1 has id 0

        final Button button = getPlayerButton(player, choice);

        if (triviaGame.selectAnswer(player, choice-1)) {
            // correct
            setButtonBackground(button, correctBackgroundColor, 1000);
        } else {
            // incorrect
            setButtonBackground(button, incorrectBackgroundColor, 1000);
        }

        TriviaGame.Question question = triviaGame.nextQuestion(player);

        if(question == null) { // question==null means no more questions for this player
            TriviaGame.Question dummyQuestion = new TriviaGame.Question();
            dummyQuestion.question = "Finished!";
            dummyQuestion.answers = new String[]{":)",":)",":)",":)",};
            updatePlayerQuestion(player, dummyQuestion); // updates the players gui to this question.

            setPlayerButtons(player, false); // Disable this player's buttons
        } else {
            updatePlayerQuestion(player, question); // updates the players gui to this question.

        }

    }

    public void setButtonBackground(final Button button, int color, int millTime) {
        //final Drawable background = button.getBackground();
        //final int oldColor = button.getBackground().getColor();

        //button.setBackgroundColor(color);
        button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        new CountDownTimer(millTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {
                button.getBackground().clearColorFilter();
            }
        }.start();
    }

    public Button getPlayerButton(int player, int buttonNum) {
        String buttonID = "p" + player + "a" + (buttonNum); //buttonNum is 1indexed
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        Button button = ((Button) findViewById(resID));
        return button;
    }

    public void setPlayerButtons(int player, boolean enabled) {
        for(int i=0; i<4; i++) {
            String buttonID = "p" + player + "a" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = ((Button) findViewById(resID));
            button.setEnabled(enabled);
        }
    }

    public void updatePlayerQuestion(int player, TriviaGame.Question question) {
        String textViewId = "p" + player + "q";
        int qId = getResources().getIdentifier(textViewId, "id", getPackageName());
        TextView textView = ((TextView) this.findViewById(qId));
        final int activity_game_screen = R.layout.activity_game_screen;

        if(question == null) {
            textView.setText("Finished!");
        } else {
            textView.setText(question.question);
        }

        for(int i=0; i<4; i++) {
            String buttonID = "p" + player + "a" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = ((Button) findViewById(resID));
            if(question == null) {
                button.setText(":)");
            } else {
                if(button == null) {
                    Log.e("alex", "button is null");
                }
                button.setText(question.answers[i]);
            }
        }
    }

    public void selectP1A1(View view) {
        Log.i("alex","Player 1 selected answer 1");
        answerQuestion(1, 1);

    }
    public void selectP1A2(View view) {
        Log.i("alex","Player 1 selected answer 2");
        answerQuestion(1, 2);
    }
    public void selectP1A3(View view) {
        Log.i("alex","Player 1 selected answer 3");
        answerQuestion(1, 3);
    }
    public void selectP1A4(View view) {
        Log.i("alex","Player 1 selected answer 4");
        answerQuestion(1, 4);
    }

    public void selectP2A1(View view) {
        Log.i("alex","Player 2 selected answer 1");
        answerQuestion(2, 1);
    }
    public void selectP2A2(View view) {
        Log.i("alex","Player 2 selected answer 2");
        answerQuestion(2, 2);
    }
    public void selectP2A3(View view) {
        Log.i("alex","Player 2 selected answer 3");
        answerQuestion(2, 3);
    }
    public void selectP2A4(View view) {
        Log.i("alex","Player 2 selected answer 4");
        answerQuestion(2, 4);
    }
}

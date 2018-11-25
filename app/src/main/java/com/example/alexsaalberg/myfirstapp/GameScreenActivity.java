package com.example.alexsaalberg.myfirstapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Alex", "In the game!");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_screen);

        //Button button = findViewById(R.id.p1a1);
        //button.setText("yay!");

        int numPlayers = 2;
        triviaGame = new TriviaGame(numPlayers);
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

        if (triviaGame.selectAnswer(player, choice)) {
            // correct
        } else {
            // incorrect
        }

        TriviaGame.Question question = triviaGame.nextQuestion(player);

        updatePlayerQuestion(player, question);

        if(question == null) {
            // this player is finished
            // todo: deactive buttons, give other player time limit.
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

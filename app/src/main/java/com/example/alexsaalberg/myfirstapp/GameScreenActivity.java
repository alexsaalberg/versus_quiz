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

import java.util.ArrayList;
import java.util.Arrays;

public class GameScreenActivity extends AppCompatActivity {

    TriviaGame triviaGame;
    View rootView;

    public static final int correctBackgroundColor = 0xFF81c784; // light greenish
    public static final int incorrectBackgroundColor = 0xFFFF5252; // light reddish

    private class PlayerGUI {
        TextView correctText;
        TextView timeLeftText;
        TextView pointsText;

        TextView questionText;
        Button[] buttons;
    }

    PlayerGUI[] playerGUIs;
    final static int numPlayers = 2;
    final static int numAnswers = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Inflate layout
        setContentView(R.layout.activity_game_screen);
        
        // Initialize GUI objects
        playerGUIs = new PlayerGUI[numPlayers];
        for(int playerId = 0; playerId < numPlayers; playerId++) {
            playerGUIs[playerId] = generatePlayerGUI(playerId);
        }

        // Initialize the trivia game
        triviaGame = new TriviaGame(getApplicationContext(), numPlayers);

        // Populate the player guis with question 0
        for (int playerId = 0; playerId < numPlayers; playerId++) {
            Question question = triviaGame.getCurrentQuestion(playerId);
            updatePlayerGUI(playerId, question);
        }

        // Remove the action bar
        ActionBar ab = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
        ab.hide();
    }

    public PlayerGUI generatePlayerGUI(int playerId) {
        /*
         *  There's probably a better place to document this, but...
         *
         *  playerId is 0 indexed, where "Player One" has id 0
         *  playerNum is 1 indexed, "where "Player One" has num 1
         *
         *  playerId is used in the activities and other internal classes.
         *  playerNum is used for the userfacing stuff (INCLUDING THE VIEW IDS) !!!
         *  (which is why it appears here)
         *
         *  same goes for buttonId / buttonNum

         */
        
        PlayerGUI playerGUI = new PlayerGUI();

        int playerNum = playerId + 1;
        playerGUI.correctText = (TextView) getViewByStringId("p"+playerNum+"correct");
        playerGUI.timeLeftText = (TextView) getViewByStringId("p"+playerNum+"timeLeft");
        playerGUI.pointsText = (TextView) getViewByStringId("p"+playerNum+"points");

        playerGUI.questionText = (TextView) getViewByStringId("p"+playerNum+"q");
        playerGUI.buttons = new Button[numAnswers];
        for(int buttonId = 0; buttonId < numAnswers; buttonId++) {
            int buttonNum = buttonId + 1;
            playerGUI.buttons[buttonId] = (Button) getViewByStringId("p"+playerNum+"a"+buttonNum);
        }

        return playerGUI;
    }

    public View getViewByStringId(String stringId) {
        int id = getResources().getIdentifier(stringId, "id", getPackageName());
        View result = findViewById(id);

        if(result == null) {
            Log.e("alex", "View obtained via stringId=\""+stringId+"\" is null");
        }

        return result;
    }

    /*
     * Activated via the GUI.
     * Indicates that a player clicked a button to answer a question.
     */
    public void answerQuestion(int playerId, int choiceId) {
        final Button button = playerGUIs[playerId].buttons[choiceId];

        if (triviaGame.selectAnswer(playerId, choiceId)) {
            // correct
            setButtonBackground(button, correctBackgroundColor, 1000);
        } else {
            // incorrect
            setButtonBackground(button, incorrectBackgroundColor, 1000);
        }

        Question question = triviaGame.nextQuestion(playerId);

        if(question == null) { // question==null means no more questions for this player
            Question dummyQuestion = new Question();
            dummyQuestion.question = "Finished!";
            dummyQuestion.correct_answer = ":)";
            dummyQuestion.incorrect_answers = new String[]{":)",":)",":)"};
            updatePlayerGUI(playerId, dummyQuestion); // updates the players gui to this question.

            setPlayerButtons(playerId, false); // Disable this player's buttons
        } else {
            updatePlayerGUI(playerId, question); // updates the players gui to this question.
        }
    }

    /*
     * Sets `button`'s background color to `color` for `millTime` milliseconds
     */
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

    /*
     * Enable or disable all of playerId's buttons
     */
    public void setPlayerButtons(int playerId, boolean newEnabledState) {
        for(Button button : playerGUIs[playerId].buttons) {
            button.setEnabled(newEnabledState);
        }
    }


    public void updatePlayerGUI(int playerId, Question question) {
        PlayerGUI gui = playerGUIs[playerId];
        PlayerState playerState = triviaGame.getPlayerState(playerId);

        gui.questionText.setText(question.question);

        ArrayList<Integer> buttonNums = new ArrayList<Integer>();
        buttonNums.add(0);
        buttonNums.add(1);
        buttonNums.add(2);
        buttonNums.add(3);

        for(int buttonId = 0; buttonId < gui.buttons.length; buttonId++) {
            Button button = gui.buttons[buttonId];

            String newText;

            if(buttonId < playerState.correctAnswerNum) {
                newText = question.incorrect_answers[buttonId];
            } else if (buttonId == playerState.correctAnswerNum) {
                newText = question.correct_answer;
            } else {
                newText = question.incorrect_answers[buttonId - 1];
            }

            button.setText(newText);
        }
    }

    /* Things activated by layouts */
    public void selectP1A1(View view) {
        Log.i("alex","Player 1 selected answer 1");
        answerQuestion(0, 0);

    }
    public void selectP1A2(View view) {
        Log.i("alex","Player 1 selected answer 2");
        answerQuestion(0, 1);
    }
    public void selectP1A3(View view) {
        Log.i("alex","Player 1 selected answer 3");
        answerQuestion(0, 2);
    }
    public void selectP1A4(View view) {
        Log.i("alex","Player 1 selected answer 4");
        answerQuestion(0, 3);
    }

    public void selectP2A1(View view) {
        Log.i("alex","Player 2 selected answer 1");
        answerQuestion(1, 0);
    }
    public void selectP2A2(View view) {
        Log.i("alex","Player 2 selected answer 2");
        answerQuestion(1, 1);
    }
    public void selectP2A3(View view) {
        Log.i("alex","Player 2 selected answer 3");
        answerQuestion(1, 2);
    }
    public void selectP2A4(View view) {
        Log.i("alex","Player 2 selected answer 4");
        answerQuestion(1, 3);
    }
}

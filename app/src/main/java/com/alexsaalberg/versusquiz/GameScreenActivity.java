package com.alexsaalberg.versusquiz;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alexsaalberg.versusquiz.R;

import java.util.ArrayList;

public class GameScreenActivity extends AppCompatActivity {

    TriviaGame triviaGame;
    View rootView;

    public static final int correctBackgroundColor = 0xFFA1D7A4; // darker greenish
    public static final int incorrectBackgroundColor = 0xFFFF5252; // light reddish

    private class PlayerGUI {
        TextView correctText;
        TextView timeLeftText;
        TextView pointsText;

        TextView questionText;

        View hud;

        Button[] buttons;
    }

    PlayerGUI[] playerGUIs;
    final static int numPlayers = 2;
    final static int numAnswers = 4;
    final static int gameplayTimeLimit = 30; // in seconds

    final static String EXTRA_P1_SCORE_NAME = "PlayerOneScore";
    final static String EXTRA_P2_SCORE_NAME = "PlayerTwoScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide nav buttons (< O SQUARE)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        
        // Inflate layout
        setContentView(R.layout.activity_game_screen);
        
        // Initialize GUI objects
        playerGUIs = new PlayerGUI[numPlayers];
        for(int playerId = 0; playerId < numPlayers; playerId++) {
            playerGUIs[playerId] = generatePlayerGUI(playerId);
        }

        Intent intent = getIntent();
        String jsonQuestionSet = intent.getStringExtra(DownloadQuestionActivity.EXTRA_JSON_QUESTION_SET_STRING);

        // Initialize the trivia game
        triviaGame = new TriviaGame(getApplicationContext(), numPlayers, jsonQuestionSet);

        // Populate the player guis with question 0
        for (int playerId = 0; playerId < numPlayers; playerId++) {
            Question question = triviaGame.getCurrentQuestion(playerId);
            updatePlayerGUI(playerId, question);

        }

        //start the timers
        int numSeconds = gameplayTimeLimit;
        new CountDownTimer(numSeconds*1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilFinished = (int) (millisUntilFinished / 1000);
                for(int i = 0; i < numPlayers; i++) {
                    playerGUIs[i].timeLeftText.setText(secondsUntilFinished + " seconds left");
                }
            }
            @Override
            public void onFinish() {
                // game is always over for last one of these to activate.
                goToGameOver();
            }
        }.start();

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

        playerGUI.hud = (View) getViewByStringId("p"+playerNum+"hud");

        return playerGUI;
    }

    public View getViewByStringId(String stringId) {
        int id = getResources().getIdentifier(stringId, "id", getPackageName());
        View result = findViewById(id);

        if(result == null) {
            //Log.e("alex", "View obtained via stringId=\""+stringId+"\" is null");
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
            //setViewBackground(playerGUIs[playerId].hud, correctBackgroundColor, 500);
            setViewBackground(button, correctBackgroundColor, 200);
            //setTextViewColor(playerGUIs[playerId].correctText, correctBackgroundColor, 500);
            //(playerGUIs[playerId].pointsText, correctBackgroundColor, 500);
        } else {
            // incorrect
            //setViewBackground(playerGUIs[playerId].hud, incorrectBackgroundColor, 500);
            setViewBackground(button, incorrectBackgroundColor, 200);
            //setTextViewColor(playerGUIs[playerId].correctText, incorrectBackgroundColor, 500);
            setTextViewColor(playerGUIs[playerId].pointsText, incorrectBackgroundColor, 500);
        }

        Question question = triviaGame.nextQuestion(playerId);

        if(question == null) { // question==null means no more questions for this player
            Question dummyQuestion = new Question();
            dummyQuestion.question = "Finished!";
            dummyQuestion.correct_answer = ":)";
            dummyQuestion.incorrect_answers = new String[]{":)",":)",":)"};
            updatePlayerGUI(playerId, dummyQuestion); // updates the players gui to this question.

            // note that this player is done
            triviaGame.endPlayersGame(playerId);
            checkForGameOver();

            setPlayerButtons(playerId, false); // Disable this player's buttons
        } else {
            updatePlayerGUI(playerId, question); // updates the players gui to this question.
        }
    }

    /*
     * Sets `button`'s background color to `color` for `millTime` milliseconds
     */
    public void setViewBackground(final View view, int color, int millTime) {
        //final Drawable background = button.getBackground();
        //final int oldColor = button.getBackground().getColor();

        //button.setBackgroundColor(color);
        view.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        new CountDownTimer(millTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {
                view.getBackground().clearColorFilter();
            }
        }.start();
    }

    public void setTextViewColor(final TextView view, int color, int millTime) {

        final int originalColor = view.getCurrentTextColor();
        view.setTextColor(color);

        new CountDownTimer(millTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {
                view.setTextColor(originalColor);
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

        // question
        gui.questionText.setText(question.question);

        // buttons
        ArrayList<Integer> buttonNums = new ArrayList<Integer>();

        int numAnswers = question.incorrect_answers.length + 1;
        for(int buttonId = 0; buttonId < gui.buttons.length && buttonId < numAnswers; buttonId++) {
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

        // todo: clear text for buttons which were not updated
        for(int buttonId = numAnswers; buttonId < gui.buttons.length; buttonId++) {
            gui.buttons[buttonId].setText("");
        }

        // score stuff
        gui.pointsText.setText(playerState.points + " points");
        gui.correctText.setText(playerState.correct + "/"+ playerState.questionNum +" correct");
    }

    public boolean isGameOver() {
        for(int i = 0; i < numPlayers; i++) {
            if(! triviaGame.isPlayersGameOver(i)) {
                return false;
            }
        }

        return true;
    }

    public void checkForGameOver() {
        if (isGameOver() ) {
            // game over stuff
            goToGameOver();
        }
    }

    public void goToGameOver() {
            Intent intent = new Intent(this, GameOverActivity.class);
            intent.putExtra(EXTRA_P1_SCORE_NAME, triviaGame.getPlayersScore(0));
            intent.putExtra(EXTRA_P2_SCORE_NAME, triviaGame.getPlayersScore(1));
            //Log.i("Alex", "Going to the game over...");
            startActivity(intent);
    }

    /* Things activated by layouts */
    public void selectP1A1(View view) {
        //Log.i("alex","Player 1 selected answer 1");
        answerQuestion(0, 0);

    }
    public void selectP1A2(View view) {
        //Log.i("alex","Player 1 selected answer 2");
        answerQuestion(0, 1);
    }
    public void selectP1A3(View view) {
        //Log.i("alex","Player 1 selected answer 3");
        answerQuestion(0, 2);
    }
    public void selectP1A4(View view) {
        //Log.i("alex","Player 1 selected answer 4");
        answerQuestion(0, 3);
    }

    public void selectP2A1(View view) {
        //Log.i("alex","Player 2 selected answer 1");
        answerQuestion(1, 0);
    }
    public void selectP2A2(View view) {
        //Log.i("alex","Player 2 selected answer 2");
        answerQuestion(1, 1);
    }
    public void selectP2A3(View view) {
        //Log.i("alex","Player 2 selected answer 3");
        answerQuestion(1, 2);
    }
    public void selectP2A4(View view) {
        //Log.i("alex","Player 2 selected answer 4");
        answerQuestion(1, 3);
    }
}

package com.alexsaalberg.versusquiz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alexsaalberg.versusquiz.R;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Make fullscreen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        int p1Score = intent.getIntExtra(GameScreenActivity.EXTRA_P1_SCORE_NAME, 0);
        int p2Score = intent.getIntExtra(GameScreenActivity.EXTRA_P2_SCORE_NAME, 0);

        TextView p1ScoreTextView = (TextView) getViewByStringId("p1score");
        TextView p2ScoreTextView = (TextView) getViewByStringId("p2score");

        p1ScoreTextView.setText(""+p1Score);
        p2ScoreTextView.setText(""+p2Score);

        TextView winTextView = (TextView) getViewByStringId("winText");
        if(p1Score > p2Score) {
            winTextView.setText("Player One Wins!");
        } else if(p2Score > p1Score){
            winTextView.setText("Player Two Wins!");
        } else {
            winTextView.setText("It's a tie! :O");
        }

        // Remove the action bar
        ActionBar ab = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
        ab.hide();
    }

    public View getViewByStringId(String stringId) {
        int id = getResources().getIdentifier(stringId, "id", getPackageName());
        View result = findViewById(id);

        if(result == null) {
            //Log.e("alex", "View obtained via stringId=\""+stringId+"\" is null");
        }

        return result;
    }

    public void onMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //Log.i("Alex", "Going to the main menu...");
        startActivity(intent);
    }

    public void onPlayAgain(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        //Log.i("Alex", "Going to the game...");
        startActivity(intent);
    }
}

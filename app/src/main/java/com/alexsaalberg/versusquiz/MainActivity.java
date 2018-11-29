package com.alexsaalberg.versusquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alexsaalberg.versusquiz.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.alexsaalberg.MESSAGE";
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        // Remove the action bar
        ActionBar ab = getSupportActionBar();
        ab.hide();
    }

    public static Context getContext() {
        return context;
    }


    public void goToGame(View view) {
        Intent intent = new Intent(this, DownloadQuestionActivity.class);
        Log.i("Alex", "Going to the game...");
        startActivity(intent);
    }

}

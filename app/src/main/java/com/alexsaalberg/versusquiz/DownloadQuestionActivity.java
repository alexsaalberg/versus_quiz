package com.alexsaalberg.versusquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alexsaalberg.versusquiz.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DownloadQuestionActivity extends AppCompatActivity {

    public static final String EXTRA_JSON_QUESTION_SET_STRING = "jsonQuestionSetString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_question);

        new DownloadOpenTriviaQuestions().execute();
    }



    protected class DownloadOpenTriviaQuestions extends AsyncTask<Void, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(Void... params)
        {

            String str="https://opentdb.com/api.php?amount=10";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                //Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                // todo: check for improper success code
                // if so ... ? go back to main menu? update text?
                goToGame(response);
            }
        }
    }

    public void goToGame(JSONObject jsonQuestionSet) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        intent.putExtra(EXTRA_JSON_QUESTION_SET_STRING, jsonQuestionSet.toString());
        //Log.i("Alex", "Going to the game...");
        startActivity(intent);
    }
}

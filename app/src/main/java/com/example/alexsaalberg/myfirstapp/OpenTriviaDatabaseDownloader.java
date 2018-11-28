package com.example.alexsaalberg.myfirstapp;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OpenTriviaDatabaseDownloader {
    public class DownloadOptions {

    }

    public static String getQuestionJSON(Context context) {
        return loadAssetTextAsString(context, "example_questions");
    }

    private static String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            String result = buf.toString();
            return result;
        } catch (IOException e) {
            //Log.e("alex", "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //Log.e("alex", "Error closing asset " + name);
                }
            }
        }

        return null;
    }
}

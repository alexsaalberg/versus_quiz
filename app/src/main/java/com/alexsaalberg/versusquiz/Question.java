package com.alexsaalberg.versusquiz;

import android.os.Build;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Question {
    public String category;
    public String question_type;
    public String difficulty;
    public String question;
    public String correct_answer;
    public String[] incorrect_answers;

    public static Question[] parseJSON(final JSONObject jsonObject) {
        try {
            JSONObject triviaDatabaseResponse = jsonObject;
            int responseCode = triviaDatabaseResponse.getInt("response_code");
            if(responseCode != 0) {
                // todo: error
            }

            JSONArray jsonQuestions = triviaDatabaseResponse.getJSONArray("results");
            Question[] resultQuestions = new Question[jsonQuestions.length()];

            for(int i = 0; i < jsonQuestions.length(); i++) {
                JSONObject jsonQuestion = jsonQuestions.getJSONObject(i);
                Question question = new Question();

                question.category = jsonQuestion.getString("category");
                question.question_type = jsonQuestion.getString("type");
                question.difficulty = jsonQuestion.getString("difficulty");
                question.question = jsonQuestion.getString("question");
                question.correct_answer = jsonQuestion.getString("correct_answer");

                JSONArray incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers");
                question.incorrect_answers = new String[incorrectAnswers.length()];

                for(int j = 0; j < incorrectAnswers.length(); j++) {
                    question.incorrect_answers[j] = incorrectAnswers.getString(j);
                }

                resultQuestions[i] = fromHtmlQuestion(question);
            }

            return resultQuestions;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Question[] parseJSON(final String JSON_STRING) {
        try {
            return parseJSON(new JSONObject(JSON_STRING));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Question fromHtmlQuestion(Question original) {
        Question decoded = new Question();

        decoded.question = fromHtmlString(original.question);

        decoded.category = fromHtmlString(original.category);
        decoded.question_type = fromHtmlString(original.question_type);
        decoded.difficulty = fromHtmlString(original.difficulty);
        decoded.correct_answer = fromHtmlString(original.correct_answer);

        decoded.incorrect_answers = new String[original.incorrect_answers.length];
        for(int i = 0; i < original.incorrect_answers.length; i++) {
            decoded.incorrect_answers[i] = fromHtmlString(original.incorrect_answers[i]);
        }

        return decoded;
    }

    public static String fromHtmlString(String original) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(original, 0).toString();
        } else {
            return Html.fromHtml(original).toString();
        }
    }
}
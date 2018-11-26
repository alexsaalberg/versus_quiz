package com.example.alexsaalberg.myfirstapp;

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

    public static Question[] parseJSON(final String JSON_STRING) {
        try {
            JSONObject triviaDatabaseResponse = new JSONObject(JSON_STRING);
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
            }

            return resultQuestions;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
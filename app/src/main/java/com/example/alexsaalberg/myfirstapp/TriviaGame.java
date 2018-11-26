package com.example.alexsaalberg.myfirstapp;

import android.content.Context;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.Random;

public class TriviaGame {

    private Question[] questions;
    private PlayerState[] players;

    public TriviaGame(Context context, int numPlayers) {
        OpenTriviaDatabaseDownloader questionDownloader = new OpenTriviaDatabaseDownloader();

        String jsonQuestions = OpenTriviaDatabaseDownloader.getQuestionJSON(context);
        questions = Question.parseJSON(jsonQuestions);

        players = new PlayerState[numPlayers];
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayerState();
        }
    }

    public boolean selectAnswer(int playerId, int choiceNum) {
        if(players[playerId].correctAnswerNum == choiceNum) {
            players[playerId].correct++;
            players[playerId].points += 3;
            return true;
        } else {
            players[playerId].points -= 2;
            return false;
        }
    }

    public Question nextQuestion(int playerId) {
        players[playerId].questionNum += 1;

        int questionNum = players[playerId].questionNum;

        if(questionNum >= questions.length) {
            // this player has exhausted all questions.
            // give the other player a deadline.
            return null;
        } else {
            Random random = new Random();
            players[playerId].correctAnswerNum = random.nextInt(4); // random from {0,1,2,3}

            return questions[questionNum];
        }
    }

    public PlayerState getPlayerState(int playerId) {
        return players[playerId];
    }

    public Question getCurrentQuestion(int playerId) {
        return questions[players[playerId].questionNum];
    }
}

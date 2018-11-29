package com.alexsaalberg.versusquiz;

import android.content.Context;

import java.util.Random;

public class TriviaGame {

    private Question[] questions;
    private PlayerState[] players;

    public TriviaGame(Context context, int numPlayers, String jsonQuestionSet) {
        OpenTriviaDatabaseDownloader questionDownloader = new OpenTriviaDatabaseDownloader();

        // use default if for some reason we weren't given questions.
        if(jsonQuestionSet == null) {
            jsonQuestionSet = OpenTriviaDatabaseDownloader.getQuestionJSON(context);
        }

        questions = Question.parseJSON(jsonQuestionSet);


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
            players[playerId].points -= 1;
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
            // correct + incorrect
            int numQuestions = 1 + questions[questionNum].incorrect_answers.length;
            Random random = new Random();
            players[playerId].correctAnswerNum = random.nextInt(numQuestions); // random from {0,1,2,num-1}

            return questions[questionNum];
        }
    }

    public PlayerState getPlayerState(int playerId) {
        return players[playerId];
    }

    public Question getCurrentQuestion(int playerId) {
        return questions[players[playerId].questionNum];
    }

    public void endPlayersGame(int playerId) {
        players[playerId].gameOver = true;
    }

    public boolean isPlayersGameOver(int playerId) {
        return players[playerId].gameOver;
    }

    public int getPlayersScore(int playerId) {
        return players[playerId].points;
    }
}

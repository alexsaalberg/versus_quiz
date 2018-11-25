package com.example.alexsaalberg.myfirstapp;

import java.util.ArrayList;

public class TriviaGame {
    public class Question {
        String question;
        String[] answers;
        int correctAnswer;
    }

    public class PlayerState {
        int questionNum = 0;
        int correct = 0;
        int points = 0;
        public Question question;
    }

    private ArrayList<Question> questions;
    private PlayerState[] players;

    private ArrayList<Question> fetchNewQuestionSet() {
        ArrayList<Question> example = new ArrayList<Question>();
        Question q1 = new Question();
        q1.question="Which color is blue?";
        q1.answers = new String[]{"red", "orange", "purple", "blue"};
        q1.correctAnswer=3;

        for (int i = 0; i < 10; i++) {
            q1.question += ""+i;
            q1.answers[1] += ""+i;
            example.add(q1);
        }

        return example;
    }

    public TriviaGame(int numPlayers) {
        questions = fetchNewQuestionSet();
        players = new PlayerState[numPlayers];
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayerState();
            players[i].question = questions.get(0);
        }
    }

    public boolean selectAnswer(int player, int choice) {
        int playerNum = player - 1;

        int question = players[playerNum].questionNum;
        if(questions.get(question).correctAnswer == choice) {
            players[playerNum].correct++;
            players[playerNum].points++;
            return true;
        } else {
            players[playerNum].points--;
            // incorrect answer
            return false;
        }
    }

    public Question nextQuestion(int player) {
        int playerNum = player - 1;

        players[playerNum].questionNum += 1;

        if(players[playerNum].questionNum >= questions.size()) {
            // this player has exhausted all questions.
            // give the other player a deadline.
            return null;
        } else {
            // todo: update the question and 4 answers.
            players[playerNum].question = questions.get(players[playerNum].questionNum);
            return players[playerNum].question;
        }
    }

    public Question getCurrentQuestion(int player) {
        int playerId = player - 1;

        return players[playerId].question;
    }


}

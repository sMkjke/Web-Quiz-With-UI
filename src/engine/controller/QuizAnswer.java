package engine.controller;

import java.util.List;

public class QuizAnswer{

    List<Integer> answer;

    private QuizAnswer() {}

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }
}
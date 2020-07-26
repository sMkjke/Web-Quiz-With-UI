package engine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QuizDAO implements DAO {

    private List<Quiz> quizList;
    private AtomicInteger id;
    private Matcher matchAnswer;

    public QuizDAO() {
        this.quizList = new ArrayList<>();
        id = new AtomicInteger(1);
        matchAnswer = Pattern.compile("\\s*answer\\s*=\\s*(\\d+)\\s*", Pattern.CASE_INSENSITIVE)
                .matcher("");
    }


    @Override
    public Quiz get(int id) {
        try {
            return quizList.get(id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @Override
    public List<Quiz> getAll() {
        return quizList;
    }

    @Override
    public Quiz save(Quiz quiz) {
        quiz.setId(id.getAndIncrement());
        quizList.add(quiz);
        return quiz;
    }

    public QuizResult evaluateQuiz(int id, String body) {
        matchAnswer.reset(body);
        if (matchAnswer.matches()) {
            int submittedAnswer = Integer.parseInt(matchAnswer.group(1));
            if (submittedAnswer == get(id).getAnswer()) {
                return new QuizResult(true, "Congratulations, you're right!");
            }
            return new QuizResult(false, "Wrong answer! Please, try again.");
        } else {
            return null;
        }
    }
}

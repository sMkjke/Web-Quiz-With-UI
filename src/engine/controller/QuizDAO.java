package engine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class QuizDAO implements DAO<Quiz> {

    private List<Quiz> quizList;
    private AtomicInteger id;

    QuizDAO() {
        this.quizList = new ArrayList<>();
        id = new AtomicInteger(1);
    }


    @Override
    public Quiz get(int id) {
//        try {
//            return quizList.get(id - 1);
//        } catch (IndexOutOfBoundsException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }

        if (quizList.size() > id - 1) {
            return quizList.get(id - 1);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @Override
    public List<Quiz> getAll() {
        return quizList;
    }


    @Override
    public Quiz save(Quiz quiz) {
        quizList.add(quiz);
        quiz.setId(id.getAndIncrement());
        return quiz;
    }

    public QuizResult evaluateQuiz(int id, QuizAnswer answer) {

        int[] originalAnswer = get(id).getAnswer();
        int[] providedAnswer = answer.getAnswer();

        if (originalAnswer.length != providedAnswer.length)
            return new QuizResult(true, "Congratulations, you're right!");
        for (int i = 0; i < originalAnswer.length; i++) {
            if (originalAnswer[i] != providedAnswer[i])
                return new QuizResult(false, "Wrong answer! Please, try again.");
        }
        return new QuizResult(true, "Congratulations, you're right!");
    }
}

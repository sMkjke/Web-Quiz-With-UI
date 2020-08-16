package engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuizController {

    @Autowired
    private IRepository quizRepository;

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuestion(@PathVariable int id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/quizzes")
    public List<Quiz> getAllQuestions() {
        return quizRepository.findAll();
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult checkAnswer(@RequestBody QuizAnswer guess, @PathVariable int id) {
        Quiz question = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (question.isCorrect(guess.getAnswer())) {
            return QuizResult.CORRECT_ANSWER;
        } else {
            return QuizResult.WRONG_ANSWER;
        }
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuestion(@RequestBody @Valid Quiz quiz) {
        return quizRepository.save(quiz);
    }
}
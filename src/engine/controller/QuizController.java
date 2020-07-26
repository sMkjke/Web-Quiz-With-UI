package engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    QuizDAO service;


    @GetMapping()
    public List<Quiz> getQuiz() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        try {
            return service.get(id);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Required quiz not available");
        }
    }

    @PostMapping
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        return service.save(quiz);
    }

    @PostMapping("/{id}/solve")
    public QuizResult submitQuiz(@PathVariable int id, @RequestBody String body) {
        return service.evaluateQuiz(id, body);
    }
}

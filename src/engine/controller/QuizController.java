package engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
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
    public Quiz getQuiz(@PathVariable @Min(1) int id) {
        try {
            return service.get(id);
        } catch (IndexOutOfBoundsException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Required quiz not available");
        }
    }

    @PostMapping
    public Quiz addQuiz(@RequestBody @Valid @NotNull Quiz quiz) {
        return service.save(quiz);
    }

    @PostMapping("/{id}/solve")
    public QuizResult submitQuiz(@PathVariable @Min(1) int id,
                                 @RequestBody @NotNull QuizAnswer answer) {
        return service.evaluateQuiz(id, answer);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}

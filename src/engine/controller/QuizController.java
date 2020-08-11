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
import java.util.NoSuchElementException;

@Validated
@RestController
@RequestMapping(value = "/api/quizzes", produces = "application/json")
public class QuizController {

    @Autowired
    private JPAQuizDAO jpaQuizDAO;


    @GetMapping()
    public List<Quiz> getQuiz() {
        return jpaQuizDAO.getAll();
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable @Min(1) int id) {
//        try {
//            return jpaQuizDAO.get(id);
//        } catch (IndexOutOfBoundsException ignored) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Required quiz not available");
//        }
        return jpaQuizDAO.get(id);
    }

    @PostMapping
    @ResponseBody
    public Quiz addQuiz(@RequestBody @Valid @NotNull Quiz quiz) {
        return jpaQuizDAO.save(quiz);
    }

    @PostMapping("/{id}/solve")
    public QuizResult submitQuiz(@PathVariable @Min(1) int id,
                                 @RequestBody @NotNull QuizAnswer answer) {
        return jpaQuizDAO.evaluateQuiz(id, answer);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleConstraintViolationException() {
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException() {
    }
}

package engine.controller;

import engine.Accomplishment;
import engine.QuizAnswer;
import engine.QuizResult;
import engine.entity.Quiz;
import engine.repository.AccomplishmentRepository;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

// TODO: 10.09.2020 Сделать авторизацию UI 
// TODO: 10.09.2020 Сделать UI квизов 
// TODO: 10.09.2020 Сделать main страничку приветствия 

@RestController
public class QuizController {

//    private static final Logger logger = Logger.getLogger(QuizController.class);
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AccomplishmentRepository accomplishmentRepository;


    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuestion(@PathVariable int id) {
        return quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/quizzes/completed")
    public Page<Accomplishment> getCompleted(@RequestParam(defaultValue = "0") int page, Principal principal) {
        Pageable paging = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        String userEmail = principal.getName();

        return accomplishmentRepository.findByUserEmail(userEmail, paging);
    }


    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getAllQuestions(@RequestParam(defaultValue = "0") int page) {
        Pageable paging = PageRequest.of(page, 10);

        return quizRepository.findAll(paging);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult checkAnswer(@RequestBody QuizAnswer guess, @PathVariable int id, Principal principal) {
        Quiz question = quizRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (question.isCorrect(guess.getAnswer())) {
            Accomplishment accomplishment = new Accomplishment();
            accomplishment.setCompletedAt(LocalDateTime.now());
            accomplishment.setQuestionId(id);
            accomplishment.setUserEmail(principal.getName());

            accomplishmentRepository.save(accomplishment);

            return QuizResult.CORRECT_ANSWER;
        } else {
            return QuizResult.WRONG_ANSWER;
        }
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuestion(@RequestBody @Valid Quiz quiz, Principal principal) {
        quiz.setAuthor(principal.getName());
        return quizRepository.save(quiz);
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int id, Principal principal) {
//        Quiz quiz = quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!quiz.getAuthor().equals(principal.getName())) {
            return new ResponseEntity<>("User is not the author of the quiz", HttpStatus.FORBIDDEN);
        }
        quizRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
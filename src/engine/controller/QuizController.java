package engine.controller;

import engine.Accomplishment;
import engine.entity.Quiz;
import engine.entity.User;
import engine.repository.AccomplishmentRepository;
import engine.repository.QuizRepository;
import engine.service.UserPrincipal;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;

@Controller
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AccomplishmentRepository accomplishmentRepository;
    private ModelAndView modelAndView = new ModelAndView();

    @RequestMapping("/")
    public ModelAndView index() {
        modelAndView.setViewName("home");
        return modelAndView;
    }


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

//    @PostMapping(path = "/api/quizzes/{id}/solve")
//    public QuizResult checkAnswer(@RequestBody QuizAnswer guess, @PathVariable int id, Principal principal) {
//        Quiz question = quizRepository.findById(id).orElseThrow(() ->
//                new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        if (question.isCorrect(guess.getAnswer())) {
//            Accomplishment accomplishment = new Accomplishment();
//            accomplishment.setCompletedAt(LocalDateTime.now());
//            accomplishment.setQuestionId(id);
//            accomplishment.setUserEmail(principal.getName());
//
//            accomplishmentRepository.save(accomplishment);
//
//            return QuizResult.CORRECT_ANSWER;
//        } else {
//            return QuizResult.WRONG_ANSWER;
//        }
//    }

//    @PostMapping(path = "/api/quizzes")
//    public Quiz addQuestion(@RequestBody @Valid Quiz quiz, Principal principal) {
//        quiz.setAuthor(principal.getName());
//        return quizRepository.save(quiz);
//    }

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

    //Quiz add
    @GetMapping("/addquiz")
    public String quizAdd(
            @ModelAttribute Quiz quiz, Model model) {
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", new HashSet<TypePatternQuestions.Question>());
        return "addquiz";
    }

//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/quizsave")
    public String quizSave(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @ModelAttribute Quiz quiz,
            BindingResult bindingResult,
            Model model
    ) {
        quiz.setAuthor(user.getUser());

        if (bindingResult.hasErrors()) {
            return "quizedit";
        }

        quizRepository.save(quiz);
        Iterable<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);

        return "redirect:/quizzes";
    }


    @GetMapping("/quizzes")
    public String index(
            @AuthenticationPrincipal UserPrincipal user,
            Model model
    ) {
        Iterable<Quiz> quizzes;

        quizzes = quizRepository.findAll();

        model.addAttribute("isEnabled", user.isEnabled());;
        model.addAttribute("quizzes", quizzes);
        return "quizzes";
    }

    @GetMapping("/quizedit")
    public String quizEdit(
            @AuthenticationPrincipal User user,
            Model model,
            int id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!user.equals(quiz.getAuthor())) {
            return "redirect:/quizadd";
        }
        model.addAttribute("quiz", quiz);

        return "quizedit";
    }

    @PostMapping("quizzes/delete")
    public String delete(@RequestParam Integer id, Model model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("quizzes", quizzes);

        return "redirect:/quizzes";
    }
}

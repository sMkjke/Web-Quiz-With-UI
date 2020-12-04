package engine.controller;

import engine.entity.Question;
import engine.entity.Quiz;
import engine.entity.User;
import engine.repository.AccomplishmentRepository;
import engine.repository.QuestionRepository;
import engine.repository.QuizRepository;
import engine.service.UserPrincipal;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AccomplishmentRepository accomplishmentRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private ModelAndView modelAndView = new ModelAndView();

    @RequestMapping("/")
    public ModelAndView index() {
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/addquiz")
    public String quizAdd(
            @ModelAttribute Quiz quiz, Model model) {
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", new HashSet<TypePatternQuestions.Question>());
        return "addquiz";
    }

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

        model.addAttribute("isEnabled", user.isEnabled());
        model.addAttribute("quizzes", quizzes);
        return "quizzes";
    }


    @GetMapping("/quizedit")
    public String quizEdit(
            @AuthenticationPrincipal User user,
            Model model,
            Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        if (!user.equals(quiz.getAuthor())) {
//            return "redirect:/quizadd";
//        }
        model.addAttribute("quiz", quiz);

        return "quizedit";
    }

    @PostMapping("quizzes/delete")
    public String delete(@RequestParam Long id, Model model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("quizzes", quizzes);

        return "redirect:/quizzes";
    }

    @GetMapping("/quizdetails/{quiz}")
    public String quizDetails(
            @PathVariable Quiz quiz,
            Model model) {
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        model.addAttribute("question", new Question());
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "quizdetails";
    }

    @PostMapping("/questionadd")
    public String questionAdd(
            @RequestParam Quiz quiz,
            @ModelAttribute Question question,
            Model model
    ) {
        question.setQuiz(quiz);

        questionRepository.save(question);

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "redirect:/quizdetails/" + quiz.getId();
    }

    @GetMapping("/user-quizzes/{user}")
    public String userQuizzes(
            @PathVariable User user,
            Model model
    ) {
        if (user != null) {
            Set<Quiz> quizzes = user.getQuizzes();
            model.addAttribute("quizzes", quizzes);
        }
        model.addAttribute("user", user);

        return "userquizzes";
    }
}


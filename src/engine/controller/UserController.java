package engine.controller;

import engine.entity.User;
import engine.repository.UserRepository;
import org.apache.catalina.authenticator.SavedRequest;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    private ModelAndView modelAndView = new ModelAndView();


    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already registered!")
    static class UsernameIsTakenException extends RuntimeException {
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerUser(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public String addUser(@ModelAttribute User userForm, Model model) {
        if (userRepository.findByEmail(userForm.getEmail()) != null) {
            throw new UsernameIsTakenException();
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (userForm.getPassword() != null) {
            userForm.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        }

        userRepository.save(userForm);
        model.addAttribute("userForm", userForm);
        return "simplemessage"; // have to return success page
    }

    //Json register
    @PostMapping(value = "/api/register", consumes = "application/json")
    public String registerUser(@Valid @RequestBody User newUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new UsernameIsTakenException();
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return String.format("Email %s registration successful", newUser.getEmail());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@ModelAttribute User userForm, Model model) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ModelAndView loginPost(@ModelAttribute User userForm, Model model) {
//        modelAndView.setViewName("home");
//        return modelAndView;
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/login", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String performLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            request.login(username,password);
            return "{\"status\": true}";
        } catch (Exception e) {
            return "{\"status\": false, \"error\": \"Bad Credentials\"}";
        }
    }}


//            @RequestMapping(value = "/login", method = RequestMethod.GET)
//    @PreAuthorize("permitAll")
//    public String login(@ModelAttribute User user) {
//        return "login";
//    }

//    @RequestMapping(value = "/login-error", method = RequestMethod.GET)
//    @PreAuthorize("permitAll")
//    public String loginError(@ModelAttribute User user, Model model) {
//        model.addAttribute("loginError", true);
//        return "login";
//    }4


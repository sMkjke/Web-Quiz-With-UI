package engine.controller;

import engine.entity.User;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    public String performLogin(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            HttpServletRequest request, HttpServletResponse response) {
//        try {
//            request.login(username, password);
//            return "{\"status\": true}";
//        } catch (Exception e) {
//            return "{\"status\": false, \"error\": \"Bad Credentials\"}";
//        }

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
    }


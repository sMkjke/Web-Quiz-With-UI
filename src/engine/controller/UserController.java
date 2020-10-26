package engine.controller;

import engine.entity.User;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already registered!")
    static class UsernameIsTakenException extends RuntimeException {
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.GET)
    public ModelAndView registerUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/api/register", consumes = "application/json")
    public String registerUser(@Valid @RequestBody User newUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new UsernameIsTakenException();
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return String.format("Email %s registration successful", newUser.getEmail());
    }
}
package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.data.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;
    private final UserMapper userMapper;

    public SignUpController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String signupView() {
        return "/signup";
    }

    @PostMapping()
    public ModelAndView signup(@ModelAttribute User user, Model model) {
        int rowsAdded = userService.createUser(user);

        if (rowsAdded < 0 ) {
            ModelAndView signupModelAndView = new ModelAndView("/signup");
            String signupError = "There was an error signing you up. Please try again.";

            signupModelAndView.addObject("signupError", signupError);
            return signupModelAndView;
        } else {
            ModelAndView loginModelAndView = new ModelAndView("/login");
            String signupSuccess = "You have successfully signed up. Please login to continue";

            loginModelAndView.addObject("signupSuccess", signupSuccess);
            return loginModelAndView;
        }
    }
}

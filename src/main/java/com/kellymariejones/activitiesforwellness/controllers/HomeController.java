package com.kellymariejones.activitiesforwellness.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    private static final String userSessionKey = "user";

    @GetMapping
    public String getIndex (Model model, HttpSession session) {
        // set the title of the home page
        model.addAttribute("title", "Welcome");


        Integer userId = (Integer) session.getAttribute(userSessionKey);
        // if the user is logged in,
        // then set a flag to display the log in link
        if (!(userId == null)) {
            model.addAttribute("isLoggedIn", true);
        }
        // otherwise the user is not logged in,
        // so set a flag to display the log out link
        else {
            model.addAttribute("isLoggedIn", false);
        }

        return "index";
    }

}

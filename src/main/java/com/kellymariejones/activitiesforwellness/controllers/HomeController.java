package com.kellymariejones.activitiesforwellness.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String getIndex (Model model) {
        // set the title of the home page
        model.addAttribute("title", "Welcome");
        return "index";
    }

}

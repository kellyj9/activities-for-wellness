package com.kellymariejones.activitiesforwellness.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("dimension")
public class DimensionController {

    @GetMapping
    public String getDimensionList(Model model) {
        // temporarily create a dimension list for the purposes of testing the
        // controller and Thymeleaf template
        ArrayList<String> dimension = new ArrayList<>();
        dimension.add("Testing wellness dimension 1");
        dimension.add("Testing wellness dimension 2");
        dimension.add("Testing wellness dimension 3");
        model.addAttribute("dimension", dimension);
        model.addAttribute("title", "Dimensions");
        return "dimension/index";
    }
}
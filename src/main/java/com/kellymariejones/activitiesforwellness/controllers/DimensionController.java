package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("dimension")
public class DimensionController {

     // Autowired annotation specifies that Spring Boot should auto-populate this field
    // feature of Spring Boot - dependency injection / inversion of control
     @Autowired
    private DimensionRepository dimensionRepository;
    // findAll, save, findById are part of the DimensionRepository interface

    @GetMapping
    public String displayDimensionList(Model model) {
       // add the title of the page to the model
       model.addAttribute("title", "Dimensions of Wellness");
       // add all dimensions in the table to the model
       model.addAttribute("dimension", dimensionRepository.findAll());

        // temporarily create a dimension list for the purposes of testing the
        // controller and Thymeleaf template
//        ArrayList<String> dimension = new ArrayList<>();
//        dimension.add("Testing wellness dimension 1");
//        dimension.add("Testing wellness dimension 2");
//        dimension.add("Testing wellness dimension 3");
//        model.addAttribute("dimension", dimension);
//        model.addAttribute("title", "Dimensions");
        return "dimension/index";
    }
}
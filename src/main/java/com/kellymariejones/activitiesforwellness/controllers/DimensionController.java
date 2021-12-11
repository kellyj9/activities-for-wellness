package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dimension")
public class DimensionController {

    @Autowired
    private DimensionRepository dimensionRepository;

    @GetMapping
    public String displayDimensionList(Model model) {

        // get the list of dimensions
        Iterable<Dimension> result = dimensionRepository.findAll();

        // add the title of the page to the model
        model.addAttribute("title", "Dimensions of Wellness");
        // set the flag to display the logout link on the nav
        model.addAttribute("isSessionPresent", true);

        // add all dimensions in the dimensionRepository to the model
        model.addAttribute("dimension", result);

        return "dimension/index";
    }

}
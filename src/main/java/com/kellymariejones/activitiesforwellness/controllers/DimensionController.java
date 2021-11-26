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

     // Autowired annotation specifies that Spring Boot should auto-populate this field
    // feature of Spring Boot - dependency injection / inversion of control
     @Autowired
    private DimensionRepository dimensionRepository;
    // findAll, save, findById are part of the DimensionRepository interface

//    @Autowired
//    private ActivityRepository activityRepository;

    @GetMapping
    public String displayDimensionList(Model model) {

//         temporarily create a dimension list for the purposes of testing the
//         controller and Thymeleaf template
//        Dimension dimension = new Dimension ("Emotional",
//        "SAMHSA identifies emotional wellness as an ability to cope effectively" +
//        "with life and build satisfying relationships with others.");
//        dimensionRepository.save(dimension);
//        Dimension dimension2 = new Dimension ("Spiritual",
//        "Spiritual wellness is related to your values and beliefs that help you find" +
//        "meaning and purpose in your life.");
//        dimensionRepository.save(dimension2);

       // add the title of the page to the model
       model.addAttribute("title", "Dimensions of Wellness");
       // add all dimensions in the dimensionRepository to the model
       model.addAttribute("dimension", dimensionRepository.findAll());


        return "dimension/index";
    }
}
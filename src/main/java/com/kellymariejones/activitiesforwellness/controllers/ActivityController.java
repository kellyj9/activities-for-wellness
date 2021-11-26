package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.models.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    // specifies that Spring Boot should auto-populate this field
    // feature of Spring Boot - dependency injection / inversion of control
    private ActivityRepository activityRepository;

    // Autowired annotation specifies that Spring Boot should auto-populate this field
    // feature of Spring Boot - dependency injection / inversion of control
    @Autowired
    private DimensionRepository dimensionRepository;
    // findAll, save, findById are part of the DimensionRepository interface

    @GetMapping("index")
    public String displayActivities(@RequestParam(required=true)
                                    Integer dimensionId, Model model) {
//         temporarily create an activity list for the purposes of testing the
//         controller and Thymeleaf template

//        Activity activity1 = new Activity ("Activity1 for dimension",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity1);
//        Activity activity2 = new Activity ("Activity2 for dimension",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity2);


        // if the query param was null...
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            //model.addAttribute("events", activityRepository.findAll());
        } else {
            // gets results of querying for activities by dimensionId
            List<Activity> result = activityRepository.findAllByDimensionId(dimensionId);
            // if no activities found for that dimensionId...
            if (result.isEmpty()) {
                model.addAttribute("title", "No Activities Found");
            }
            // else print the activities for the dimension
            else {
                  // get the result of the query
                model.addAttribute("title",
                        "Activities in dimension: " +
                                dimensionId);
                model.addAttribute("activity", result);
            }
        }
        return "activity/index";
    }

}



        //         temporarily create an activity list for the purposes of testing the
//         controller and Thymeleaf template
//        Activity activity = new Activity ("Whatever1");
//        dimensionRepository.save(activity);
//        Dimension dimension2 = new Dimension ("Physical");
//        dimensionRepository.save(dimension2);

//
//        model.addAttribute("title", "Activities");
//        model.addAttribute("activity", activityRepository.findAll());

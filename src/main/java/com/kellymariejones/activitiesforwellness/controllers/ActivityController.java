package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.models.Activity;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

//        Activity activity1 = new Activity ("Listen to music.",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity1);
//        Activity activity2 = new Activity ("Enjoy a healthy meal.",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity2);

        // if the query param was null...
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
        }
        else {

            // get the name of the dimension
            String dimensionName =
                    dimensionRepository.findById(dimensionId).get().getName();

            model.addAttribute("title",
                    "Add an Activity to the " +
                    dimensionName +
                    " Domain");
            // note that we should do extra validation for the above method call to check
            // that the dimension id is found

            // gets results of querying for activities by dimensionId
            List<Activity> result = activityRepository.findAllByDimensionId(dimensionId);
            if (result.isEmpty()) {
                //model.addAttribute("title", "No Activities Found");
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);
            }
            else {

                // get the name of the dimension
                //model.addAttribute("title",
                        //dimensionRepository.findById(dimensionId).get().getName());
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);
            }
        }
        return "activity/index";  // or redirect:
    }

    @GetMapping("create")
    public String renderCreateActivityForm(
                                            Model model,
                                           @RequestParam(required=true)
                                            Integer dimensionId) {
        // if the dimensionId query parameter was null...
        if (dimensionId==null) {
            // add the title of the page to the model
            model.addAttribute("title", "Dimensions of Wellness");
            // add all dimensions in the dimensionRepository to the model
            model.addAttribute("dimension", dimensionRepository.findAll());
            return("dimension/index");
        }
        else {
            model.addAttribute("title", "Add an Activity to dimension : " +
                    dimensionRepository.findById(dimensionId).get().getName());
            model.addAttribute("activity", new Activity());

            model.addAttribute("dimension",
                    dimensionRepository.findById(dimensionId));

            model.addAttribute("dimensionId", dimensionId);
        }
        return "activity/create";
    }

    @PostMapping("create")
    public String processCreateActivityForm(
                                            @ModelAttribute
                                            @Valid Activity newActivity,
                                            Errors errors, Model model,
                                            @RequestParam(required=true) Integer dimensionId) {
        // Note: Spring Boot will put fields in activity into an Activity object
        // in the Activity class when model binding occurs
        // if the dimensionId query parameter was missing...
        if (dimensionId == null) {
            // add the title of the page to the model
            model.addAttribute("title", "Dimensions of Wellness");
            // add all dimensions in the dimensionRepository to the model
            model.addAttribute("dimension", dimensionRepository.findAll());
            return("dimension/index");
        }
        else {
            // if there are any errors in the Model object...go back to the form
            if (errors.hasErrors()) {
               // model.addAttribute("title", "Add an Activity");
                model.addAttribute("title", "Add an Activity to dimension : " +
                        dimensionRepository.findById(dimensionId).get().getName());
                model.addAttribute("activity", newActivity);
                model.addAttribute("dimensionId", dimensionId);
                return "activity/create";
            }
            else {
                // get the name of the dimension
                model.addAttribute("title",
                        dimensionRepository.findById(dimensionId).get().getName());
                // note that we should do extra validation for the above method call to check
                // that the dimension id is found

                // gets results of querying for activities by dimensionId
                List<Activity> result = activityRepository.findAllByDimensionId(dimensionId);
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);

                newActivity.setDimension(dimensionRepository.findById(dimensionId).get());
                activityRepository.save(newActivity);
            }
        }
        return "redirect:index?dimensionId=" + dimensionId;
    }


    @GetMapping("delete")
    public String displayDeleteEventForm(
                                        @RequestParam(required=true) Integer activityId,
                                        @RequestParam(required=true) Integer dimensionId,
                                         Model model) {
        if (activityId != null) {
            activityRepository.deleteById(activityId);
        }
        return "redirect:index?dimensionId=" + dimensionId;
    }

}

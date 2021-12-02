package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.data.UserRepository;
import com.kellymariejones.activitiesforwellness.models.Activity;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import com.kellymariejones.activitiesforwellness.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DimensionRepository dimensionRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }


    @GetMapping("index")
    public String displayActivities(@RequestParam Integer dimensionId,
                                    Model model,
                                    HttpServletRequest request) {
//         temporarily create an activity list for the purposes of testing the
//         controller and Thymeleaf template

//        Activity activity1 = new Activity ("Listen to music.",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity1);
//        Activity activity2 = new Activity ("Enjoy a healthy meal.",
//                dimensionRepository.findById(dimensionId).get());
//        activityRepository.save(activity2);

        // if the query param was missing, display the error page
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        else {
            // get the name of the dimension
            String dimensionName =
                    dimensionRepository.findById(dimensionId).get().getName();

            model.addAttribute("title",
                    dimensionName + " Dimension - Add an Activity");
            // note that we should do extra validation for the above method call to check
            // that the dimension id is found

            // gets results of querying for activities by dimensionId
            //List<Activity> result = activityRepository.findAllByDimensionId(dimensionId);

            // get the activities by dimension_id and user_id
            User user = getUserFromSession(request.getSession());
            Integer userId = user.getId();
            List<Activity> result =
                    activityRepository.findByDimensionIdAndUserId(dimensionId, userId);
                    
            if (result.isEmpty()) {
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);
            }
            else {
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);
            }
        }
        return "activity/index";  // or redirect:
    }

    @GetMapping("create")
    public String renderCreateActivityForm(
                                            Model model,
                                           @RequestParam Integer dimensionId) {
        // if the dimensionId query parameter was null...
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
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
                                            @RequestParam(required=true) Integer dimensionId,
                                            HttpServletRequest request) {
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

                // get the user id of the current user
                User user = getUserFromSession(request.getSession());
                Integer userId = user.getId();
                // gets results of querying for activities by dimensionId and user id
                List<Activity> result = activityRepository.findByDimensionIdAndUserId(dimensionId, userId);
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);

                newActivity.setDimension(dimensionRepository.findById(dimensionId).get());

                newActivity.setUser(user);
                activityRepository.save(newActivity);
            }
        }
        return "redirect:index?dimensionId=" + dimensionId;
    }


    @GetMapping("delete")
    public String processDeleteActivity(
                                        @RequestParam Integer activityId,
                                        @RequestParam Integer dimensionId,
                                         Model model) {
        if (dimensionId == null || activityId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        else {

            // Note: need to add validation here that the user is deleting
            // their own activity

            activityRepository.deleteById(activityId);
        }
        return "redirect:index?dimensionId=" + dimensionId;
    }

}

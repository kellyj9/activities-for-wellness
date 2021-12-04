package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.data.SampleRepository;
import com.kellymariejones.activitiesforwellness.data.UserRepository;
import com.kellymariejones.activitiesforwellness.models.Activity;
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

    @Autowired
    private SampleRepository sampleRepository;

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
                    dimensionName + " Dimension - My Activities");

            // get the activities by dimension_id and user_id
            User user = getUserFromSession(request.getSession());
            Integer userId = user.getId();
            List<Activity> result =
                    activityRepository.findByDimensionIdAndUserId(dimensionId, userId);
            model.addAttribute("activity", result);
            model.addAttribute("dimensionId", dimensionId);

            // Now set the title of the page...

            // ...when no activities were found
            if (result.isEmpty()) {
                model.addAttribute("activity_list_heading",
                        "No activities found.");
            }
            // ... when activities were found
            else {
                model.addAttribute("activity_list_heading",
                        "My List of Activities.");
            }
        }
        return "activity/index";
    }

    @GetMapping("create")
    public String renderCreateActivityForm(
                                            Model model,
                                           @RequestParam Integer dimensionId) {
        // if the dimensionId query parameter was missing, redirect user to error page
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        // otherwise, display the form for user to add their activity for that dimension
        // along with displaying the sample activities
        else {
            model.addAttribute("title",
                    "Add an Activity to dimension : " +
                    dimensionRepository.findById(dimensionId).get().getName());

            model.addAttribute("activity", new Activity());
            model.addAttribute("dimension",
                    dimensionRepository.findById(dimensionId));
            model.addAttribute("dimensionId", dimensionId);

            // add list of sample activities
            model.addAttribute(
            "sample", sampleRepository.findByDimensionId(dimensionId));

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
        // Spring Boot will put fields in newActivity into an Activity object
        // from the Activity class when model binding occurs

        // if the dimensionId query parameter was missing...
        if (dimensionId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        else {
            // if there are any errors...go back to the form
            if (errors.hasErrors()) {
                model.addAttribute("title",
                        "Add an Activity to dimension : " +
                        dimensionRepository.findById(dimensionId).get().getName());
                model.addAttribute("activity", newActivity);
                model.addAttribute("dimensionId", dimensionId);

                // add list of sample activities
                model.addAttribute(
                        "sample", sampleRepository.findByDimensionId(dimensionId));

                return "activity/create";
            }
            else {
                // get the name of the dimension
                model.addAttribute("title",
                        dimensionRepository.findById(dimensionId).get().getName());

                // get the user from the session
                User user = getUserFromSession(request.getSession());
                // get the user's id
                Integer userId = user.getId();
                // gets results of querying for activities by dimensionId and user id
                List<Activity> result =
                        activityRepository.findByDimensionIdAndUserId(dimensionId, userId);
                model.addAttribute("activity", result);
                model.addAttribute("dimensionId", dimensionId);

                // set the dimension of the new activity
                newActivity.setDimension(dimensionRepository.findById(dimensionId).get());

                //set the user for the new activity
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
            Model model,
            HttpServletRequest request) {
        // check that both query params are not null
        if (dimensionId != null && activityId != null) {

            // verify that the user is deleting their own activity
            Optional<Activity> optionalActivity =
                    activityRepository.findById(activityId);
            // make sure the activity exists for the activityId
            if (optionalActivity.isPresent()) {
                Activity activity = (Activity) optionalActivity.get();

                // next check that the user logged is deleting an activity that is in their list
                if (activity.getUser() == getUserFromSession(request.getSession())) {
                    // delete the user's activity
                    activityRepository.deleteById(activityId);
                    // redirect user to their activity list
                    return "redirect:index?dimensionId=" + dimensionId;
                }

            }
        }
        // if we get here, the validation didn't pass, so redirect user to the error page
        model.addAttribute("title",
                "An error occurred.");
        return "redirect:/error";
    }


    @GetMapping("edit")
    public String renderEditActivityForm(
            Model model,
            @RequestParam Integer dimensionId,
            @RequestParam Integer activityId) {
        // check if a query params was missing
        if (dimensionId == null && activityId != null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        else {
            // verify that the user is editing their own activity
            Optional<Activity> optionalActivity =
                    activityRepository.findById(activityId);
            // make sure the activity exists for the activityId
            if (optionalActivity.isPresent()) {

               // allow the form to be rendered
                Activity activity = (Activity) optionalActivity.get();
                model.addAttribute("activity", activity);
                model.addAttribute("activityId", activityId);
                model.addAttribute("dimensionId", dimensionId);
                model.addAttribute(
                        "title",
                        "Edit my Activity for dimension : " +
                                dimensionRepository.findById(dimensionId).get().getName());
                return "activity/edit";
            }
        }
        // if we get here, the validation didn't pass, so redirect user to the error page
        model.addAttribute("title",
                "An error occurred.");
        return "redirect:/error";
    }


    @PostMapping("edit")
    public String processEditActivityForm(
            @ModelAttribute
            @Valid Activity activity,
            Errors errors, Model model,
            @RequestParam Integer activityId,
            @RequestParam Integer dimensionId,
            HttpServletRequest request) {

        // if a query parameter was missing...
        if (dimensionId == null || activityId == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }
        else {
            // if there are any errors...go back to the form
            if (errors.hasErrors()) {
                model.addAttribute("title",
                        "Edit my Activity for dimension : " +
                                dimensionRepository.findById(dimensionId).get().getName());
                model.addAttribute("activity", activity);
                model.addAttribute("activityId", activityId);
                model.addAttribute("dimensionId", dimensionId);
               // model.addAttribute("dimension",
                 //       dimensionRepository.findById(dimensionId));
                return "activity/edit";
            }
            else {
                // get the name of the dimension
               // model.addAttribute("title",
              //          dimensionRepository.findById(dimensionId).get().getName());

                // make sure the activity exists for the activityId
                Optional<Activity> optionalActivity =
                        activityRepository.findById(activityId);

                 if (optionalActivity.isPresent()) {
                     Activity activityTmp = (Activity) optionalActivity.get();
                    // next check that the user logged is editing an activity that is in their list
                    if (activityTmp.getUser() == getUserFromSession(request.getSession())) {

                        // process the edit
                        activityTmp.setDescription(activity.getDescription());
                        activityRepository.save(activityTmp);

                        // redirect user to their activity list
                        return "redirect:index?dimensionId=" + dimensionId;
                    }
                }
            }
        }
        // if we get here, the validation didn't pass, so redirect user to the error page
        model.addAttribute("title",
                "An error occurred.");
        return "redirect:/error";
    }


}

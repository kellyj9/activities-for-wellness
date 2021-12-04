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

    // Looks for data with the key user in the user’s session.
    // If it finds one, it attempts to retrieve the corresponding User object
    // from the database. If no user ID is in the session,
    // or if there is no user with the given ID, null is returned.
    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        // if no user ID is in the session, return null
        if (userId == null) {
            return null;
        }

        // find the user in the repository
        Optional<User> user = userRepository.findById(userId);

        // if no user with that userId, return null
        if (user.isEmpty()) {
            return null;
        }

        // return the user object
        return user.get();
    }

    // Display the user's list of activities for the selected dimension
    @GetMapping("index")
    public String displayActivities(
            @RequestParam(required=true) Integer dimensionId,
            Model model,
            HttpServletRequest request) {

        // get the name of the selected dimension
        String dimensionName =
                dimensionRepository.findById(dimensionId).get().getName();

        // add the page title
        model.addAttribute("title",
                dimensionName + " Dimension - My Activities");

        // retrieve the logged-in user's activities list

        User user = getUserFromSession(request.getSession());
        // if user not found, redirect to error page
        if (user == null) {
            model.addAttribute("title",
                    "An error occurred.");
            return "redirect:/error";
        }

        // get the user's activities by user_id
        // and dimenion_id for the selected dimension
        Integer userId = user.getId();
        List<Activity> result =
                activityRepository.findByDimensionIdAndUserId(
                        dimensionId, userId);
        model.addAttribute("activity", result);
        model.addAttribute("dimensionId", dimensionId);

        // set the title of the page

        // when activities were not found...
        if (result.isEmpty()) {
            model.addAttribute("activity_list_heading",
                    "No activities found.");
        }
        // when activities were found...
        else {
            model.addAttribute("activity_list_heading",
                    "My List of Activities.");
        }

        return "activity/index";
    }

    //  Render the form for the user to add their activity for the selected
    // dimension, along with a list of sample activities
    @GetMapping("create")
    public String renderCreateActivityForm(
            Model model,
            @RequestParam(required=true) Integer dimensionId) {

        // set the title of the page according to the name of the dimension selected
        model.addAttribute("title",
                "Add an Activity to dimension : " +
                dimensionRepository.findById(dimensionId).get().getName());

        // add a new activity object to the model along with the dimensionId
        model.addAttribute("activity", new Activity());
        model.addAttribute("dimension",
                dimensionRepository.findById(dimensionId));
        model.addAttribute("dimensionId", dimensionId);

        // add the list of sample activities for the selected dimension to the model
        model.addAttribute(
        "sample",
                sampleRepository.findByDimensionId(dimensionId));

        return "activity/create";
    }

    // Process the form for the user to add their activity for the selected
    // dimension
    @PostMapping("create")
    public String processCreateActivityForm(
            @ModelAttribute
            @Valid Activity newActivity,
            Errors errors, Model model,
            @RequestParam(required=true) Integer dimensionId,
            HttpServletRequest request) {
        // Spring Boot will put fields in newActivity into an Activity object
        // from the Activity class when model binding occurs

        // if there are any errors...go back to the form
        if (errors.hasErrors()) {

            model.addAttribute("title",
                    "Add an Activity to dimension : " +
                    dimensionRepository.findById(dimensionId).get().getName());
            model.addAttribute("activity", newActivity);
            model.addAttribute("dimensionId", dimensionId);

            // add list of sample activities for the selected dimension to the model
            model.addAttribute(
                    "sample",
                    sampleRepository.findByDimensionId(dimensionId));

            return "activity/create";
        }
        else {
            // get the name of the dimension
            model.addAttribute("title",
                    dimensionRepository.findById(dimensionId).get().getName());

            // get the user from the session
            User user = getUserFromSession(request.getSession());

            // if user not found, redirect to error page
            if (user == null) {
                model.addAttribute("title",
                        "An error occurred.");
                return "redirect:/error";
            }

            // get the user's userId
            Integer userId = user.getId();
            // gets results of querying for activities by dimensionId and user id
            List<Activity> result =
                    activityRepository.findByDimensionIdAndUserId(
                            dimensionId, userId);
            model.addAttribute("activity", result);
            model.addAttribute("dimensionId", dimensionId);

            // set the dimension of the new activity
            newActivity.setDimension(
                    dimensionRepository.findById(dimensionId).get());

            // set the user for the new activity
            newActivity.setUser(user);

            // set the desciption that the user entered for the new activity
            newActivity.setDescription(newActivity.getDescription().trim());

            // save the new activity to the database
            activityRepository.save(newActivity);
        }

        // go back to the activity list
        return "redirect:index?dimensionId=" + dimensionId;
    }

    // Delete the selected activity from the user's activity list
    @GetMapping("delete")
    public String processDeleteActivity(
            @RequestParam(required=true) Integer activityId,
            @RequestParam(required=true) Integer dimensionId,
            Model model,
            HttpServletRequest request) {

        // verify that the user is attempting to delete their own activity
        // before deleting it

        // find the activity by activityId
        Optional<Activity> optionalActivity =
                activityRepository.findById(activityId);

        // check that the activity exists for the activityId
        if (optionalActivity.isPresent()) {

            Activity activity = (Activity) optionalActivity.get();
            // check that the user logged is deleting an activity that is in their own list
            if (activity.getUser() == getUserFromSession(request.getSession())) {

                // delete the selected activity from the database
                activityRepository.deleteById(activityId);

                // redirect user to their activity list
                return "redirect:index?dimensionId=" + dimensionId;
            }
        }

        // if we get here, validation didn't pass, so redirect user to the error page
        model.addAttribute("title",
                "An error occurred.");
        return "redirect:/error";
    }


    @GetMapping("edit")
    public String renderEditActivityForm(
            Model model,
            @RequestParam(required=true) Integer dimensionId,
            @RequestParam(required=true) Integer activityId) {

        // make sure the activity exists for the activityId
        Optional<Activity> optionalActivity =
                activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {

           // allow the form to be rendered

            // get the activity from the database
            Activity activity = (Activity) optionalActivity.get();

            // add the activity, activityId, dimensionId, and  page title to the model
            model.addAttribute("activity", activity);
            model.addAttribute("activityId", activityId);
            model.addAttribute("dimensionId", dimensionId);
            model.addAttribute(
                    "title",
                    "Edit my Activity for dimension : " +
                            dimensionRepository.findById(dimensionId).get().getName());
            // render the form
            return "activity/edit";
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
            @RequestParam(required=true) Integer activityId,
            @RequestParam(required=true) Integer dimensionId,
            HttpServletRequest request) {

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
                    activityTmp.setDescription(activity.getDescription().trim());
                    activityRepository.save(activityTmp);

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


}

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

    // Looks for data with the key user in the session.
    // If it finds one, it attempts to retrieve the corresponding User object
    // from the database. If no user ID is in the session,
    // or if there is no user with the given ID, null is returned.
    public User getUserFromSession(HttpSession session) {
        // if no session present, return null
        if (session == null) {
            return null;
        }

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

        // return the user
        return user.get();
    }

    // Displays the user's list of activities for the selected dimension
    @GetMapping("index")
    public String displayActivities(
            @RequestParam Integer dimensionId,
            Model model,
            HttpServletRequest request) {

        if (dimensionId == null) {
            return "redirect:error";
        }

        // get the name of the selected dimension
        String dimensionName =
                dimensionRepository.findById(dimensionId).get().getName();

        // add the page title
        model.addAttribute("title",
                "My Activity List for dimension : " + dimensionName );

        // retrieve the logged-in user's activities list

        User user = getUserFromSession(request.getSession(false));

        // if user not found, return error page
        if (user == null) {
            return "redirect:error";
        }

        // get the user's activities by user id
        // and by dimenion_id for the selected dimension
        Integer userId = user.getId();
        List<Activity> result =
                activityRepository.findByDimensionIdAndUserId(
                        dimensionId, userId);
        model.addAttribute("activity", result);
        model.addAttribute("dimensionId", dimensionId);

        // set the title of the page

        String activity_list_heading;

        // if activities were not found
        if (result.isEmpty()) {
            activity_list_heading =
                    "No activities found yet for the " + dimensionName +
                            " dimension... Add an activity!";
        }
        // activities were found
        else {
            activity_list_heading =
                    "My List of Activities for the " + dimensionName + " dimension. ";

        }
        model.addAttribute("activity_list_heading",
                activity_list_heading);

        // set the flag to display the logout link on the nav
        model.addAttribute("isSessionPresent", true);

        return "activity/index";
    }

    //  Display the form for the user to add their activity for the selected
    // dimension, along with displaying a list of sample activities
    @GetMapping("create")
    public String renderCreateActivityForm(
            Model model,
            @RequestParam Integer dimensionId) {

        if (dimensionId == null) {
            return "redirect:error";
        }

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

        // set the flag to display the logout link on the nav
        model.addAttribute("isSessionPresent", true);

        return "activity/create";
    }

    // Process the form for the user to add their activity for the selected dimension
    @PostMapping("create")
    public String processCreateActivityForm(
            @ModelAttribute
            @Valid Activity newActivity,
            Errors errors, Model model,
            @RequestParam Integer dimensionId,
            HttpServletRequest request) {
        // Spring Boot will put fields in newActivity into an Activity object
        // from the Activity class when model binding occurs

        if (dimensionId == null) {
            return "redirect:error";
        }

        // if there are any errors...go back to the form
        if (errors.hasErrors()) {

            model.addAttribute("title",
                    "Add an Activity for dimension : " +
                            dimensionRepository.findById(dimensionId).get().getName());
            model.addAttribute("activity", newActivity);
            model.addAttribute("dimensionId", dimensionId);

            // add list of sample activities for the selected dimension to the model
            model.addAttribute(
                    "sample",
                    sampleRepository.findByDimensionId(dimensionId));

            model.addAttribute("isSessionPresent", true);
            return "activity/create";
        }
        else {
            // get the name of the dimension
            model.addAttribute("title",
                    dimensionRepository.findById(dimensionId).get().getName());

            // get the user from the session
            User user = getUserFromSession(request.getSession(false));

            // if user not found, return error page
            if (user == null) {
                return "redirect:error";
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

    // Delete the selected activity from the user's activity list.
    // Verifies that the user is attempting to delete their own existing activity
    @GetMapping("delete")
    public String processDeleteActivity(
            @RequestParam Integer activityId,
            @RequestParam Integer dimensionId,
            Model model,
            HttpServletRequest request) {

        if (dimensionId == null || activityId == null) {
            return "redirect:error";
        }

        // verify that the user is attempting to delete their own activity
        // before deleting it

        // find the activity by activityId
        Optional<Activity> optionalActivity =
                activityRepository.findById(activityId);

        // check that the activity exists for the activityId
        if (optionalActivity.isPresent()) {

            Activity activity = (Activity) optionalActivity.get();
            // check that the user is deleting an activity that is in their own list
            if (activity.getUser() ==
                    getUserFromSession(request.getSession(false))) {

                // delete the selected activity from the database
                activityRepository.deleteById(activityId);

                // redirect user to their activity list
                return "redirect:index?dimensionId=" + dimensionId;
            }
        }
        // if we get here, validation didn't pass, return error page
        return "redirect:error";
    }

    //  Render the form for the user to edit their activity for the selected
    // dimension
    @GetMapping("edit")
    public String renderEditActivityForm(
            Model model,
            @RequestParam Integer dimensionId,
            @RequestParam Integer activityId,
            HttpServletRequest request) {

        if (dimensionId == null || activityId == null) {
            return "redirect:error";
        }

        // make sure the activity exists for the activityId
        Optional<Activity> optionalActivity =
                activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {

            // allow the form to be rendered

            // get the activity from the database
            Activity activity = (Activity) optionalActivity.get();

            if (activity.getUser() ==
                    getUserFromSession(request.getSession(false))) {
                // add the activity, activityId, dimensionId, and title to the model
                model.addAttribute("activity", activity);
                model.addAttribute("activityId", activityId);
                model.addAttribute("dimensionId", dimensionId);
                model.addAttribute(
                        "title",
                        "Edit my Activity for dimension : " +
                        dimensionRepository.findById(dimensionId).get().getName());

                // set the flag to display the logout link on the nav
                model.addAttribute("isSessionPresent", true);
                return "activity/edit";
            }
        }
        // if we get here, the validation didn't pass, return error page
        return "redirect:error";
    }

    // Process the form for the user to edit their activity for the selected
    // dimension.
    // Verifies that the user is attempting to edit their own existing activity.
    @PostMapping("edit")
    public String processEditActivityForm(
            @ModelAttribute
            @Valid Activity activity,
            Errors errors, Model model,
            @RequestParam Integer activityId,
            @RequestParam Integer dimensionId,
            HttpServletRequest request) {

        if (dimensionId == null || activityId == null) {
            return "redirect:error";
        }

        // if there are any errors...go back to the form
        if (errors.hasErrors()) {

            model.addAttribute("title",
                    "Edit my Activity for dimension : " +
                       dimensionRepository.findById(dimensionId).get().getName());
            model.addAttribute("activity", activity);
            model.addAttribute("activityId", activityId);
            model.addAttribute("dimensionId", dimensionId);

            model.addAttribute("isSessionPresent", true);
            return "activity/edit";
        }

        // verify the activity exists for the activityId
        Optional<Activity> optionalActivity =
                activityRepository.findById(activityId);

        // verify the activity exists and the user is trying to edit their own acitivty

        // if the activity exists...
        if (optionalActivity.isPresent()) {
            // get the activity for the database
            Activity activityTmp = (Activity) optionalActivity.get();
            // next check that the user is editing an activity that
            // is in their own list
            if (activityTmp.getUser() ==
                    getUserFromSession(request.getSession(false))) {

                // Set the new description for the activity
                activityTmp.setDescription(activity.getDescription().trim());

                // save the change to the database
                activityRepository.save(activityTmp);

                // redirect user to their activity list
                return "redirect:index?dimensionId=" + dimensionId;
            }
        }
        // if we get here, the validation didn't pass, return error page
        return "redirect:error";
    }

}

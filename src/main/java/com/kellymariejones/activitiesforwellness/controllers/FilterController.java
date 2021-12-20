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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("filter")
public class FilterController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DimensionRepository dimensionRepository;

    @Autowired
    private UserRepository userRepository;

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

    // Displays all of the activities for a user and allows for filtering
    @GetMapping
    public String renderFilterActivitiesForm(Model model,
                                                HttpServletRequest request) {
            model.addAttribute("title",
                "Filter my Activity Lists");

          // retrieve the logged-in user's activities list
                User user = getUserFromSession(request.getSession(false));

        // if user not found, return error page
        if (user == null) {
            return "redirect:/error";
        }

        // get the user's activities by user id
        Integer userId = user.getId();
        List<Activity> activities =
                activityRepository.findByUserId(userId);

        model.addAttribute("allActivities", activities);

        // set the title of the page

        String list_heading ="";

        // if activities were not found
        if (activities.isEmpty()) {
            list_heading =
                    "No activity lists found yet.... Create an Activity List!";
        }
        // activities were found
        else {
            List<Dimension> dimensionsWithActivities = new ArrayList<>();
            List<Dimension> dimensionsWithoutActivities = new ArrayList<>();
            List<Dimension> dimensionsAll = (List<Dimension>) dimensionRepository.findAll();
            int counter = 0;

            // based on the user's activities, find the dimensions with and without activities
            for (int i = 0; i < dimensionsAll.size(); i++) {
                for (int j = 0; j < activities.size(); j++) {
                    if (dimensionsAll.get(i).equals(activities.get(j).getDimension())) {
                        counter++;
                        break;  // break out of inner loop
                    }
                }
                if (counter == 0) { // the current dimension has at least one activity for the user
                    dimensionsWithoutActivities.add(dimensionsAll.get(i));
                } else { // the current dimension doesn't have any activities for the user
                    dimensionsWithActivities.add(dimensionsAll.get(i));
                }
                counter = 0; // reset counter;
            }

            // TO DO: sort the dimensions and activities by id first !


            model.addAttribute("dimensionsWithActivities", dimensionsWithActivities);

            // if there are dimensions without activities...
            if (!dimensionsWithoutActivities.isEmpty()) {
                model.addAttribute("dimensionsWithoutActivities",
                            dimensionsWithoutActivities);
                list_heading = "The following dimensions have no activities created.";
            }

        }

        model.addAttribute("list_heading", list_heading);

        // set the flag to display the logout link on the nav
        model.addAttribute("isSessionPresent", true);

        return "filter/index";
    }

}

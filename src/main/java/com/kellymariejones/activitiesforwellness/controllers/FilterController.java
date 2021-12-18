package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.data.UserRepository;
import com.kellymariejones.activitiesforwellness.models.Activity;
import com.kellymariejones.activitiesforwellness.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        List<Activity> result =
                activityRepository.findByUserId(userId);

        model.addAttribute("allActivities", result);

        // set the title of the page

        String filter_list_heading;

        // if activities were not found
        if (result.isEmpty()) {
            filter_list_heading =
                    "No activity lists were created yet.... Create an Activity List!";
        }
        // activities were found
        else {
            filter_list_heading = "My List of Activities";

            model.addAttribute("dimensions",
                    dimensionRepository.findAll());

        }
        model.addAttribute("filter_list_heading",
                filter_list_heading);

        // set the flag to display the logout link on the nav
        model.addAttribute("isSessionPresent", true);

        return "filter/index";
    }

}

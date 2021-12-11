package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.UserRepository;
import com.kellymariejones.activitiesforwellness.models.DTO.LoginFormDTO;
import com.kellymariejones.activitiesforwellness.models.DTO.RegisterFormDTO;
import com.kellymariejones.activitiesforwellness.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    // Before creating handler methods for rendering and processing
    // our login and registration forms,
    // we need some utility methods for working with sessions.
    // This code allows us to store and retrieve the login status of a
    // user in a session. More specifically, a logged-in user’s user ID
    // will be stored in their session.

    private static final String userSessionKey = "user";

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

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    //  Before we can authenticate a user, we must have users in the application,
    //  so we have a registration form first.
    @GetMapping("/register")
    public String displayRegistrationForm(Model model, HttpServletRequest request) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");

        // set a flag to determine whether to display a login or a logout link
        boolean isSessionPresent = false;   // assume session not present
        // get the current session. (and if session not present, don't create a session)
        HttpSession session = request.getSession(false);
        if (session != null) {
            // session present
            isSessionPresent = true;
        }
        model.addAttribute("isSessionPresent", isSessionPresent);

        return "register";
    }

    // Define the handler method at the route /register that takes a
    // valid RegisterFormDTO object, associated errors, and a Model.
    // In addition, the method needs an HttpServletRequest object. This object
    // represents the incoming request, and will be provided by Spring.
    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute
                                              @Valid RegisterFormDTO registerFormDTO,
                                              Errors errors, HttpServletRequest request,
                                              Model model) {

        // Add a validation error if the username contains a space.
        if (registerFormDTO.getUsername().contains(" "))
          {
            errors.rejectValue("username", "username.invalidusername",
                    "Username must not contain spaces.");
        }
        // Add a validation error if the password contains a space.
        if (registerFormDTO.getPassword().contains(" ")) {
            errors.rejectValue("password", "username.invalidpassword",
                    "Password must not contain spaces.");
        }

        // Retrieve the user with the given username from the database.
        User existingUser =
                userRepository.findByUsername(registerFormDTO.getUsername());

        //  If a user with the given username already exists, register a custom error
        //  with the errors object and return the user to the form.
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists",
                    "A user with that username already exists");
//            model.addAttribute("title", "Register");
//            return "register";
        }

        // Compare the two passwords submitted. If they do not match,
        // register a custom error and return the user to the form.
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {

            // errors.rejectValue takes three parameters:
            //The field containing the error.
            //A label representing the error. This allows error messages to be imported
            // from another file. While we don’t have such a file, this parameter is required.
            //A default message to use if no external error message file is available
            // (as is the case here).
            errors.rejectValue("password", "passwords.mismatch",
                    "Passwords do not match");
//            model.addAttribute("title", "Register");
//            return "register";
        }


        // Return the user to the form if an validation errors occur.
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");

            boolean isSessionPresent = false;   // assume session not present
            // get the current session. (and if session not present, don't create a session)
            HttpSession session = request.getSession(false);
            if (session != null) {
                // session present
                isSessionPresent = true;
            }
            model.addAttribute("isSessionPresent", isSessionPresent);

            return "register";
        }

        // At this point, we know that a user with the given username does
        // NOT already exist, and the rest of the form data is valid.
        // So we create a new user object, store it in the database, and then create a
        // new session for the user.
        User newUser = new User(registerFormDTO.getUsername(),
                registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        // user is registered and also logged in.
        // redirect the user to the dimension page

        model.addAttribute("isSessionPresent", true);

        return "redirect:dimension";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model, HttpServletRequest request) {

        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");

        // set a flag to determine whether to display a login or a logout link
        boolean isSessionPresent = false;   // assume session not present
        // get the current session. (and if session not present, don't create a session)
        HttpSession session = request.getSession(false);
        if (session != null) {
            // session present
            isSessionPresent = true;
        }
        model.addAttribute("isSessionPresent", isSessionPresent);

        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute
                                       @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            model.addAttribute("isSessionPresent", false);
            return "login";
        }

        // Retrieve the User object with the given password from the database.
        User theUser =
                userRepository.findByUsername(loginFormDTO.getUsername());

        // If no such user exists, register a custom error and return to the form.
        if (theUser == null) {
            errors.rejectValue("username", "user.invalid",
                    "The given username does not exist");
            model.addAttribute("title", "Log In");
            model.addAttribute("isSessionPresent", false);
            return "login";
        }

        // Retrieves the submitted password from the form DTO.
        String password = loginFormDTO.getPassword();

        // If the password is incorrect, register a custom error and return to the form.
        // Password verification uses the User.isMatchingPassword() method,
        // which handles the details associated with checking hashed passwords.
        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid",
                    "Invalid password");
            model.addAttribute("title", "Log In");
            model.addAttribute("isSessionPresent", false);
            return "login";
        }
        if (errors.hasErrors()) {
             model.addAttribute("title", "Log In");
             model.addAttribute("isSessionPresent", false);
             return "login";
        }
        // At this point, we know the given user exists and that the submitted
        // password is correct.
        // So we create a new session for the user.
        setUserInSession(request.getSession(), theUser);

        // user is logged in.  redirect the user to the dimension index page
        model.addAttribute("isSessionPresent", true);
        return "redirect:dimension";
    }

    // logs out a user
    // Invalidates the session associated with the given user.
    // This removes all data from the session, so that when the user makes a
    // subsequent request, they will be forced to log in again.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:";
    }

    @GetMapping("/error")
    public String displayErrorPage(Model model) {
        model.addAttribute("title", "An Error Occurred");
        model.addAttribute("isSessionPresent", false);
        return "error";
    }

}
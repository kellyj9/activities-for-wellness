package com.kellymariejones.activitiesforwellness;

import com.kellymariejones.activitiesforwellness.controllers.AuthenticationController;
import com.kellymariejones.activitiesforwellness.data.UserRepository;
import com.kellymariejones.activitiesforwellness.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> allowedList = Arrays.asList(
            "/login", "/register", "/logout", "/styles", "/image");

    private static boolean checkAllowedList(String path) {
        // if the home page is requested, user doesn't need to be logged in
        if (path.equals("/")) {
            return true;
        }
        // if the path is in the whitelist, user doesn't need to be logged in
        for (String pathRoot : allowedList) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        // otherwise, the user needs to log in
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // prevent pages in our application from being cached
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        // don't require sign-in for whitelisted pages
        if (checkAllowedList(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        // get the session, but don't create one if not already present
        HttpSession session = request.getSession(false);

        // if there is a session present...
        if (session != null) {

            // if there was an error,
            // invalidate the session
            if (request.getRequestURI().startsWith("/error")) {
                session.invalidate();
                return true;
            }

            // get the user
            User user = authenticationController.getUserFromSession(session);

            // The user is logged in
            if (user != null) {
                // allow the request to proceed
                return true;
            }

        }

        // The user is NOT logged in

        // there was an error, display the error page
        if (request.getRequestURI().equals("/error")) {
            return true;
        }

        // otherwise, the user needs to log in, redirect to the login page
        response.sendRedirect("/login");
        return false;
    }

}
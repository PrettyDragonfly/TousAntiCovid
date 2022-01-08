package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.Activity;
import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.ActivityRepository;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ActivityRepository activityRepo;

    @GetMapping("/app")
    public String appHome(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = getCurrentUserFromUserDetails(userDetails);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/index";
    }

    @GetMapping("/app/users/me")
    public String appCurrentUserProfile(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/current_user_profile";
    }

    @GetMapping("/app/users/me/edit")
    public String appCurrentUserProfileEdit(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/current_user_profile_edit";
    }

    @GetMapping("/app/users/me/friends")
    public String appCurrentUserFriends(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/current_user_friends";
    }

    @GetMapping("/app/users/me/notifications")
    public String appCurrentUserNotification(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));
        model.addAttribute("notifications", currentUser.getNotifications());

        return "app/current_user_notifications";
    }

    @GetMapping("/app/user/{id}")
    public ModelAndView appUserProfile(Authentication authentication, Model model, @PathVariable Long id) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = getCurrentUserFromUserDetails(currentUserDetails);

        Optional<User> userToShow = userRepo.findById(id);

        if (userToShow.isPresent()) {
            User u = userToShow.get();

            if (u.equals(currentUser)) {
                return new ModelAndView("redirect:/app/profile/me");
            }

            model.addAttribute("userToShow", u);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(currentUserDetails));

        return new ModelAndView("app/user_profile");
    }

    @GetMapping("/app/users/search")
    public String appUsersSearch(Authentication authentication, Model model, @RequestParam(required = true) String search) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = getCurrentUserFromUserDetails(currentUserDetails);

        Collection<User> allMatchingUsers = userRepo.findMatch(search);
        allMatchingUsers.remove(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", allMatchingUsers);
        model.addAttribute("isAdmin", isCurrentUserAdmin(currentUserDetails));

        allMatchingUsers.forEach(u -> System.out.println("User: " + u.getFullName() + " has " + (long) u.getFriendRequests().size() + " friend requests"));

        return "app/users_search";
    }

    @GetMapping("/app/events")
    public String appEvents(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        List<Activity> allEvents = activityRepo.findAll();
        List<Activity> userEvents = activityRepo.getActivitiesForUser(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));
        model.addAttribute("allEvents", allEvents);
        model.addAttribute("userEvents", userEvents);

        return "app/events";
    }

    @GetMapping("/app/events/{id}")
    public String appActivitiesShow(Authentication authentication, Model model, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        Optional<Activity> act = activityRepo.findById(id);
        if (act.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "activity not found"
            );
        }

        model.addAttribute("activity", act.get());

        //TO CHANGE
        return "app/events_show";
    }

//    @GetMapping("/app/users")
//    public String appUsers(Authentication authentication, Model model) {
//        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
//        Collection<User> allUsers = userRepo.findAll();
//
//        model.addAttribute("user", userDetails);
//        model.addAttribute("users", allUsers);
//        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));
//
//        return "app/users";
//    }

    private boolean isCurrentUserAdmin(CovidAppUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }

    private User getCurrentUserFromUserDetails(CovidAppUserDetails userDetails) {
        return userRepo.findCustomId(userDetails.getUserId());
    }
}

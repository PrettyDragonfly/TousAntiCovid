package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
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
import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/app")
    public String appHome(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();

        model.addAttribute("user", userDetails);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/index";
    }

    @GetMapping("/app/profile/me")
    public String appCurrentUserProfile(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();

        model.addAttribute("user", userDetails);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/current_user_profile";
    }

    @GetMapping("/app/user/{id}")
    public ModelAndView appUserProfile(Authentication authentication, Model model, @PathVariable Long id) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Optional<User> userToShow = userRepo.findById(id);

        if (userToShow.isPresent()) {
            User u = userToShow.get();

            if (u.equals(currentUserDetails.getUser())) {
                return new ModelAndView("redirect:/app/profile/me");
            }

            model.addAttribute("userToShow", u);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }

        model.addAttribute("user", currentUserDetails);
        model.addAttribute("isAdmin", isCurrentUserAdmin(currentUserDetails));

        return new ModelAndView("app/user_profile");
    }

    @GetMapping("/app/users/search")
    public String appUsersSearch(Authentication authentication, Model model, @RequestParam(required = true) String searchTerm) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Collection<User> allMatchingUsers = userRepo.findMatch(searchTerm);

        allMatchingUsers.remove(currentUserDetails.getUser());

        model.addAttribute("user", currentUserDetails);
        model.addAttribute("users", allMatchingUsers);
        model.addAttribute("isAdmin", isCurrentUserAdmin(currentUserDetails));

        return "app/users_search";
    }

    @GetMapping("/app/users")
    public String appUsers(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Collection<User> allUsers = userRepo.findAll();

        model.addAttribute("user", userDetails);
        model.addAttribute("users", allUsers);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/users";
    }

    private boolean isCurrentUserAdmin(CovidAppUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }
}

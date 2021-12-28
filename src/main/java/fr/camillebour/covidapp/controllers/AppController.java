package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

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
    public String appUserProfile(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();

        model.addAttribute("user", userDetails);
        model.addAttribute("isAdmin", isCurrentUserAdmin(userDetails));

        return "app/current_user_profile";
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

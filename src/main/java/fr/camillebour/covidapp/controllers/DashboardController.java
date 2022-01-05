package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());
        List<User> allUsers = userRepo.findAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", allUsers);

        return "dashboard/index";
    }

    @GetMapping("/dashboard/users")
    public String dashboardUsers(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());
        List<User> allUsers = userRepo.findAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", allUsers);

        return "dashboard/users";
    }
}

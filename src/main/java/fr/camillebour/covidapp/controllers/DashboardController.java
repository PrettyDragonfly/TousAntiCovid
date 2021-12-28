package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        model.addAttribute("user", userDetails);

        return "dashboard/index";
    }
}

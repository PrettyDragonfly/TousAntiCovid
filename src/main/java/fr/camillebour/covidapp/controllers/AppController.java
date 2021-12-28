package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/app")
    public String appHome(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();

        boolean isAdmin = false;
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            isAdmin = true;
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("isAdmin", isAdmin);

        return "app/index";
    }
}

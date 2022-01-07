package fr.camillebour.covidapp.controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.UserRepository;
import fr.camillebour.covidapp.utils.D3Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/dashboard/stats/users-graph")
    public String dashboardStatsUsersGraph(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());

        List<User> allUsers = userRepo.findAll();
        D3Graph usersGraph = D3Graph.fromUsers(allUsers);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String graphString;
        try {
            graphString = objectMapper.writeValueAsString(usersGraph);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "user not found"
            );
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("usersGraph", graphString);

        return "dashboard/users_graph";
    }
}

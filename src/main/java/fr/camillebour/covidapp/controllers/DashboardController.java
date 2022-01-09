package fr.camillebour.covidapp.controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.camillebour.covidapp.models.*;
import fr.camillebour.covidapp.repositories.ActivityRepository;
import fr.camillebour.covidapp.repositories.LocationRepository;
import fr.camillebour.covidapp.repositories.UserRepository;
import fr.camillebour.covidapp.utils.D3Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ActivityRepository activityRepo;

    @Autowired
    private LocationRepository locationRepo;

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

    @GetMapping("/dashboard/locations")
    public String dashboardLocations(Authentication authentication, Model model) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());
        List<Location> allLocations = locationRepo.findAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("locations", allLocations);

        return "dashboard/locations/locations";
    }

    @GetMapping("/dashboard/locations/{id}")
    public String dashboardLocationsShow(Authentication authentication, Model model, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());
        Optional<Location> location = locationRepo.findById(id);

        if (location.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "location not found"
            );
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("location", location.get());

        return "dashboard/locations/location_show";
    }

    @GetMapping("/dashboard/locations/create")
    public String dashboardLocationsCreate(Model model) {
        model.addAttribute("location", new Location());
        return "dashboard/locations/location_create";
    }

    @GetMapping("/dashboard/locations/{id}/edit")
    public String dashboardLocationsEdit(Authentication authentication, Model model, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findCustomId(userDetails.getUserId());
        Optional<Location> location = locationRepo.findById(id);

        if (location.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "location not found"
            );
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("location", location.get());

        return "dashboard/locations/location_edit";
    }

    @GetMapping("/dashboard/events")
    public String dashboardActivities(Model model) {
        List<Activity> allActivities = activityRepo.findAll();
        model.addAttribute("activities", allActivities);

        return "dashboard/activities/activities";
    }

    @GetMapping("/dashboard/events/create")
    public String dashboardActivitiesCreate(Model model) {
        List<Location> allLocations = locationRepo.findAll();
        model.addAttribute("locations", allLocations);
        return "dashboard/activities/activities_create";
    }

    @GetMapping("/dashboard/events/{id}")
    public String dashboardActivitiesShow(Model model, @PathVariable Long id) {
        Optional<Activity> act = activityRepo.findById(id);
        if (act.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "activity not found"
            );
        }

        model.addAttribute("activity", act.get());

        return "dashboard/activities/activities_show";
    }

    @GetMapping("/dashboard/events/{id}/edit")
    public String dashboardActivitiesEdit(Model model, @PathVariable Long id) {
        Optional<Activity> act = activityRepo.findById(id);
        if (act.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "activity not found"
            );
        }

        List<Location> allLocations = locationRepo.findAll();

        model.addAttribute("activity", act.get());
        model.addAttribute("locations", allLocations);

        return "dashboard/activities/activities_edit";
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

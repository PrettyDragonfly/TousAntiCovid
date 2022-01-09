package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.*;
import fr.camillebour.covidapp.repositories.ActivityRepository;
import fr.camillebour.covidapp.repositories.LocationRepository;
import fr.camillebour.covidapp.repositories.RoleRepository;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardRestController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private LocationRepository locationsRepo;

    @Autowired
    private ActivityRepository activityRepo;

    @GetMapping("/promote/{id}")
    public ResponseEntity promoteAdmin(@PathVariable Long id) {
        Optional<User> userToPromote = userRepo.findById(id);

        if (userToPromote.isPresent()) {
            User u = userToPromote.get();
            if (u.hasRole("ROLE_ADMIN")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "cannot promote a user that is already admin"
                );
            }
            Role adminRole = roleRepo.findByName("ROLE_ADMIN");
            u.addRole(adminRole);
            userRepo.save(u);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locations/create")
    public ModelAndView createLocation(@ModelAttribute Location newLocation, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            System.out.println("Error have occurred");
            result.getAllErrors().forEach(e -> System.out.println("Error: " + e.toString()));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error occurred"
            );
        }

        locationsRepo.save(newLocation);
        return new ModelAndView("redirect:/dashboard/locations/" + newLocation.getId());
    }

    @PostMapping("/locations/{id}/edit")
    public ModelAndView editLocation(@PathVariable Long id, @ModelAttribute Location locationInfo, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            System.out.println("Error have occurred");
            result.getAllErrors().forEach(e -> System.out.println("Error: " + e.toString()));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error occurred"
            );
        }

        Optional<Location> loc = locationsRepo.findById(id);
        if (loc.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "location not found"
            );
        }

        Location location = loc.get();
        location.setDenomination(locationInfo.getDenomination());
        location.setAddress(locationInfo.getAddress());
        location.setGPSCoordinates(locationInfo.getGPSCoordinates());
        locationsRepo.save(location);

        return new ModelAndView("redirect:/dashboard/locations/" + id);
    }

    @PostMapping("/locations/{id}/delete")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ModelAndView dashboardLocationDelete(Authentication authentication, @PathVariable Long id) {
        Optional<Location> location = locationsRepo.findById(id);

        if (location.isPresent()) {
            Location l = location.get();
            System.out.println("Location " + l.getId() + " deleted");

            for (Activity a : activityRepo.getActivitiesForLocation(l)) {
                activityRepo.delete(a);
            }
            locationsRepo.delete(l);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "location not found"
            );
        }
        return new ModelAndView("redirect:/dashboard/locations");
    }

    @PostMapping("/events/create")
    public ModelAndView createActivities(@ModelAttribute Activity newActivity, BindingResult result, ModelMap model,  @RequestBody MultiValueMap<String, String> formData) {
        if (result.hasErrors()) {
            System.out.println("Error have occurred");
            result.getAllErrors().forEach(e -> System.out.println("Error: " + e.toString()));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error occurred"
            );
        }

        String lData = formData.getFirst("location");
        if (lData == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "could not get location"
            );
        }

        Long lId = Long.parseLong(lData);
        Location location = locationsRepo.getById(lId);
        newActivity.setLocation(location);
        activityRepo.save(newActivity);

        return new ModelAndView("redirect:/dashboard/events/" + newActivity.getId());
    }

    @PostMapping("/events/{id}/edit")
    public ModelAndView editActivities(@ModelAttribute Activity activityInfo, @PathVariable Long id,  BindingResult result, ModelMap model, @RequestBody MultiValueMap<String, String> formData) {
        if (result.hasErrors()) {
            System.out.println("Error have occurred");
            result.getAllErrors().forEach(e -> System.out.println("Error: " + e.toString()));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error occurred"
            );
        }

        Optional<Activity> act = activityRepo.findById(id);
        if (act.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "activity not found"
            );
        }

        String lData = formData.getFirst("location");
        if (lData == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "could not get location"
            );
        }

        Long lId = Long.parseLong(lData);
        Location location = locationsRepo.getById(lId);

        Activity a = act.get();
        a.setName(activityInfo.getName());
        a.setStartDate(activityInfo.getStartDate());
        a.setEndDate(activityInfo.getEndDate());
        a.setLocation(location);
        activityRepo.save(a);

        return new ModelAndView("redirect:/dashboard/events/" + a.getId());
    }

    @PostMapping("/events/{id}/delete")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ModelAndView dashboardActivityDelete(Authentication authentication, @PathVariable Long id) {
        Optional<Activity> activity = activityRepo.findById(id);

        if (activity.isPresent()) {
            Activity a = activity.get();
            System.out.println("Activity " + a.getId() + " deleted");

            activityRepo.delete(a);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Activity not found"
            );
        }
        return new ModelAndView("redirect:/dashboard/events");
    }
}

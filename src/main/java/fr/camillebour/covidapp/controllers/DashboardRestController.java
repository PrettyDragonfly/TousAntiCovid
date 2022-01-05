package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.Role;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.RoleRepository;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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
}

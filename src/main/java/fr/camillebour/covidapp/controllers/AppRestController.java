package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/app/api/v1")
public class AppRestController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/request-friend/{id}")
    public ResponseEntity requestFriend(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Optional<User> userToAsk = userRepo.findById(id);

        if (userToAsk.isPresent()) {
            User u = userToAsk.get();
            if (!u.hasRequestFrom(userDetails.getUser()) && !u.getFriends().contains(userDetails.getUser())) {
                u.addFriendRequestFrom(userDetails.getUser());
                userRepo.save(u);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friend-request/{id}/accept")
    public ResponseEntity acceptFriendRequest(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Optional<User> userAsking = userRepo.findById(id);

        if (userAsking.isPresent()) {
            User u = userAsking.get();
            if (!userDetails.getUser().getFriends().contains(u) && userDetails.getFriendRequests().contains(u)) {
                userDetails.addFriend(u);
                userDetails.getUser().removeFriendRequestFrom(u);
                u.addFriend(userDetails.getUser());
                userRepo.save(u);
                userRepo.save(userDetails.getUser());
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friend-request/{id}/reject")
    public ResponseEntity rejectFriendRequest(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails userDetails = (CovidAppUserDetails) authentication.getPrincipal();
        Optional<User> userAsking = userRepo.findById(id);

        if (userAsking.isPresent()) {
            User u = userAsking.get();
            if (!userDetails.getUser().getFriends().contains(u) && userDetails.getFriendRequests().contains(u)) {
                userDetails.getUser().removeFriendRequestFrom(u);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    private boolean isCurrentUserAdmin(CovidAppUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }
}

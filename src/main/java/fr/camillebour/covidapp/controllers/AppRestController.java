package fr.camillebour.covidapp.controllers;

import fr.camillebour.covidapp.models.CovidAppUserDetails;
import fr.camillebour.covidapp.models.ExposureNotification;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.ExposureNotificationRepository;
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
@RequestMapping("/api/v1")
public class AppRestController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExposureNotificationRepository notificationRepo;

    @GetMapping("/request-friend/{id}")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity requestFriend(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();

        User currentUser = userRepo.findCustomId(currentUserDetails.getUserId());
        Optional<User> userToAsk = userRepo.findById(id);

        if (userToAsk.isPresent()) {
            User u = userToAsk.get();

            if (u.equals(currentUser)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "you better be friend with yourself already..."
                );
            }

            if (!u.hasRequestFrom(currentUser) && !u.getFriends().contains(currentUser)) {
                System.out.println("User " + currentUser.getId() + " requested " + u.getId() + " as a friend...");
                u.addFriendRequestFrom(currentUser);
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity acceptFriendRequest(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();

        User currentUser = userRepo.findCustomId(currentUserDetails.getUserId());

        System.out.println("Current user # friend request = " + currentUser.getFriendRequests().size());
        Optional<User> userAsking = userRepo.findById(id);

        if (userAsking.isPresent()) {
            User u = userAsking.get();
            if (!currentUser.isFriendWith(u) && currentUser.getFriendRequests().contains(u)) {
                System.out.println("User " + currentUser.getId() + " accepted " + u.getId() + " as a friend");
                currentUser.addFriend(u);
                currentUser.removeFriendRequestFrom(u);
                u.addFriend(currentUser);
                userRepo.save(u);
                userRepo.save(currentUser);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friend-request/{id}/reject")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity rejectFriendRequest(Authentication authentication, @PathVariable Long id) {
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findById(currentUserDetails.getUserId()).get();
        Optional<User> userAsking = userRepo.findById(id);

        if (userAsking.isPresent()) {
            User u = userAsking.get();
            if (!currentUser.getFriends().contains(u) && currentUser.getFriendRequests().contains(u)) {
                System.out.println("User " + currentUser.getId() + " rejected " + u.getId() + " as a friend");
                currentUser.removeFriendRequestFrom(u);
                userRepo.save(currentUser);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found"
            );
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/me/edit")
    public ModelAndView editCurrentUserProfile(Authentication authentication, @ModelAttribute User userInfo, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            System.out.println("Error have occurred");
            result.getAllErrors().forEach(e -> System.out.println("Error: " + e.toString()));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error occurred"
            );
        }

        // Getting user from repo and updating fields one by one
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findById(currentUserDetails.getUserId()).get();

        currentUser.setFirstName(userInfo.getFirstName());
        currentUser.setLastName(userInfo.getLastName());
        currentUser.setEmail(userInfo.getEmail());
        currentUser.setBirthdate(userInfo.getBirthdate());
        userRepo.save(currentUser);

        return new ModelAndView("redirect:/app/users/me");
    }


    @GetMapping("/users/me/positive")
    public ResponseEntity makeCurrentUserPositive(Authentication authentication) {
        // Getting user from repo and updating fields one by one
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findById(currentUserDetails.getUserId()).get();

        if (currentUser.isPositiveToCovid()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the current user is already positive to covid"
            );
        }

        currentUser.setPositiveToCovid(true);
        userRepo.save(currentUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/{id}/mark-as-read")
    public ResponseEntity markNotificationAsRead(Authentication authentication, @PathVariable Long id) {
        // Getting user from repo and updating fields one by one
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findById(currentUserDetails.getUserId()).get();

        Optional<ExposureNotification> notif =  notificationRepo.findById(id);

        if (notif.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "notification not found"
            );
        }

        if (!currentUser.getNotifications().contains(notif.get())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "notification not found"
            );
        }

        ExposureNotification notification = notif.get();
        notification.setOpened(true);
        notificationRepo.save(notification);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/{id}/mark-as-unread")
    public ResponseEntity markNotificationAsUnread(Authentication authentication, @PathVariable Long id) {
        // Getting user from repo and updating fields one by one
        CovidAppUserDetails currentUserDetails = (CovidAppUserDetails) authentication.getPrincipal();
        User currentUser = userRepo.findById(currentUserDetails.getUserId()).get();

        Optional<ExposureNotification> notif =  notificationRepo.findById(id);

        if (notif.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "notification not found"
            );
        }

        if (!currentUser.getNotifications().contains(notif.get())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "notification not found"
            );
        }

        ExposureNotification notification = notif.get();
        notification.setOpened(false);
        notificationRepo.save(notification);

        return ResponseEntity.ok().build();
    }

    private boolean isCurrentUserAdmin(CovidAppUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }

    private User getCurrentUserFromUserDetails(CovidAppUserDetails userDetails) {
        Optional<User> sameUser = userRepo.findById(userDetails.getUserId());
        return sameUser.orElse(null);
    }
}

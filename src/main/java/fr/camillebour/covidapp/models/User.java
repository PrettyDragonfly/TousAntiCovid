package fr.camillebour.covidapp.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    public User() {
        this.friends = new ArrayList<User>();
        this.notifications = new HashSet<Notification>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "is_positive")
    private boolean positiveToCovid;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "friend_id", referencedColumnName = "id"
            )
    )
    private Collection<User> friends;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "friend_request",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "fr_user_id", referencedColumnName = "id"
            )
    )
    private Collection<User> friendRequests;

    @ManyToMany
    @JoinTable(
            name = "activity_participants",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "activity_id", referencedColumnName = "id")
    )
    private Set<Activity> activities;

    @OneToMany
    @JoinTable(
            name = "user_notifications",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "notification_id", referencedColumnName = "id")
    )
    private Set<Notification> notifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    public void addRole(Role r) {
        this.roles.add(r);
    }

    public Collection<User> getFriends() {
        return friends;
    }

    public void setFriends(Collection<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void deleteFriend(User user) { this.friends.remove(user); }

    public void removeFriend(User user) {
        this.friends.remove(user);
    }

    public boolean isFriendWith(User u) {
        return this.friends.contains(u);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User u = (User) o;

        return u.getId().equals(this.getId());
    }

    public Collection<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Collection<User> friendRequest) {
        this.friendRequests = friendRequest;
    }

    public void addFriendRequestFrom(User u) {
        System.out.println("Adding friend request from " + u.getId());
        this.friendRequests.add(u);
    }

    public void removeFriendRequestFrom(User u) {
        this.friendRequests.remove(u);
    }

    public boolean hasRequestFrom(User u) {
        return this.friendRequests.contains(u);
    }

    public boolean participateInActivity(Activity a) {
        return this.activities.contains(a);
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getBirthdateString() {
        if (this.birthdate == null) {
            return "";
        }
        return this.birthdate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
    }

    public String getBirthdateValue() {
        return this.birthdate.toString();
    }

    public String getMaxBirthdateValue() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        if (this.birthdate != null) {
            return Period.between(this.birthdate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public boolean isPositiveToCovid() {
        return positiveToCovid;
    }

    public void setPositiveToCovid(boolean positiveToCovid) {
        this.positiveToCovid = positiveToCovid;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notif) {
        this.notifications.add(notif);
    }

    public Set<User> getUserThatHaveBeenInContact() {
        HashSet<User> contacts = new HashSet<>();
        HashSet<User> friends = new HashSet<>(this.friends);
        HashSet<User> contactActivities = new HashSet<>();

        HashSet<Activity> passedActivities = new HashSet<>();
        this.activities.forEach(a -> {
            if (a.isFinished()) {
                passedActivities.add(a);
            }
        });

        passedActivities.forEach(a -> contactActivities.addAll(a.getParticipants()));
        contactActivities.remove(this);

        contacts.addAll(friends);
        contacts.addAll(contactActivities);

        return contacts;
    }
}

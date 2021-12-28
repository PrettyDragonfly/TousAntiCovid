package fr.camillebour.covidapp.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User {

    public User() {
        this.friends = new ArrayList<User>();
        //this.friendRequest = new ArrayList<FriendRequest>();
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
    private Collection<User> friendRequest;

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

    public Collection<User> getFriends() {
        return friends;
    }

    public void setFriends(Collection<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
    }

    public boolean isFriendWith(User u) {
        return this.friends.contains(u);
    }

    public boolean isFriendWith(CovidAppUserDetails u) {
        return u.isFriendWith(this);
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

    public Collection<User> getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(Collection<User> friendRequest) {
        this.friendRequest = friendRequest;
    }

    public void addFriendRequestFrom(User u) {
        this.friendRequest.add(u);
    }

    public void removeFriendRequestFrom(User u) {
        this.friendRequest.remove(u);
    }

    public boolean isCurrentUser(CovidAppUserDetails userDetails) {
        return this.equals(userDetails.getUser());
    }

    public boolean hasRequestFrom(User u) {
        return this.friendRequest.contains(u);
    }
}

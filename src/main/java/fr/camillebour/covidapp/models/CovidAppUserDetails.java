package fr.camillebour.covidapp.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CovidAppUserDetails implements UserDetails {

    private User user;

    public CovidAppUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Collection<Role> userRoles = user.getRoles();
        for (Role r : userRoles) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public void addFriend(User u) {
        user.addFriend(u);
    }

    public void removeFriend(User u) {
        user.removeFriend(u);
    }

    public Collection<User> getFriends() {
        return user.getFriends();
    }

    public boolean isFriendWith(User u) {
        return user.isFriendWith(u);
    }
}

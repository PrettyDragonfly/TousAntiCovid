package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FRom User u WHERE u.email = ?1")
    User findByEmail(String username);

    @Query("SELECT u FROM User u")
    List<User> findAll();
}

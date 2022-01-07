package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.utils.D3Graph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FRom User u WHERE u.email = ?1")
    User findByEmail(String username);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findCustomId(Long id);

    @Query("SELECT u FROM User u")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE concat(UPPER(u.firstName), ' ', UPPER(u.lastName)) LIKE concat('%', UPPER(?1), '%')")
    List<User> findMatch(@Param("pattern") String pattern);
}



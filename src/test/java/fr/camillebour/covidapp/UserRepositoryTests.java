package fr.camillebour.covidapp;

import static org.assertj.core.api.Assertions.assertThat;

import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("camille@gmail.com");
        user.setPassword(passwordEncoder.encode("securePassword2022!"));
        user.setFirstName("Camille");
        user.setLastName("Bour");

        User savedUser = repo.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());

        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
    }
}

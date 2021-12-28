package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = ?1")
    Role findByName(String r_name);
}

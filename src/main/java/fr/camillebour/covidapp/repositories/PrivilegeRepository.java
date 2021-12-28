package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Query("SELECT p FROM Privilege p WHERE p.name = ?1")
    Privilege findByName(String p_name);
}

package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> { }

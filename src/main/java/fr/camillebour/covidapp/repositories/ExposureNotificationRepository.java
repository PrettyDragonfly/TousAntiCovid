package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.ExposureNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExposureNotificationRepository extends JpaRepository<ExposureNotification, Long> {}



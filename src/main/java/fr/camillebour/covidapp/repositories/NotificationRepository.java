package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}



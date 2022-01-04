package fr.camillebour.covidapp.repositories;

import fr.camillebour.covidapp.models.Activity;
import fr.camillebour.covidapp.models.Location;
import fr.camillebour.covidapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("SELECT a FROM Activity a WHERE :user MEMBER a.participants")
    List<Activity> getActivitiesForUser(@Param("user") User user);
}

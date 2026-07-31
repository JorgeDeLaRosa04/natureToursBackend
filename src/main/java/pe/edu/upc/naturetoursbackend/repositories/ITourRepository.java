package pe.edu.upc.naturetoursbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.naturetoursbackend.entities.Tours;

import java.util.List;

@Repository
public interface ITourRepository extends JpaRepository<Tours,Integer> {

    @Query("SELECT t.id, t.slug, t.name, t.shortDescription, t.fullDescription, " +
            "t.duration_days, t.duration_hours, t.price, t.difficulty_level, t.category, " +
            "t.latitude, t.longitude, t.mapIconType, t.includes, t.excludes, " +
            "t.imageUrl, t.enabled " +
            "FROM Tours t WHERE t.enabled = true")
    List<Object[]> findAllEnabledTours();

    @Query("SELECT t.id, t.slug, t.name, t.shortDescription, t.fullDescription, " +
            "t.duration_days, t.duration_hours, t.price, t.difficulty_level, t.category, " +
            "t.latitude, t.longitude, t.mapIconType, t.includes, t.excludes, " +
            "t.imageUrl, t.enabled " +
            "FROM Tours t, QuizProfiles q " +
            "WHERE q.idQuizProfiles = :quizProfileId " +
            "AND t.enabled = true " +
            "AND t.category = q.interestType " +
            "AND t.duration_days <= q.availableDays " +
            "AND t.difficulty_level = q.adventureLevel")
    List<Object[]> findRecommendedToursByQuizProfileId(@Param("quizProfileId") int quizProfileId);
}
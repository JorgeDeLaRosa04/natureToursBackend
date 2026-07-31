package pe.edu.upc.naturetoursbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;


@Repository
public interface IQuizProfileRepository extends JpaRepository<QuizProfiles,Integer> {

}

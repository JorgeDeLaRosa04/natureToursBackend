package pe.edu.upc.naturetoursbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.naturetoursbackend.entities.Itineraries;
import pe.edu.upc.naturetoursbackend.entities.Tours;

import java.util.List;


@Repository
public interface IItinerariesRepository extends JpaRepository<Itineraries,Integer> {

    @Query("select i from Itineraries i where i.user.id = :id")
    List<Itineraries> findByUserId(int userId);

}

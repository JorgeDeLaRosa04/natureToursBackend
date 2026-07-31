package pe.edu.upc.naturetoursbackend.servicesinterfaces;

import pe.edu.upc.naturetoursbackend.entities.Tours;

import java.util.List;
import java.util.Optional;

public interface ITourService {
    public List<Tours> list();
    public Tours insert(Tours t);
    public Optional<Tours> listId(int id);
    public void update(Tours t);
    public void delete(int id);
    public List<Object[]> findAllEnabledTours();
    public List<Object[]> findRecommendedToursByQuizProfileId(int quizProfileId);

}

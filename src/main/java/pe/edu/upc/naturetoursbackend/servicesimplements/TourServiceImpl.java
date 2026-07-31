package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.naturetoursbackend.entities.Tours;
import pe.edu.upc.naturetoursbackend.repositories.ITourRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.ITourService;

import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements ITourService {

    @Autowired
    private ITourRepository tR;

    @Override
    public List<Tours> list() {
        return tR.findAll();
    }

    @Override
    public Tours insert(Tours t) {
        return tR.save(t);
    }

    @Override
    public Optional<Tours> listId(int id) {
        return tR.findById(id);
    }

    @Override
    public void update(Tours t) {
        tR.save(t);
    }

    @Override
    public void delete(int id) {
        tR.deleteById(id);
    }

    @Override
    public List<Object[]> findAllEnabledTours() {
        return tR.findAllEnabledTours();
    }

    @Override
    public List<Object[]> findRecommendedToursByQuizProfileId(int quizProfileId) {
        return tR.findRecommendedToursByQuizProfileId(quizProfileId);
    }
}
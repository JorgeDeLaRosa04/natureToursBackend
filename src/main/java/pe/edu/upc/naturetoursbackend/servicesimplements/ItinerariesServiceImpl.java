package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.naturetoursbackend.entities.Itineraries;
import pe.edu.upc.naturetoursbackend.repositories.IItinerariesRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IItinerariesService;

import java.util.List;
import java.util.Optional;

@Service
public class ItinerariesServiceImpl implements IItinerariesService {
    @Autowired
    private IItinerariesRepository iR;
    @Override
    public List<Itineraries> list() {
        return iR.findAll();
    }

    @Override
    public Itineraries insert(Itineraries i) {
        return iR.save(i);
    }

    @Override
    public Optional<Itineraries> listId(int id) {
        return iR.findById(id);
    }

    @Override
    public void update(Itineraries i) {
        iR.save(i);
    }

    @Override
    public void delete(int id) {
        iR.deleteById(id);
    }
}

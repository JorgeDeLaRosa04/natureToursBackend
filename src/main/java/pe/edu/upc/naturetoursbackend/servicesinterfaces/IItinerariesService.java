package pe.edu.upc.naturetoursbackend.servicesinterfaces;

import pe.edu.upc.naturetoursbackend.entities.Itineraries;

import java.util.List;
import java.util.Optional;

public interface IItinerariesService {
    public List<Itineraries> list();
    public Itineraries insert(Itineraries i);
    public Optional<Itineraries> listId(int id);
    public void update(Itineraries i);
    public void delete(int id);
}
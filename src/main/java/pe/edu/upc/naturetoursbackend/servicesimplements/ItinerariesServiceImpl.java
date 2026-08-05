package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.naturetoursbackend.dtos.ItinerariesDTO;
import pe.edu.upc.naturetoursbackend.dtos.ItineraryItemDTO;
import pe.edu.upc.naturetoursbackend.entities.Itineraries;
import pe.edu.upc.naturetoursbackend.entities.ItineraryItems;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;
import pe.edu.upc.naturetoursbackend.entities.Tours;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.repositories.IItinerariesRepository;
import pe.edu.upc.naturetoursbackend.repositories.ITourRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IItinerariesService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItinerariesServiceImpl implements IItinerariesService {
    @Autowired
    private IItinerariesRepository iR;

    @Autowired
    private ITourRepository tR;

    @Autowired
    private IUserService uS;

    @Autowired
    private IQuizProfileService qS;

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

    @Override
    public Itineraries createItinerary(ItinerariesDTO dto) {

        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            if (dto.getEndDate().isBefore(dto.getStartDate())) {
                throw new IllegalArgumentException("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }


        Itineraries itinerary = new Itineraries();
        itinerary.setTitle(dto.getTitle());
        itinerary.setStartDate(dto.getStartDate());
        itinerary.setEndDate(dto.getEndDate());
        itinerary.setNumPeople(dto.getNumPeople());
        itinerary.setStatus(true);


        Optional<Users> userOptional = uS.listId(dto.getIdUser());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + dto.getIdUser() + " no encontrado");
        }
        itinerary.setUser(userOptional.get());


        Optional<QuizProfiles> quizOptional = qS.listId(dto.getIdQuiz());
        if (quizOptional.isEmpty()) {
            throw new IllegalArgumentException("Quiz Profile con ID " + dto.getIdQuiz() + " no encontrado");
        }
        itinerary.setQuiz(quizOptional.get());


        List<ItineraryItems> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (ItineraryItemDTO itemDto : dto.getItems()) {

                Optional<Tours> tourOptional = tR.findById(itemDto.getTourId());
                if (tourOptional.isEmpty()) {
                    throw new IllegalArgumentException("Tour con ID " + itemDto.getTourId() + " no encontrado");
                }

                Tours tour = tourOptional.get();
                ItineraryItems item = new ItineraryItems();


                BigDecimal itemPrice = tour.getPrice().multiply(BigDecimal.valueOf(itemDto.getNumPeopleForTour()));
                item.setPriceAtMoment(itemPrice);
                totalPrice = totalPrice.add(itemPrice);


                item.setPlannedDate(itemDto.getPlannedDate());
                item.setNumPeopleForTour(itemDto.getNumPeopleForTour());
                item.setTour(tour);
                item.setItinerary(itinerary);

                items.add(item);
            }
        }

        itinerary.setItems(items);
        itinerary.setTotalEstimatedPrice(totalPrice);
        return iR.save(itinerary);
    }
}

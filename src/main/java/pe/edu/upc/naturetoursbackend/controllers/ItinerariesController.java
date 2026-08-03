package pe.edu.upc.naturetoursbackend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.ItinerariesDTO;
import pe.edu.upc.naturetoursbackend.dtos.ItineraryItemDTO;
import pe.edu.upc.naturetoursbackend.entities.Itineraries;
import pe.edu.upc.naturetoursbackend.entities.ItineraryItems;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;
import pe.edu.upc.naturetoursbackend.entities.Tours;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IItinerariesService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.ITourService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-itineraries")
public class ItinerariesController {

    @Autowired
    private IItinerariesService iS;

    @Autowired
    private IUserService uS;

    @Autowired
    private IQuizProfileService qS;

    @Autowired
    private ITourService tS;

    @GetMapping("/lista")
    public ResponseEntity<List<ItinerariesDTO>> listar() {
        ModelMapper m = new ModelMapper();

        List<ItinerariesDTO> lista = iS.list()
                .stream()
                .map(y -> m.map(y, ItinerariesDTO.class))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> registrar(@RequestBody ItinerariesDTO dto) {

        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            if (dto.getEndDate().isBefore(dto.getStartDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }

        Optional<Users> userOptional = uS.listId(dto.getIdUser());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        Optional<QuizProfiles> quizOptional = qS.listId(dto.getIdQuiz());
        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Quiz Profile no encontrado");
        }

        ModelMapper m = new ModelMapper();
        Itineraries itinerario = new Itineraries();
        itinerario.setTitle(dto.getTitle());
        itinerario.setStartDate(dto.getStartDate());
        itinerario.setEndDate(dto.getEndDate());
        itinerario.setNumPeople(dto.getNumPeople());
        itinerario.setUser(userOptional.get());
        itinerario.setQuiz(quizOptional.get());

        BigDecimal totalEstimatedPrice = BigDecimal.ZERO;
        List<ItineraryItems> itemsList = new ArrayList<>();

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (ItineraryItemDTO itemDto : dto.getItems()) {
                Optional<Tours> tourOptional = tS.listId(itemDto.getTourId());
                if (tourOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Tour con ID " + itemDto.getTourId() + " no encontrado");
                }

                Tours tour = tourOptional.get();
                BigDecimal priceAtMoment = tour.getPrice().multiply(BigDecimal.valueOf(itemDto.getNumPeopleForTour()));

                ItineraryItems item = new ItineraryItems();
                item.setTour(tour);
                item.setPlannedDate(itemDto.getPlannedDate());
                item.setNumPeopleForTour(itemDto.getNumPeopleForTour());
                item.setPriceAtMoment(priceAtMoment);

                itemsList.add(item);
                totalEstimatedPrice = totalEstimatedPrice.add(priceAtMoment);
            }
        }

        itinerario.setItems(itemsList);
        itinerario.setTotalEstimatedPrice(totalEstimatedPrice);

        Itineraries itinerarioGuardado = iS.insert(itinerario);

        ItinerariesDTO responseDTO = m.map(itinerarioGuardado, ItinerariesDTO.class);
        List<ItineraryItemDTO> itemsResponseDTO = itinerarioGuardado.getItems()
                .stream()
                .map(item -> {
                    ItineraryItemDTO itemDto = new ItineraryItemDTO();
                    itemDto.setId(item.getId());
                    itemDto.setTourId(item.getTour().getId());
                    itemDto.setPlannedDate(item.getPlannedDate());
                    itemDto.setNumPeopleForTour(item.getNumPeopleForTour());
                    itemDto.setPriceAtMoment(item.getPriceAtMoment());
                    return itemDto;
                })
                .collect(Collectors.toList());
        responseDTO.setItems(itemsResponseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        ModelMapper m = new ModelMapper();
        Optional<Itineraries> itinerario = iS.listId(id);

        if (itinerario.isPresent()) {
            Itineraries entity = itinerario.get();
            ItinerariesDTO dto = m.map(entity, ItinerariesDTO.class);
            
            List<ItineraryItemDTO> itemsDTO = entity.getItems()
                    .stream()
                    .map(item -> {
                        ItineraryItemDTO itemDto = new ItineraryItemDTO();
                        itemDto.setId(item.getId());
                        itemDto.setTourId(item.getTour().getId());
                        itemDto.setPlannedDate(item.getPlannedDate());
                        itemDto.setNumPeopleForTour(item.getNumPeopleForTour());
                        itemDto.setPriceAtMoment(item.getPriceAtMoment());
                        return itemDto;
                    })
                    .collect(Collectors.toList());
            dto.setItems(itemsDTO);
            
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Itinerario no encontrado");
        }
    }

    @PutMapping("/actualiza")
    public ResponseEntity<String> actualizar(@RequestBody ItinerariesDTO dto) {

        Optional<Itineraries> existente = iS.listId(dto.getIdItineraries());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Itinerario no encontrado");
        }

        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            if (dto.getEndDate().isBefore(dto.getStartDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }

        Itineraries i = existente.get();

        i.setTitle(dto.getTitle());
        i.setStartDate(dto.getStartDate());
        i.setEndDate(dto.getEndDate());
        i.setNumPeople(dto.getNumPeople());
        i.setTotalEstimatedPrice(dto.getTotalEstimatedPrice());

        if (dto.getIdUser() != null) {
            Optional<Users> userOptional = uS.listId(dto.getIdUser());
            if (userOptional.isPresent()) {
                i.setUser(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado");
            }
        }

        if (dto.getIdQuiz() != 0) {
            Optional<QuizProfiles> quizOptional = qS.listId(dto.getIdQuiz());
            if (quizOptional.isPresent()) {
                i.setQuiz(quizOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Quiz Profile no encontrado");
            }
        }

        iS.update(i);

        return ResponseEntity.ok("Itinerario actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        Optional<Itineraries> itinerario = iS.listId(id);

        if (itinerario.isPresent()) {
            iS.delete(id);
            return ResponseEntity.ok("Itinerario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Itinerario no encontrado");
        }
    }
}
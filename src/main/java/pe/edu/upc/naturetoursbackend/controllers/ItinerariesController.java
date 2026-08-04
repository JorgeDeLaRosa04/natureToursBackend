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
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IItinerariesService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.math.BigDecimal;
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

    @GetMapping("/lista")
    public ResponseEntity<List<ItinerariesDTO>> listar() {
        ModelMapper m = new ModelMapper();

        List<ItinerariesDTO> lista = iS.list()
                .stream()
                .map(y -> mapToDTO(y, m))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearItinerario(@RequestBody ItinerariesDTO dto) {
        try {
            Itineraries itineraryCreated = iS.createItinerary(dto);
            ModelMapper m = new ModelMapper();
            ItinerariesDTO responseDTO = mapToDTO(itineraryCreated, m);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el itinerario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        ModelMapper m = new ModelMapper();
        Optional<Itineraries> itinerario = iS.listId(id);

        if (itinerario.isPresent()) {
            ItinerariesDTO dto = mapToDTO(itinerario.get(), m);
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

        // Validación: endDate no debe ser menor a startDate
        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            if (dto.getEndDate().isBefore(dto.getStartDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }

        if (dto.getTotalEstimatedPrice() != null && dto.getTotalEstimatedPrice().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El precio estimado total (totalEstimatedPrice) no puede ser negativo");
        }

        Itineraries i = existente.get();

        i.setTitle(dto.getTitle());
        // Ahora startDate/endDate son LocalDate en la entidad, se asignan directo
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

    private ItinerariesDTO mapToDTO(Itineraries itinerary, ModelMapper mapper) {
        ItinerariesDTO dto = new ItinerariesDTO();
        dto.setIdItineraries(itinerary.getIdItineraries());
        dto.setTitle(itinerary.getTitle());

        // Ya no se necesita cast: la entidad ahora expone LocalDate directamente
        dto.setStartDate(itinerary.getStartDate());
        dto.setEndDate(itinerary.getEndDate());

        dto.setNumPeople(itinerary.getNumPeople());
        dto.setTotalEstimatedPrice(itinerary.getTotalEstimatedPrice());

        // Obtener idUser de forma segura (Users.id es Long)
        if (itinerary.getUser() != null) {
            dto.setIdUser(itinerary.getUser().getId());
        }

        // Obtener idQuiz de forma segura
        if (itinerary.getQuiz() != null) {
            dto.setIdQuiz(itinerary.getQuiz().getIdQuizProfiles());
        }

        if (itinerary.getItems() != null && !itinerary.getItems().isEmpty()) {
            List<ItineraryItemDTO> itemDTOs = itinerary.getItems().stream()
                    .map(item -> mapItemToDTO(item))
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    private ItineraryItemDTO mapItemToDTO(ItineraryItems item) {
        ItineraryItemDTO dto = new ItineraryItemDTO();
        dto.setId(item.getId());

        if (item.getTour() != null) {
            dto.setTourId(item.getTour().getId());
        }

        // Ya no se necesita cast: getPlannedDate() ahora devuelve LocalDate
        dto.setPlannedDate(item.getPlannedDate());

        dto.setNumPeopleForTour(item.getNumPeopleForTour());
        dto.setPriceAtMoment(item.getPriceAtMoment());

        return dto;
    }
}
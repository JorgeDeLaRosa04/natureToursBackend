package pe.edu.upc.naturetoursbackend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.ItinerariesDTO;
import pe.edu.upc.naturetoursbackend.entities.Itineraries;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IItinerariesService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.math.BigDecimal;
import java.util.Date;
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
                .map(y -> m.map(y, ItinerariesDTO.class))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> registrar(@RequestBody ItinerariesDTO dto) {

        // Validación: endDate no debe ser menor a startDate
        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            if (dto.getEndDate().before(dto.getStartDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }

        // Validación: totalEstimatedPrice no debe ser negativo
        if (dto.getTotalEstimatedPrice() != null && dto.getTotalEstimatedPrice().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El precio estimado total (totalEstimatedPrice) no puede ser negativo");
        }

        ModelMapper m = new ModelMapper();
        Itineraries i = m.map(dto, Itineraries.class);

        // Buscar y asignar el usuario
        Optional<Users> userOptional = uS.listId(dto.getIdUser());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }
        i.setUser(userOptional.get());

        // Buscar y asignar el quiz profile
        Optional<QuizProfiles> quizOptional = qS.listId(dto.getIdQuiz());
        if (quizOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Quiz Profile no encontrado");
        }
        i.setQuiz(quizOptional.get());

        Itineraries itinerarioGuardado = iS.insert(i);
        ItinerariesDTO responseDTO = m.map(itinerarioGuardado, ItinerariesDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        ModelMapper m = new ModelMapper();
        Optional<Itineraries> itinerario = iS.listId(id);

        if (itinerario.isPresent()) {
            ItinerariesDTO dto = m.map(itinerario.get(), ItinerariesDTO.class);
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
            if (dto.getEndDate().before(dto.getStartDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La fecha de fin (endDate) no puede ser menor a la fecha de inicio (startDate)");
            }
        }

        // Validación: totalEstimatedPrice no debe ser negativo
        if (dto.getTotalEstimatedPrice() != null && dto.getTotalEstimatedPrice().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El precio estimado total (totalEstimatedPrice) no puede ser negativo");
        }

        Itineraries i = existente.get();

        i.setTitle(dto.getTitle());
        i.setStartDate(dto.getStartDate());
        i.setEndDate(dto.getEndDate());
        i.setNumPeople(dto.getNumPeople());
        i.setTotalEstimatedPrice(dto.getTotalEstimatedPrice());

        // Buscar y asignar el usuario si cambió
        if (dto.getIdUser() != null) {
            Optional<Users> userOptional = uS.listId(dto.getIdUser());
            if (userOptional.isPresent()) {
                i.setUser(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado");
            }
        }

        // Buscar y asignar el quiz profile si cambió
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
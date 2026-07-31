package pe.edu.upc.naturetoursbackend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.ToursDTO;
import pe.edu.upc.naturetoursbackend.entities.Tours;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.ITourService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-tour")
public class TourController {
    @Autowired
    private ITourService tS;
    @GetMapping("/lista")
    public ResponseEntity<List<ToursDTO>> listar() {
        ModelMapper m = new ModelMapper();

        List<ToursDTO> lista = tS.list()
                .stream()
                .map(y -> m.map(y, ToursDTO.class))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> registrar(@RequestBody ToursDTO dto){

        ModelMapper m=new ModelMapper();
        Tours c=m.map(dto, Tours.class);

        Tours cur= tS.insert(c);
        ToursDTO responseDTO=m.map(cur,ToursDTO.class);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        ModelMapper m = new ModelMapper();
        Optional<Tours> project = tS.listId(id);

        if (project.isPresent()) {
            ToursDTO dto = m.map(project.get(), ToursDTO.class);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tour no encontrado");
        }
    }

    @PutMapping("/actualiza")
    public ResponseEntity<String> actualizar(@RequestBody ToursDTO dto) {

        Optional<Tours> existente = tS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tour no encontrado");
        }

        Tours t = existente.get();

        t.setSlug(dto.getSlug());
        t.setName(dto.getName());
        t.setShortDescription(dto.getShortDescription());
        t.setFullDescription(dto.getFullDescription());
        t.setDuration_days(dto.getDuration_days());
        t.setDuration_hours(dto.getDuration_hours());
        t.setPrice(dto.getPrice());
        t.setDifficulty_level(dto.getDifficulty_level());
        t.setCategory(dto.getCategory());
        t.setLatitude(dto.getLatitude());
        t.setLongitude(dto.getLongitude());
        t.setMapIconType(dto.getMapIconType());
        t.setIncludes(dto.getIncludes());
        t.setExcludes(dto.getExcludes());
        t.setImageUrl(dto.getImageUrl());
        t.setEnabled(dto.getEnabled());

        tS.update(t);

        return ResponseEntity.ok("Tour actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        Optional<Tours> tour = tS.listId(id);

        if (tour.isPresent()) {
            tS.delete(id);
            return ResponseEntity.ok("Tour eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tour no encontrado");
        }
    }
    @GetMapping("/enabled")
    public ResponseEntity<?> findAllEnabledTours() {
        List<Object[]> lista = tS.findAllEnabledTours();

        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay registros");
        }

        List<ToursDTO> respuesta = new ArrayList<>();

        for (Object[] fila : lista) {

            ToursDTO dto =
                    new ToursDTO();

            dto.setId(((Number) fila[0]).intValue());
            dto.setSlug((String) fila[1]);
            dto.setName((String) fila[2]);
            dto.setShortDescription((String) fila[3]);
            dto.setFullDescription((String) fila[4]);
            dto.setDuration_days(((Number) fila[5]).intValue());
            dto.setDuration_hours(((Number) fila[6]).intValue());
            dto.setPrice((BigDecimal) fila[7]);
            dto.setDifficulty_level((String) fila[8]);
            dto.setCategory((String) fila[9]);
            dto.setLatitude((BigDecimal) fila[10]);
            dto.setLongitude((BigDecimal) fila[11]);
            dto.setMapIconType((String) fila[12]);
            dto.setIncludes((List<String>) fila[13]);
            dto.setExcludes((List<String>) fila[14]);
            dto.setImageUrl((String) fila[15]);
            dto.setEnabled((Boolean) fila[16]);

            respuesta.add(dto);
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/recommended")
    public ResponseEntity<?> findRecommendedToursByQuizProfileId(@RequestParam int quizProfileId) {
        List<Object[]> lista = tS.findRecommendedToursByQuizProfileId(quizProfileId);

        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay registros");
        }

        List<ToursDTO> respuesta = new ArrayList<>();

        for (Object[] fila : lista) {

            ToursDTO dto =
                    new ToursDTO();

            dto.setId(((Number) fila[0]).intValue());
            dto.setSlug((String) fila[1]);
            dto.setName((String) fila[2]);
            dto.setShortDescription((String) fila[3]);
            dto.setFullDescription((String) fila[4]);
            dto.setDuration_days(((Number) fila[5]).intValue());
            dto.setDuration_hours(((Number) fila[6]).intValue());
            dto.setPrice((BigDecimal) fila[7]);
            dto.setDifficulty_level((String) fila[8]);
            dto.setCategory((String) fila[9]);
            dto.setLatitude((BigDecimal) fila[10]);
            dto.setLongitude((BigDecimal) fila[11]);
            dto.setMapIconType((String) fila[12]);
            dto.setIncludes((List<String>) fila[13]);
            dto.setExcludes((List<String>) fila[14]);
            dto.setImageUrl((String) fila[15]);
            dto.setEnabled((Boolean) fila[16]);

            respuesta.add(dto);
        }

        return ResponseEntity.ok(respuesta);
    }

}

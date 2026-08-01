package pe.edu.upc.naturetoursbackend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.QuizProfilesDTO;
import pe.edu.upc.naturetoursbackend.dtos.ToursDTO;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;
import pe.edu.upc.naturetoursbackend.entities.Tours;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.util.Optional;

@RestController
@RequestMapping("/api-quiz")
public class QuizProfilesController {
    @Autowired
    private IQuizProfileService qS;
    @PostMapping("/nuevo")
    public ResponseEntity<?> registrar(@RequestBody QuizProfilesDTO dto){

        ModelMapper m=new ModelMapper();
        QuizProfiles c=m.map(dto, QuizProfiles.class);

        QuizProfiles cur= qS.insert(c);
        QuizProfilesDTO responseDTO=m.map(cur,QuizProfilesDTO.class);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
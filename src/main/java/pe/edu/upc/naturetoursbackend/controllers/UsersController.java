package pe.edu.upc.naturetoursbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.UserInsertDTO;
import pe.edu.upc.naturetoursbackend.dtos.UserUpdateDTO;
import pe.edu.upc.naturetoursbackend.dtos.UserResponseDTO;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api-users")
public class UsersController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> list() {
        List<Users> users = userService.list();
        List<UserResponseDTO> responseDTOs = new ArrayList<>();
        for (Users user : users) {
            responseDTOs.add(convertToResponseDTO(user));
        }
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        Optional<Users> userOptional = userService.listId(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(convertToResponseDTO(userOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@RequestBody UserInsertDTO insertDTO) {
        Users user = new Users();
        user.setUsername(insertDTO.getUsername());
        user.setEmail(insertDTO.getEmail());
        user.setPassword(insertDTO.getPassword());
        user.setEnabled(true);

        Users savedUser = userService.insert(user);
        return ResponseEntity.ok(convertToResponseDTO(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        Optional<Users> userOptional = userService.listId(id);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setUsername(updateDTO.getUsername());
            user.setPassword(updateDTO.getPassword());
            userService.update(user);
            return ResponseEntity.ok(convertToResponseDTO(user));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDTO convertToResponseDTO(Users user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.getEnabled());
        return dto;
    }
}
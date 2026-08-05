package pe.edu.upc.naturetoursbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.UserInsertDTO;
import pe.edu.upc.naturetoursbackend.dtos.UserUpdateDTO;
import pe.edu.upc.naturetoursbackend.dtos.UserResponseDTO;
import pe.edu.upc.naturetoursbackend.entities.Tours;
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

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> list() {
        List<Users> users = userService.list();
        List<UserResponseDTO> responseDTOs = new ArrayList<>();
        for (Users user : users) {
            responseDTOs.add(convertToResponseDTO(user));
        }
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        // Validar que USER solo puede ver su propio perfil
        org.springframework.security.core.Authentication authentication = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            if (!isAdmin) {
                String currentUserEmail = authentication.getName();
                Optional<Users> currentUser = userService.findByEmail(currentUserEmail);
                
                if (currentUser.isPresent() && !currentUser.get().getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(null);
                }
            }
        }
        
        Optional<Users> userOptional = userService.listId(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(convertToResponseDTO(userOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    /*
    @PostMapping("/crear-cuenta")
    public ResponseEntity<UserResponseDTO> insert(@RequestBody UserInsertDTO insertDTO) {
        Users user = new Users();
        user.setUsername(insertDTO.getUsername());
        user.setEmail(insertDTO.getEmail());
        user.setPassword(insertDTO.getPassword());
        user.setEnabled(true);

        Users savedUser = userService.insert(user);
        return ResponseEntity.ok(convertToResponseDTO(savedUser));
    }

    @PutMapping("/cambiar-contraseña")
    public ResponseEntity<String> actualizar(@RequestBody UserUpdateDTO dto) {

        Optional<Users> existente = userService.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        Users user = existente.get();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());


        userService.update(user);

        return ResponseEntity.ok("Usuario actualizado correctamente");
    }
    */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Users> user = userService.listId(id);

        if (user.isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }
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
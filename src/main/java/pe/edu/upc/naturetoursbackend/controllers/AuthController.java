package pe.edu.upc.naturetoursbackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.naturetoursbackend.dtos.*;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IAuthService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, verificación, login y recuperación de contraseña")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario con email y contraseña. Envía automáticamente un correo de verificación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cuenta creada correctamente",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Validaciones básicas
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("El nombre de usuario es obligatorio"));
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("El correo electrónico es obligatorio"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("La contraseña es obligatoria"));
            }

            if (request.getPassword().length() < 8) {
                return ResponseEntity.badRequest().body(new MessageResponse("La contraseña debe tener al menos 8 caracteres"));
            }


            authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse("Cuenta creada correctamente. Revisa tu correo electrónico para verificar tu cuenta.")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new MessageResponse("Error al crear la cuenta. Intente nuevamente.")
            );
        }
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verificar correo electrónico", description = "Verifica el correo electrónico usando el token recibido por email.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correo verificado correctamente",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            boolean verified = authService.verifyEmail(token);
            
            if (verified) {
                return ResponseEntity.ok(new MessageResponse("Correo electrónico verificado correctamente."));
            } else {
                return ResponseEntity.badRequest().body(
                    new MessageResponse("El enlace de verificación es inválido o ha expirado.")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new MessageResponse("El enlace de verificación es inválido o ha expirado.")
            );
        }
    }

    /*
    @PostMapping("/resend-verification")
    @Operation(summary = "Reenviar correo de verificación", description = "Reenvía el correo de verificación a un usuario que no ha verificado su cuenta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correo reenviado si la cuenta existe y necesita verificación",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> resendVerification(@RequestBody ResendVerificationRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("El correo electrónico es obligatorio"));
            }

            authService.resendVerificationEmail(request.getEmail());
            
            // Por seguridad, no revelar si el email existe
            return ResponseEntity.ok(new MessageResponse(
                "Si la cuenta existe y necesita verificación, recibirás un nuevo correo."
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(
                "Si la cuenta existe y necesita verificación, recibirás un nuevo correo."
            ));
        }
    }
    */
    @PostMapping("/forgot-password")
    @Operation(summary = "Recuperar contraseña", description = "Envía un correo con instrucciones para restablecer la contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correo enviado si la cuenta existe",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("El correo electrónico es obligatorio"));
            }

            authService.forgotPassword(request.getEmail());
            

            return ResponseEntity.ok(new MessageResponse(
                "Si existe una cuenta asociada a ese correo, recibirás instrucciones para recuperar tu contraseña."
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(
                "Si existe una cuenta asociada a ese correo, recibirás instrucciones para recuperar tu contraseña."
            ));
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contraseña", description = "Restablece la contraseña usando el token recibido por correo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Token inválido, expirado o contraseña inválida",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("El token es obligatorio"));
            }
            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("La nueva contraseña es obligatoria"));
            }

            boolean success = authService.resetPassword(request.getToken(), request.getNewPassword());
            
            if (success) {
                return ResponseEntity.ok(new MessageResponse("La contraseña se actualizó correctamente."));
            } else {
                return ResponseEntity.badRequest().body(
                    new MessageResponse("El enlace de recuperación es inválido o ha expirado.")
                );
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new MessageResponse("El enlace de recuperación es inválido o ha expirado.")
            );
        }
    }
}

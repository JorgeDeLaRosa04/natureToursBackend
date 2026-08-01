package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.naturetoursbackend.entities.Provider;
import pe.edu.upc.naturetoursbackend.entities.Role;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.repositories.IUserRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IAuthService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final int TOKEN_LENGTH = 64;
    private static final long VERIFICATION_TOKEN_EXPIRATION_HOURS = 24;
    private static final long RESET_PASSWORD_TOKEN_EXPIRATION_MINUTES = 30;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Genera un token criptográficamente seguro
     */
    private String generateSecureToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Genera el hash de un token usando BCrypt
     */
    private String hashToken(String token) {
        return passwordEncoder.encode(token);
    }

    /**
     * Registra un nuevo usuario con email y contraseña
     */
    @Override
    @Transactional
    public Users register(String username, String email, String password) {
        // Verificar si el email ya existe
        if (userRepository.findOneByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Buscar por email también
        Users existingByEmail = findByEmail(email).orElse(null);
        if (existingByEmail != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);
        user.setEmailVerified(false);
        user.setProvider(Provider.LOCAL);

        // Guardar usuario primero para obtener el ID
        Users savedUser = userRepository.save(user);

        // Asignar rol ROLE_USER automáticamente
        Role userRole = new Role();
        userRole.setRol("ROLE_USER");
        userRole.setUser(savedUser);
        
        // Guardar el rol usando la consulta nativa del repositorio
        userRepository.insRol("ROLE_USER", savedUser.getId());

        // Generar token de verificación
        String verificationToken = generateSecureToken();
        String tokenHash = hashToken(verificationToken);
        LocalDateTime expiration = LocalDateTime.now().plusHours(VERIFICATION_TOKEN_EXPIRATION_HOURS);

        savedUser.setVerificationTokenHash(tokenHash);
        savedUser.setVerificationTokenExpiration(expiration);
        userRepository.save(savedUser);

        // Enviar correo de verificación
        try {
            emailService.sendVerificationEmail(email, verificationToken);
        } catch (Exception e) {
            // Si falla el envío de email, registrar pero no fallar el registro
            // En producción se podría implementar una cola de reintentos
            System.err.println("Error al enviar correo de verificación: " + e.getMessage());
        }

        return savedUser;
    }

    /**
     * Verifica el correo electrónico mediante el token
     */
    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        String tokenHash = hashToken(token);

        // Buscar todos los usuarios y verificar manualmente (no hay índice para esto)
        // En producción se debería optimizar con una consulta personalizada
        var allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            if (user.getVerificationTokenHash() != null &&
                user.getVerificationTokenExpiration() != null &&
                user.getVerificationTokenExpiration().isAfter(LocalDateTime.now())) {
                
                // Verificar el hash del token
                if (passwordEncoder.matches(token, user.getVerificationTokenHash())) {
                    // Usuario encontrado y token válido
                    user.setEmailVerified(true);
                    user.setEnabled(true);
                    user.setVerificationTokenHash(null);
                    user.setVerificationTokenExpiration(null);
                    userRepository.save(user);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Reenvía el correo de verificación
     */
    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        Optional<Users> userOptional = findByEmail(email);
        
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            
            // Solo reenviar si es usuario LOCAL y no está verificado
            if (user.getProvider() == Provider.LOCAL && !user.getEmailVerified()) {
                // Invalidar token anterior y generar uno nuevo
                String verificationToken = generateSecureToken();
                String tokenHash = hashToken(verificationToken);
                LocalDateTime expiration = LocalDateTime.now().plusHours(VERIFICATION_TOKEN_EXPIRATION_HOURS);

                user.setVerificationTokenHash(tokenHash);
                user.setVerificationTokenExpiration(expiration);
                userRepository.save(user);

                // Enviar nuevo correo
                try {
                    emailService.sendVerificationEmail(email, verificationToken);
                } catch (Exception e) {
                    System.err.println("Error al reenviar correo de verificación: " + e.getMessage());
                }
            }
        }
        // Por seguridad, no revelar si el email existe o no
    }

    /**
     * Solicita recuperación de contraseña
     */
    @Override
    @Transactional
    public void forgotPassword(String email) {
        Optional<Users> userOptional = findByEmail(email);
        
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            
            // Solo permitir para usuarios LOCAL
            if (user.getProvider() == Provider.LOCAL) {
                // Generar token de recuperación
                String resetToken = generateSecureToken();
                String tokenHash = hashToken(resetToken);
                LocalDateTime expiration = LocalDateTime.now().plusMinutes(RESET_PASSWORD_TOKEN_EXPIRATION_MINUTES);

                user.setResetPasswordTokenHash(tokenHash);
                user.setResetPasswordTokenExpiration(expiration);
                userRepository.save(user);

                // Enviar correo de recuperación
                try {
                    emailService.sendPasswordResetEmail(email, resetToken);
                } catch (Exception e) {
                    System.err.println("Error al enviar correo de recuperación: " + e.getMessage());
                }
            }
        }
        // Por seguridad, no revelar si el email existe o no
    }

    /**
     * Restablece la contraseña usando el token
     */
    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        if (token == null || token.trim().isEmpty() || 
            newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }

        // Validar política de contraseña simple
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        String tokenHash = hashToken(token);

        // Buscar usuario con token válido
        var allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            if (user.getResetPasswordTokenHash() != null &&
                user.getResetPasswordTokenExpiration() != null &&
                user.getResetPasswordTokenExpiration().isAfter(LocalDateTime.now())) {
                
                // Verificar el hash del token
                if (passwordEncoder.matches(token, user.getResetPasswordTokenHash())) {
                    // Token válido, actualizar contraseña
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetPasswordTokenHash(null);
                    user.setResetPasswordTokenExpiration(null);
                    userRepository.save(user);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Busca usuario por email
     */
    @Override
    public Optional<Users> findByEmail(String email) {
        var allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            if (email.equals(user.getEmail())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Busca usuario por username
     */
    @Override
    public Optional<Users> findByUsername(String username) {
        Users user = userRepository.findOneByUsername(username);
        return Optional.ofNullable(user);
    }
}

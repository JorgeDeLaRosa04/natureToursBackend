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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private String generateSecureToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }

    private boolean verifyTokenHash(String token, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return MessageDigest.isEqual(
            hashToken(token).getBytes(StandardCharsets.UTF_8),
            storedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    @Transactional
    public Users register(String username, String email, String password) {

        if (userRepository.findOneByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }


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


        Users savedUser = userRepository.save(user);


        Role userRole = new Role();
        userRole.setRol("USER");
        userRole.setUser(savedUser);
        

        userRepository.insRol("USER", savedUser.getId());


        String verificationToken = generateSecureToken();
        String tokenHash = hashToken(verificationToken);
        LocalDateTime expiration = LocalDateTime.now().plusHours(VERIFICATION_TOKEN_EXPIRATION_HOURS);

        savedUser.setVerificationTokenHash(tokenHash);
        savedUser.setVerificationTokenExpiration(expiration);
        userRepository.save(savedUser);


        try {
            emailService.sendVerificationEmail(email, verificationToken);
        } catch (Exception e) {

            System.err.println("Error al enviar correo de verificación: " + e.getMessage());
        }

        return savedUser;
    }


    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }


        var allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            if (user.getVerificationTokenHash() != null &&
                user.getVerificationTokenExpiration() != null &&
                user.getVerificationTokenExpiration().isAfter(LocalDateTime.now())) {
                
                // Verificar el hash del token (SHA-256)
                if (verifyTokenHash(token, user.getVerificationTokenHash())) {
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


                try {
                    emailService.sendVerificationEmail(email, verificationToken);
                } catch (Exception e) {
                    System.err.println("Error al reenviar correo de verificación: " + e.getMessage());
                }
            }
        }

    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        Optional<Users> userOptional = findByEmail(email);
        
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            

            if (user.getProvider() == Provider.LOCAL) {

                String resetToken = generateSecureToken();
                String tokenHash = hashToken(resetToken);
                LocalDateTime expiration = LocalDateTime.now().plusMinutes(RESET_PASSWORD_TOKEN_EXPIRATION_MINUTES);

                user.setResetPasswordTokenHash(tokenHash);
                user.setResetPasswordTokenExpiration(expiration);
                userRepository.save(user);


                try {
                    emailService.sendPasswordResetEmail(email, resetToken);
                } catch (Exception e) {
                    System.err.println("Error al enviar correo de recuperación: " + e.getMessage());
                }
            }
        }

    }


    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        if (token == null || token.trim().isEmpty() || 
            newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }


        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }


        var allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            if (user.getResetPasswordTokenHash() != null &&
                user.getResetPasswordTokenExpiration() != null &&
                user.getResetPasswordTokenExpiration().isAfter(LocalDateTime.now())) {
                

                if (verifyTokenHash(token, user.getResetPasswordTokenHash())) {

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


    @Override
    public Optional<Users> findByUsername(String username) {
        Users user = userRepository.findOneByUsername(username);
        return Optional.ofNullable(user);
    }
}

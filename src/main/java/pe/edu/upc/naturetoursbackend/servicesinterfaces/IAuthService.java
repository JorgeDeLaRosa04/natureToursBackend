package pe.edu.upc.naturetoursbackend.servicesinterfaces;

import pe.edu.upc.naturetoursbackend.entities.Users;

import java.util.Optional;

public interface IAuthService {
    Users register(String username, String email, String password);
    boolean verifyEmail(String token);
    void resendVerificationEmail(String email);
    void forgotPassword(String email);
    boolean resetPassword(String token, String newPassword);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
}

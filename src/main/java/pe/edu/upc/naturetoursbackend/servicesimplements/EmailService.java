package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía correo de verificación de email
     */
    public void sendVerificationEmail(String to, String token) {
        String subject = "Verifica tu correo electrónico - NatureTours";
        
        String verificationLink = frontendUrl + "/verify-email?token=" + token;
        
        String body = "Hola,\n\n" +
                "Gracias por registrarte en NatureTours.\n\n" +
                "Para verificar tu cuenta, por favor haz clic en el siguiente enlace:\n\n" +
                verificationLink + "\n\n" +
                "Este enlace expirará en 24 horas.\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este correo.\n\n" +
                "Saludos,\n" +
                "El equipo de NatureTours";

        sendEmail(to, subject, body);
    }

    /**
     * Envía correo de recuperación de contraseña
     */
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Recuperación de contraseña - NatureTours";
        
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        
        String body = "Hola,\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña en NatureTours.\n\n" +
                "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n\n" +
                resetLink + "\n\n" +
                "Este enlace expirará en 30 minutos.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo y tu contraseña permanecerá sin cambios.\n\n" +
                "Saludos,\n" +
                "El equipo de NatureTours";

        sendEmail(to, subject, body);
    }

    /**
     * Método genérico para enviar correos
     */
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        mailSender.send(message);
    }
}

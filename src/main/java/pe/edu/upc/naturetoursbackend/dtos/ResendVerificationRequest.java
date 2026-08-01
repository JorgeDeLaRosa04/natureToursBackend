package pe.edu.upc.naturetoursbackend.dtos;

public class ResendVerificationRequest {
    private String email;

    public ResendVerificationRequest() {
        super();
    }

    public ResendVerificationRequest(String email) {
        super();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

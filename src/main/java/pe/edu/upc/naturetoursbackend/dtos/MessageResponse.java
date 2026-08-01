package pe.edu.upc.naturetoursbackend.dtos;

public class MessageResponse {
    private String message;

    public MessageResponse() {
        super();
    }

    public MessageResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

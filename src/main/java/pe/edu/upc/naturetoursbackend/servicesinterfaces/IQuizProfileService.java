package pe.edu.upc.naturetoursbackend.servicesinterfaces;

import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;

import java.util.List;
import java.util.Optional;

public interface IQuizProfileService {
    public List<QuizProfiles> list();
    public QuizProfiles insert(QuizProfiles qp);
    public Optional<QuizProfiles> listId(int id);
    public void update(QuizProfiles qp);
    public void delete(int id);
}
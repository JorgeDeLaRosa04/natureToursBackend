package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.naturetoursbackend.entities.QuizProfiles;
import pe.edu.upc.naturetoursbackend.repositories.IQuizProfileRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IQuizProfileService;

import java.util.List;
import java.util.Optional;

@Service
public class QuizProfileServiceImpl implements IQuizProfileService {

    @Autowired
    private IQuizProfileRepository qR;

    @Override
    public List<QuizProfiles> list() {
        return qR.findAll();
    }

    @Override
    public QuizProfiles insert(QuizProfiles qp) {
        return qR.save(qp);
    }

    @Override
    public Optional<QuizProfiles> listId(int id) {
        return qR.findById(id);
    }

    @Override
    public void update(QuizProfiles qp) {
        qR.save(qp);
    }

    @Override
    public void delete(int id) {
        qR.deleteById(id);
    }
}


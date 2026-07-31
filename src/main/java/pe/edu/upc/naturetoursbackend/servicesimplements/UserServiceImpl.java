package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.naturetoursbackend.entities.Users;
import pe.edu.upc.naturetoursbackend.repositories.IUserRepository;
import pe.edu.upc.naturetoursbackend.servicesinterfaces.IUserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {


    @Override
    public List<Users> list() {
        return List.of();
    }

    @Override
    public Optional<Users> listId(Long id) {
        return Optional.empty();
    }

    @Override
    public Users insert(Users u) {
        return null;
    }

    @Override
    public void update(Users u) {

    }

    @Override
    public void delete(Long id) {

    }
}
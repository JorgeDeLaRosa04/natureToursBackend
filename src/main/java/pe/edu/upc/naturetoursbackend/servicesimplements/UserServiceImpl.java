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

    @Autowired
    private IUserRepository uR;

    @Override
    public List<Users> list() {
        return uR.findAll();
    }

    @Override
    public Optional<Users> listId(Long id) {
        return uR.findById(id);
    }

    @Override
    public Users insert(Users u) {
        return uR.save(u);
    }

    @Override
    public void update(Users u) {
        uR.save(u);
    }

    @Override
    public void delete(Long id) {
        uR.deleteById(id);
    }
}

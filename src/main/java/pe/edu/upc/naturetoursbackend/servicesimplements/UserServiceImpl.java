package pe.edu.upc.naturetoursbackend.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        return uR.save(u);
    }

    @Override
    public void update(Users u) {

        if (u.getPassword() == null || u.getPassword().isEmpty()) {
            Users existing = uR.findById(u.getId()).orElse(null);
            if (existing != null) {
                u.setPassword(existing.getPassword());
            }
        } else if (!isPasswordHashed(u.getPassword())) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        uR.save(u);
    }

    @Override
    public void delete(Long id) {
        uR.deleteById(id);
    }

    private boolean isPasswordHashed(String password) {
        return password != null && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }
}

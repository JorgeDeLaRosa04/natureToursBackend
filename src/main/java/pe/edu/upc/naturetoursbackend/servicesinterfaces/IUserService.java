package pe.edu.upc.naturetoursbackend.servicesinterfaces;

import pe.edu.upc.naturetoursbackend.entities.Users;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<Users> list();
    public Optional<Users> listId(Long id);
    public Users insert(Users u);
    public void update(Users u);
    public void delete(Long id);
    public Optional<Users> findByEmail(String email);
}
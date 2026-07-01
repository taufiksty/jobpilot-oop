package repository;

import java.util.List;
import java.util.Optional;

import model.User;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(int id);

    User save(User user);
}

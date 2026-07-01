package repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import exception.HandleException;
import model.User;
import repository.UserRepository;

public class UserRepositoryInMemoryImpl implements UserRepository {
    private final AtomicInteger idCounter = new AtomicInteger(2);
    private final List<User> users = new ArrayList<>(
            List.of(
                    new User(1, "Taufik", "password123")));

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == 0) {
            user.setId(idCounter.getAndIncrement());
            users.add(user);
            return user;
        } else {
            // Update existing user
            Optional<User> existingUserOpt = findById(user.getId());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                existingUser.setName(user.getName());
                existingUser.setPassword(user.getPassword());
                existingUser.setLastLoginAt(user.getLastLoginAt());
                existingUser.setUpdatedAt(user.getUpdatedAt());
                existingUser.setDeletedAt(user.getDeletedAt());
                existingUser.setCreatedBy(user.getCreatedBy());
                existingUser.setUpdatedBy(user.getUpdatedBy());
                existingUser.setDeletedBy(user.getDeletedBy());
                return existingUser;
            } else {
                throw new HandleException("User with id " + user.getId() + " does not exist.");
            }
        }
    }
}

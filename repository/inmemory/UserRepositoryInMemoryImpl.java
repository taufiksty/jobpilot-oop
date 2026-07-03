package repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import exception.HandleException;
import model.User;
import repository.UserRepository;

public class UserRepositoryInMemoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>(
            List.of(
                    new User("user-001", "Taufik", "password123")));

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
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

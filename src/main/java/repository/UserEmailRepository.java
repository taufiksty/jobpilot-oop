package repository;

import java.util.Optional;

import model.UserEmail;

public interface UserEmailRepository {

    Optional<UserEmail> findById(String id);

    Optional<UserEmail> findByEmail(String email);

    UserEmail save(UserEmail userEmail);
}

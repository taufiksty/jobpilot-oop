package repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import exception.HandleException;
import model.UserEmail;
import repository.UserEmailRepository;

public class UserEmailRepositoryInMemoryImpl implements UserEmailRepository {
    private final List<UserEmail> userEmails = new ArrayList<>(
            List.of(
                    new UserEmail("email-001", "user-001", "taufik@gmail.com", null, "local")));

    @Override
    public Optional<UserEmail> findById(String id) {
        return userEmails.stream()
                .filter(userEmail -> userEmail.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<UserEmail> findByEmail(String email) {

        return userEmails.stream()
                .filter(userEmail -> userEmail.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public UserEmail save(UserEmail userEmail) {
        if (userEmail.getId() == null) {
            userEmail.setId(UUID.randomUUID().toString());
            userEmails.add(userEmail);
            return userEmail;
        } else {
            // Update existing userEmail
            Optional<UserEmail> existingUserEmailOpt = findById(userEmail.getId());
            if (existingUserEmailOpt.isPresent()) {
                UserEmail existingUserEmail = existingUserEmailOpt.get();
                existingUserEmail.setUserId(userEmail.getUserId());
                existingUserEmail.setEmail(userEmail.getEmail());
                existingUserEmail.setProviderId(userEmail.getProviderId());
                existingUserEmail.setProviderName(userEmail.getProviderName());
                existingUserEmail.setUpdatedAt(userEmail.getUpdatedAt());
                existingUserEmail.setDeletedAt(userEmail.getDeletedAt());
                existingUserEmail.setCreatedBy(userEmail.getCreatedBy());
                existingUserEmail.setUpdatedBy(userEmail.getUpdatedBy());
                existingUserEmail.setDeletedBy(userEmail.getDeletedBy());
                return existingUserEmail;
            } else {
                throw new HandleException("UserEmail with id " + userEmail.getId() + " does not exist.");
            }
        }
    }

}

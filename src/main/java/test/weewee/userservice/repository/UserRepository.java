package test.weewee.userservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.weewee.userservice.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);

    // Можно добавить дополнительные методы когда понадобятся:
    // Optional<User> findByVerificationCode(String verificationCode);
    // Optional<User> findByRecoveryToken(String recoveryToken);
    // List<User> findByRole(User.Role role);
}
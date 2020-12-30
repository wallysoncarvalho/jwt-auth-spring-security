package wally.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wally.info.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findUserByUsername(String username);
	boolean existsByUsernameOrEmail(String username, String email);
}

package wally.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wally.info.entity.User;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String> {
	Optional<User> findUserByUsername(String username);
	boolean existsByUsername(String username);
}

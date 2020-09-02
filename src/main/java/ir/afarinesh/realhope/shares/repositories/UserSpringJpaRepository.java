package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.security.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSpringJpaRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}

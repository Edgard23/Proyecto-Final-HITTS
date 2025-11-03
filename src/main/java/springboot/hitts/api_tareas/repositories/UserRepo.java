package springboot.hitts.api_tareas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hitts.api_tareas.models.User;

public interface UserRepo extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

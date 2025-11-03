package springboot.hitts.api_tareas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hitts.api_tareas.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);

}

package springboot.hitts.api_tareas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hitts.api_tareas.models.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task> findByOwnerId(Long ownerId);

}

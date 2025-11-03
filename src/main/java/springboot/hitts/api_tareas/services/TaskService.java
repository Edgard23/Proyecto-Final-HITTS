package springboot.hitts.api_tareas.services;

import java.util.List;
import java.util.Optional;

import DTO.TaskDto;
import springboot.hitts.api_tareas.models.Task;

public interface TaskService {
    Task createTask(TaskDto dto);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task updateTask(Long id, TaskDto dto);
    void deleteTask(Long id);
    List<Task> getTasksById(Long userId);
}

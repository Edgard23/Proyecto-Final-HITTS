package springboot.hitts.api_tareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import DTO.TaskDto;
import springboot.hitts.api_tareas.models.Task;
import springboot.hitts.api_tareas.models.User;
import springboot.hitts.api_tareas.repositories.TaskRepository;
import springboot.hitts.api_tareas.repositories.UserRepo;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepo;
    private final UserRepo userRepo;
    public TaskServiceImpl(TaskRepository taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Task createTask(TaskDto dto) {
        Task t = new Task();
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setDueDate(dto.getDueDate());
        if (dto.getOwnerId() != null) {
            User u = userRepo.findById(dto.getOwnerId()).orElseThrow();
            t.setOwner(u);
        }    
        return taskRepo.save(t);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepo.findById(id);
    }

    @Override
    public Task updateTask(Long id, TaskDto dto) {
        Task t = taskRepo.findById(id).orElseThrow();
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setCompleted(dto.isCompleted());
        t.setDueDate(dto.getDueDate());
        if (dto.getOwnerId() != null) {
            User u = userRepo.findById(dto.getOwnerId()).orElseThrow();
            t.setOwner(u);
        }
        return taskRepo.save(t);
    }
    @Override
    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
        
    }
    @Override
    public List<Task> getTasksById(Long userId) {
        return taskRepo.findByOwnerId(userId);
    }
}

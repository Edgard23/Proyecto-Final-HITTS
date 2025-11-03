package springboot.hitts.api_tareas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DTO.TaskDto;
import jakarta.validation.Valid;
import springboot.hitts.api_tareas.models.Task;
import springboot.hitts.api_tareas.repositories.UserRepo;
import springboot.hitts.api_tareas.services.TaskService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserRepo userRepo;
    public TaskController(TaskService taskService, UserRepo userRepo) {
        this.taskService = taskService;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<Task> getAll(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return taskService.getAllTasks();
        }
        var username = auth.getName();
        var user = userRepo.findByUsername(username).orElseThrow();
        return taskService.getTasksById(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return taskService.getTaskById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskDto dto, Authentication auth) {
        if (dto.getOwnerId() == null) {
            var user = userRepo.findByUsername(auth.getName()).orElseThrow();
            dto.setOwnerId(user.getId());
        }
        Task created = taskService.createTask(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody TaskDto dto, Authentication auth) {
        var opt = taskService.getTaskById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task t = opt.get();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        if (!isAdmin && (t.getOwner() == null || !t.getOwner().getId().equals(user.getId()))) {
            return ResponseEntity.status(403).build();
        }
        Task updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

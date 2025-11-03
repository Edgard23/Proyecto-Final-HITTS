package springboot.hitts.api_tareas.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import DTO.SignUpRequest;
import springboot.hitts.api_tareas.models.Role;
import springboot.hitts.api_tareas.models.User;
import springboot.hitts.api_tareas.repositories.RoleRepository;
import springboot.hitts.api_tareas.repositories.UserRepo;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    
    public UserServiceImpl(UserRepo userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User registerUser(SignUpRequest request) {
        User u = new User();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow();
            roles.add(userRole);
        } else {
            request.getRoles().forEach(r -> {String roleName = r.equalsIgnoreCase("admin") ? "ROLE_ADMIN" : "ROLE_USER";
        roleRepo.findByName(roleName).ifPresent(roles::add);
        });
        }
        u.getRoles().addAll(roles);
        return userRepo.save(u);
    }
}

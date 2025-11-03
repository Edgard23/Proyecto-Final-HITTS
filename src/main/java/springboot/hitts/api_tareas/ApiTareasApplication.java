package springboot.hitts.api_tareas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import springboot.hitts.api_tareas.models.Role;
import springboot.hitts.api_tareas.models.User;
import springboot.hitts.api_tareas.repositories.RoleRepository;
import springboot.hitts.api_tareas.repositories.UserRepo;

@SpringBootApplication
public class ApiTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTareasApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepo, UserRepo userRepo, PasswordEncoder encoder){
		return args -> {
			Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));

			if (!userRepo.existsByUsername("admin")) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(encoder.encode("Admin1234"));
				admin.getRoles().add(adminRole);
				admin.getRoles().add(userRole);
				userRepo.save(admin);
			}
		};
	}
}

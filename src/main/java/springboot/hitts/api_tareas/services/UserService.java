package springboot.hitts.api_tareas.services;

import DTO.SignUpRequest;
import springboot.hitts.api_tareas.models.User;

public interface UserService {
    User registerUser(SignUpRequest request);
}

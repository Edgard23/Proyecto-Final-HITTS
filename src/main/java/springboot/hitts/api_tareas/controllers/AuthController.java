package springboot.hitts.api_tareas.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DTO.AuthRequest;
import DTO.AuthResponse;
import DTO.SignUpRequest;
import jakarta.validation.Valid;
import springboot.hitts.api_tareas.security.JwtUtil;
import springboot.hitts.api_tareas.security.UserDetailsServiceImpl;
import springboot.hitts.api_tareas.services.UserService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    
    public AuthController(AuthenticationManager authManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil,
            UserService userService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails ud = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(ud.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest req) {
        if (userService == null) {
            return ResponseEntity.status(500).body("Service unavailable");
        }
        var user = userService.registerUser(req);
        return ResponseEntity.ok("Usuario creado: " + user.getUsername());
    }
}

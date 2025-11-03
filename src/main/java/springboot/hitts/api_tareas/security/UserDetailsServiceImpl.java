package springboot.hitts.api_tareas.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.var;
import springboot.hitts.api_tareas.models.User;
import springboot.hitts.api_tareas.repositories.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var authorities = u.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
        return org.springframework.security.core.userdetails.User.builder()
                  .username(u.getUsername())
                  .password(u.getPassword())
                  .authorities(authorities)
                  .build();
    }
    
    

}

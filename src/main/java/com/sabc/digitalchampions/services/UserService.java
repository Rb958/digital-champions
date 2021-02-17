package com.sabc.digitalchampions.services;

import com.sabc.digitalchampions.entity.User;
import com.sabc.digitalchampions.repository.UserRepository;
import com.sabc.digitalchampions.security.jwt.JwtUtils;
import com.sabc.digitalchampions.security.payload.response.JwtResponse;
import com.sabc.digitalchampions.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User register(User user){
        // Create new user's account
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        if(user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return this.userRepository.save(user);
    }

    public JwtResponse login(User user){
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> ((GrantedAuthority) item).getAuthority()).collect(Collectors.toList());
        return new JwtResponse(jwtToken, userDetails.getUsername(), roles);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Page<User> getUsers(Optional<String> username, Pageable pageable){
        return userRepository.findAll(username.orElse(""), pageable);
    }

    public boolean existsByRef(String ref) {
        return userRepository.existsByRef(ref);
    }

    public User findByRef(String ref) {
        return userRepository.findByRef(ref);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}

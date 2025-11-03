package com.skillmatch.backend.controller;

import com.skillmatch.backend.model.User;
import com.skillmatch.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get current user info by jwt email
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found: " + email));

        return ResponseEntity.ok(user);
    }

    // update user skills list
    @Transactional
    @PutMapping("/skills")
    public ResponseEntity<User> updateMySkills(Principal principal, @RequestBody List<String> newSkills) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found: " + email));

        // clear old skills and add new ones
        List<String> managedSkills = user.getUserSkills();
        managedSkills.clear();
        managedSkills.addAll(newSkills);

        userRepository.save(user);

        System.out.println(">>> user " + email + " updated skills: " + newSkills);
        return ResponseEntity.ok(user);
    }
}

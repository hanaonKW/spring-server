package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.apiPayload.exception.ResourceNotFoundException;
import hanaon.AiAssistant.domain.User;
import hanaon.AiAssistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setSignupDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @GetMapping("/{user_id}")
    public User getUser(@PathVariable("user_id") Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PutMapping("/{user_id}")
    public User updateUser(@PathVariable("user_id") Long userId, @RequestBody User userDetails) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }
}

package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.apiPayload.exception.ResourceNotFoundException;
import hanaon.AiAssistant.domain.User;
import hanaon.AiAssistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setSignupDate(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void getUser() {
        User user = new User();
        user.setUserId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userController.getUser(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getUserId());
        assertEquals("John Doe", foundUser.getName());
        assertEquals("john@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUser(1L);
        });

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser() {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");

        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userController.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);

    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setUserId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }
}
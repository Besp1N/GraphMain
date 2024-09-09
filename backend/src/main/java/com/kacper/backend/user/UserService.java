package com.kacper.backend.user;

import com.kacper.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing users
 *
 * @Author Kacper Karabinowski
 */
@Service
public class UserService
{
    private final UserRepository userRepository;

    /**
     * @param userRepository The repository for managing user operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param id The ID of the user to delete
     * @return The user with the specified ID
     * @throws ResourceNotFoundException if the user with the given ID is not found
     */
    public User deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        userRepository.delete(user);
        return user;
    }

    /**
     * @return List of all users
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(
                    user -> UserResponse.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .createdAt(user.getCreatedAt())
                            .build()
            )
            .collect(Collectors.toList());
    }
}

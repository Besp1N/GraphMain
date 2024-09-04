package com.kacper.backend.user;

import com.kacper.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        userRepository.delete(user);
        return user;
    }

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

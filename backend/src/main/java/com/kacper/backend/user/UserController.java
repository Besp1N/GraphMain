package com.kacper.backend.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing users
 *
 * @Author Kacper Karabinowski
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController
{
    private final UserService userService;

    /**
     * @param userService The service for managing user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return List of all users
     */
    @GetMapping("/")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * @param id The ID of the user to delete
     * @return The user with the specified ID
     */
    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }
}

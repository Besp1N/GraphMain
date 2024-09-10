package com.kacper.backend.user;

import com.kacper.backend.exception.ResourceNotFoundException;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1)
                .name("Sabname")
                .lastName("Sabsur")
                .email("sabname.dsabsur@example.com")
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .build();

        user2 = User.builder()
                .id(2)
                .name("Sabus")
                .lastName("Saber")
                .email("sabus.saber@example.com")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void deleteUserbyid_valid() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User deletedUser = userService.deleteUserById(1);

        assertThat(deletedUser).isEqualTo(user1);
        verify(userRepository).delete(user1);
    }

    @Test
    void deleteUser_userDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // throw exception
        assertThatThrownBy(() -> userService.deleteUserById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).delete(any());
    }

    @Test
    void returnListResponses() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponse> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        //first user
        assertThat(users.getFirst().id()).isEqualTo(user1.getId());
        assertThat(users.getFirst().name()).isEqualTo(user1.getName());
        assertThat(users.get(0).lastName()).isEqualTo(user1.getLastName());
        assertThat(users.get(0).email()).isEqualTo(user1.getEmail());
        assertThat(users.get(0).role()).isEqualTo(user1.getRole());
        assertThat(users.get(0).createdAt()).isEqualTo(user1.getCreatedAt());
        //sescond user
        assertThat(users.get(1).id()).isEqualTo(user2.getId());
        assertThat(users.get(1).name()).isEqualTo(user2.getName());
        assertThat(users.get(1).lastName()).isEqualTo(user2.getLastName());
        assertThat(users.get(1).email()).isEqualTo(user2.getEmail());
        assertThat(users.get(1).role()).isEqualTo(user2.getRole());
        assertThat(users.get(1).createdAt()).isEqualTo(user2.getCreatedAt());
    }

    @Test
    void allUsers_nonExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponse> users = userService.getAllUsers();
        assertThat(users).isEmpty();
    }

    @Test
    void delete_alreadyDeleted() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1)).thenReturn(Optional.empty());
        userService.deleteUserById(1);

        assertThatThrownBy(() -> userService.deleteUserById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void delete_NullId() {
        // throw illegalArgumentException
        assertThatThrownBy(() -> userService.deleteUserById(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).delete(any());
    }

    @Test
    void allUsers_called() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserResponse> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void allUsers_hndleEfficiently() {
        List<User> largeUserList = IntStream.range(0, 1000)
                .mapToObj(i -> User.builder()
                        .id(i)
                        .name("User" + i)
                        .lastName("LastName" + i)
                        .email("user" + i + "@example.com")
                        .role("USER")
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(largeUserList);
        List<UserResponse> users = userService.getAllUsers();

        assertThat(users).hasSize(1000);
        verify(userRepository, times(1)).findAll();
    }


}

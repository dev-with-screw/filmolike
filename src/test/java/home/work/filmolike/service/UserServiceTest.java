package home.work.filmolike.service;

import home.work.filmolike.domain.Role;
import home.work.filmolike.domain.User;
import home.work.filmolike.repository.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("when registrate user with unique username")
    public void addUserSuccessTest() {
        //given
        User user = createNewUser();
        doReturn(null).when(userRepo).findByUsername(user.getUsername());
        doReturn("encodedPassword").when(passwordEncoder).encode(any(String.class));

        //when
        boolean expectedValue = userService.isUserAdded(user);

        //then
        assertTrue(user.isActive(), "User should be active");
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)), "User should get role USER");
        assertEquals("encodedPassword", user.getPassword(), "Password should be encoded");

        assertTrue(expectedValue, "User should be added to db");

        verify(userRepo, times(1)).save(user);
    }

    private User createNewUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        return user;
    }

    @Test
    @DisplayName("when try to registrate user with already existed username")
    public void addUserFailTest() {
        //given
        User actualUser = createNewUser();
        User expectedUser = createNewUser();
        doReturn(expectedUser)
                .when(userRepo).findByUsername(actualUser.getUsername());

        //when
        boolean expectedValue = userService.isUserAdded(actualUser);

        //then
        assertFalse(expectedValue);

        verify(userRepo, times(0)).save(actualUser);
    }


}
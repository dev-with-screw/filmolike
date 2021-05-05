package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import home.work.filmolike.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(value = RegistrationController.class,
//            useDefaultFilters = false,
//            includeFilters = {@ComponentScan.Filter(
//                    type = FilterType.ASSIGNABLE_TYPE,
//                    value = RegistrationController.class
//            )
//            }
//)

//@WebMvcTest(controllers = RegistrationController.class)

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getRegistration_shouldReturnRegistrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(content().string(containsString("Регистрация")));
    }

    @Test
    void addUserSuccess() throws Exception {
        User user = createUser();

//        doReturn(true).when(userService.isUserAdded(user));

        when(userService.isUserAdded(user)).thenReturn(true);

        mockMvc.perform(post("/registration")
                            .param("username", "user")
                            .param("password", "pass")
                            .param("passwordConfirm", "pass")
                            .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вы успешно зарегистрировались")));

    }

    private User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");

        return user;
    }
}

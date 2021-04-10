package home.work.filmolike.controller;

import home.work.filmolike.domain.User;
import home.work.filmolike.service.UserService;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private User user;

    @Test
    void GetRegistration_shouldReturnRegistrationPage() throws Exception {

        this.mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Регистрация")))
                .andExpect(content().string(containsString("Логин")))
                .andExpect(content().string(containsString("Пароль")))
                .andExpect(content().string(containsString("Пароль повторно")));
    }

    @Test
    void Post() throws  Exception {
        //        given(this.userService.addUser(user)).willReturn(true);
//
//        when(this.userService.addUser(user)).thenReturn(true);

    }

}

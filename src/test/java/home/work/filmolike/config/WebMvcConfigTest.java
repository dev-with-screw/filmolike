package home.work.filmolike.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class WebMvcConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /home  - return home.html")
    void GetHome_shouldReturnHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Домашняя страница")));
    }

    @Test
    @DisplayName("GET /login  - return login.html")
    void GetLogin_shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Авторизация")));
    }
}
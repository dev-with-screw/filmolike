package home.work.filmolike.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ZeroEndpointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //don't create
    @Test
    void redirectWhenUnauthenticated() throws Exception {

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/notes"));
    }

    @Test
    @DisplayName("tests working")
    void test() throws Exception{
        mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test page")));
    }
}
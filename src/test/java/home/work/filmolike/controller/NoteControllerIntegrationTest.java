package home.work.filmolike.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/notes-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/notes-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@WithUserDetails(value = "u")

public class NoteControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /notes  - success")
    void getNotes_ReturnPageWithTwoNotes() throws Exception{
        mockMvc.perform(get("/notes"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(view().name("notes/notes"))
                .andExpect(content().string(containsString("Добро пожаловать, u!")))
                .andExpect(model().attribute("notes", hasSize(2)))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("Властелин колец - крутой фильм"))
                        )
                )))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("title", is("Хочу посмотреть Господин Никто"))
                        )
                )));
    }

    @Test
    @DisplayName("POST /notes - success")
    public void saveNoteTest() throws Exception {
        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "New Note")
                .param("watched", "false")
                .param("estimate", "NOT_ESTIMATE")
                .with(csrf())
        )
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }
}

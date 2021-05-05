package home.work.filmolike.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private NoteService noteServiceMock;

    @Test
    @DisplayName("POST /notes - success")
    public void saveNoteTest() throws Exception {
//        Note noteToPost = new Note("New Note", false, Estimate.NOT_ESTIMATE);
//        Note noteToReturn = new Note("New Note", false, Estimate.NOT_ESTIMATE);

//        doReturn(noteToReturn).when(noteServiceMock).save(any());

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


    // don't work.
    // Should test scenario when bindingResult.hasErrors()
    @Test
    @DisplayName("POST /notes - bad params")
    public void saveNoteWithBadParamsTest() throws Exception {
//        Note noteToPost = new Note("New Note", false, Estimate.NOT_ESTIMATE);
//        Note noteToReturn = new Note("New Note", false, Estimate.NOT_ESTIMATE);

//        doReturn(noteToReturn).when(noteServiceMock).save(any());

        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", createStringWithLength(49))
                .param("watched", "false")
                .param("estimate", "NOT_ESTIMATE")
                .with(csrf())
        )
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    private static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i< length; i++) {
            builder.append("a");
        }

        return builder.toString();
    }
}

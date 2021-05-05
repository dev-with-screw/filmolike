package home.work.filmolike.controller;

import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.Note;
import home.work.filmolike.dto.NoteDto;
import home.work.filmolike.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(NoteController.class)
public class NoteWebMvcTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private NoteService noteService;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(springSecurity())
                .build();
    }

    @Test
    void test() throws  Exception {
//       NoteDto noteDto = new NoteDto(1L, "title1", false, Estimate.NOT_ESTIMATE);
       when(noteService.get(any())).thenReturn(createNote(1L));

       mockMvc.perform(MockMvcRequestBuilders.get("/notes/1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
    }

    private Note createNote(Long id) {
        Note note = new Note();

        note.setId(id);
        note.setTitle("title");
        note.setWatched(false);
        note.setEstimate(Estimate.NOT_ESTIMATE);

        return note;
    }

    private List<NoteDto> createList() {
        List<NoteDto> notes = new ArrayList<>();

//        notes.add(new NoteDto(1L, "title1", false, Estimate.NOT_ESTIMATE));
//        notes.add(new NoteDto(2L, "title1", false, Estimate.NOT_ESTIMATE));

        return notes;
    }
}

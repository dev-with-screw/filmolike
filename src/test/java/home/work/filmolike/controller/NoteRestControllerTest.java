package home.work.filmolike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.Role;
import home.work.filmolike.domain.User;
import home.work.filmolike.dto.NoteDto;
import home.work.filmolike.service.NoteService;
import home.work.filmolike.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteRestController.class)
public class NoteRestControllerTest {
    @MockBean
    private NoteService noteService;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

//    @BeforeAll
//    private static void prepare() {
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                .thenReturn(currentUser());
//    }

    private static User currentUser() {
        User user = new User();

        user.setUsername("testuser");
        user.setPassword("secret");
        user.setActive(true);

        user.setRoles(Collections.singleton(Role.USER));

        return user;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("GET /rest/notes - success")
    @WithMockUser()
    void testGetNotesSuccess() throws Exception {
        List<NoteDto> notesDto = new ArrayList<NoteDto>() {{
            add(createNoteDtoWithId(1L));
            add(createNoteDtoWithId(2L));
        }};

        when(noteService.findByUser(any())).thenReturn(notesDto);

        mockMvc.perform(get("/rest/notes"))
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/notes"))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("title")))
                .andExpect(jsonPath("$[0].watched", is("false")))
                .andExpect(jsonPath("$[0].estimate", is("NOT_ESTIMATE")))
                .andExpect(jsonPath("$[0].changed", is("2021-05-04 19:10:20")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("title")))
                .andExpect(jsonPath("$[1].watched", is("false")))
                .andExpect(jsonPath("$[1].estimate", is("NOT_ESTIMATE")))
                .andExpect(jsonPath("$[1].changed", is("2021-05-04 19:10:20")));

        verify(noteService, times(1)).findByUser(any());
    }


    @Test
    @DisplayName("GET /rest/note/1 - success")
    @WithMockUser()

    void testGetNoteById() throws Exception {
        Note note = createNoteWithId(1L);

        doReturn(Optional.of(note)).when(noteService).findById(any(Long.class));

        mockMvc.perform(get("/rest/note/{id}", 1L))
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/note/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.watched", is(false)))
                .andExpect(jsonPath("$.estimate", is("NOT_ESTIMATE")))
                .andExpect(jsonPath("$.changed", is("2021-05-04 19:10:20")));

    }

    @Test
    @DisplayName("GET /rest/note/1 - Not Found")
    @WithMockUser
    void testGetNoteByIdNotFound() throws Exception {

        doReturn(Optional.empty()).when(noteService).findById(any(Long.class));

        mockMvc.perform(get("/rest/note/{id}", 1L))
                .andDo(print())

                // Validate the response code
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("POST /rest/note - success")
    @WithMockUser("u")
    void saveWithCorrectData() throws Exception {


        NoteDto noteToPost = new NoteDto("correct note", "false", "NOT_ESTIMATE");


        doReturn(createNoteWithId(1L)).when(noteService).save(any(Note.class), any());

        mockMvc.perform(post("/rest/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteToPost))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
        )
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/note/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.watched", is("false")))
                .andExpect(jsonPath("$.estimate", is("NOT_ESTIMATE")))
                .andExpect(jsonPath("$.changed", is("2021-05-04 19:10:20")));

        verify(noteService, times(1)).save(any(Note.class), any());

    }

    @Test
    @DisplayName("POST /rest/note - invalid input")
    void savewithIncorrectData() throws Exception {
        NoteDto noteToPost = new NoteDto("", "", "");

        mockMvc.perform(post("/rest/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteToPost))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
        )
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("title : Shouldn't be empty")))
                .andExpect(jsonPath("$.errors", hasItem("watched : Should be true or false")))
                .andExpect(jsonPath("$.errors", hasItem("estimate : Should contain value from enum Estimate")));

        verify(noteService, times(0)).save(any(Note.class), any());
    }


    @Test
    @DisplayName("PUT /rest/note/1 - success")
    void testUpdateNote() throws Exception {
        Note noteToPut = new Note("title", false, Estimate.NOT_ESTIMATE);

        doReturn(Optional.of(createNoteWithId(1L))).when(noteService).findById(1L);
        doReturn(createNoteWithId(1L)).when(noteService).save(any(), any());

        mockMvc.perform(put("/rest/note/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteToPut))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
        )
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/note/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.watched", is(false)))
                .andExpect(jsonPath("$.estimate", is("NOT_ESTIMATE")))
                .andExpect(jsonPath("$.changed", is("2021-05-04 19:10:20")));

        verify(noteService, times(1)).findById(any(Long.class));
        verify(noteService, times(1)).save(any(Note.class), any());


    }

    @Test
    @DisplayName("PUT /rest/note/1 - Not Found")
    void testUpdateNoteNotFound() throws Exception {
        Note noteToPut = new Note("title", false, Estimate.NOT_ESTIMATE);
        doReturn(Optional.empty()).when(noteService).findById(1L);

        mockMvc.perform(put("/rest/note/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteToPut))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
        )
                .andDo(print())

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }


    private static NoteDto createNoteDtoWithId(Long id) {
        NoteDto note = new NoteDto("title", "false", "NOT_ESTIMATE");

        note.setId(id);
        note.setChanged(LocalDateTime.of(2021, 05, 04, 19, 10, 20, 100));

        return note;
    }

    private static Note createNoteWithId(Long id) {
        Note note = new Note("title", false, Estimate.NOT_ESTIMATE);

        note.setId(id);
        note.setChanged(LocalDateTime.of(2021, 05, 04, 19, 10, 20, 100));

        return note;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.dto.NoteConverter;
import home.work.filmolike.dto.NoteDto;
import home.work.filmolike.service.NoteService;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RestController
@Validated
@RequestMapping("/rest")
public class NoteRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteRestController.class);

    private final NoteService noteService;

    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    public ResponseEntity<List<NoteDto>> getNotes(
            @AuthenticationPrincipal User user
    ) {
        try {
            return ResponseEntity.ok()
                    .location((new URI("/rest/notes")))
                    .body(noteService.findByUser(user));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/note/{id}")
    public ResponseEntity<?> getNoteById(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user)
    {
        Optional<Note> noteOptional = noteService.findById(id);

        if (!noteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Note foundNote = noteOptional.get();

        if (hasNotRights(foundNote, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет прав для просмотра данных");
        }

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/rest/note/" + foundNote.getId()))
                    .body(NoteConverter.toDto(foundNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/note")
    public ResponseEntity<NoteDto> createNote(
            @Valid @RequestBody NoteDto noteDto,
            @AuthenticationPrincipal User user
    ) {
        LOGGER.info("Received note: title: {}, watched: {}, estimate: {}", noteDto.getTitle(), noteDto.getWatched(), noteDto.getEstimate());

        Note savedNote = noteService.save(NoteConverter.toDomain(noteDto), user);

        LOGGER.info("Saved note: id: {}, title: {}, watched: {}, estimate: {}", savedNote.getId(), savedNote.getTitle(), savedNote.isWatched(), savedNote.getEstimate());

        try {
            return ResponseEntity
                    .created(new URI("/rest/note/" + savedNote.getId()))
                    .body(NoteConverter.toDto(savedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteDto noteDto,
            @AuthenticationPrincipal User user
    ) {
        Optional<Note> noteOptional = noteService.findById(id);

        if (!noteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Note foundNote = noteOptional.get();

        if (hasNotRights(foundNote, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет прав для изменения данных");
        }

        Note updatingNote = NoteConverter.toDomain(noteDto);
        updatingNote.setId(id);

        Note updatedNote = noteService.save(updatingNote, user);

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/rest/note/" + updatedNote.getId()))
                    .body(NoteConverter.toDto(updatedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity<?> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal User user)
    {
        Optional<Note> noteOptional = noteService.findById(id);

        if (!noteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (hasNotRights(noteOptional.get(), user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет прав для удаления данных");
        }

        noteService.delete(id);
        return ResponseEntity.ok().build();
    }

    private boolean hasNotRights(Note note, User user) {
        return !user.equals(note.getUser());
    }
}

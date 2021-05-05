package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.dto.NoteDto;
import home.work.filmolike.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class NoteRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteRestController.class);

    private final NoteService noteService;

    @Autowired
    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        NoteDto noteDto = noteService.findByIdDto(id);

        if (noteDto.equals(NoteDto.NULL_NOTE)) {
            return ResponseEntity.notFound().build();
        }

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/rest/note/" + noteDto.getId()))
                    .body(noteDto);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

//        return noteService.findById(id)
//                .map(note -> {
//                    try {
//                        NoteDto noteDto = NoteDto.toDto(note);
//
//                        return ResponseEntity
//                                .ok()
//                                .location(new URI("/rest/note/" + noteDto.getId()))
//                                .body(noteDto);
//                    } catch (URISyntaxException e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                    }
//                })
//                .orElse(ResponseEntity.notFound().build());
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

    @PostMapping("/note")
    public ResponseEntity<NoteDto> createNote(
            @RequestBody Note note,
            @AuthenticationPrincipal User user
    ) {
        LOGGER.info("Received note: title: {}, watched: {}, estimate: {}", note.getTitle(), note.isWatched(), note.getEstimate());

        Note savedNote = noteService.save(note, user);

        try {
            return ResponseEntity
                    .created(new URI("/rest/note/" + savedNote.getId()))
                    .body(NoteDto.toDto(savedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<NoteDto> updateNote(
            @PathVariable Long id,
            @RequestBody Note note,
            @AuthenticationPrincipal User user
    ) {
        Optional<Note> existingNote = noteService.findById(id);

        if (!existingNote.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        note.setId(id);

        Note updatedNote = noteService.save(note, user);

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/rest/note/" + note.getId()))
                    .body(NoteDto.toDto(updatedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity deleteNote(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.ok().build();
    }
}

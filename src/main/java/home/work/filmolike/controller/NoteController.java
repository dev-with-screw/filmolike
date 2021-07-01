package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.exception.ForbiddenException;
import home.work.filmolike.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private int pageNum = 1;

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService service) {
        this.noteService = service;
    }

    @GetMapping
    public String forward(
            @AuthenticationPrincipal User user,
            Model model)
    {
        LOGGER.info("principal user_id: {}", user.getId());
        return showNotes(user, model, pageNum, "changed", "desc");
    }

    @GetMapping("/page/{pageNum}")
    public String showNotes(
            @AuthenticationPrincipal User user,
            Model model,
            @PathVariable("pageNum") int pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir)
    {
        LOGGER.debug("principal user_id: {}", user.getId());

        this.pageNum = pageNum;

        Page<Note> page = noteService.findSeveral(user, pageNum, sortField, sortDir);

        List<Note> notes = page.getContent();

        // видел реализацию, где передают атрибутом page, а уже в template'е разбирают на атрибуты
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("notes", notes);

        return "notes/notes";
    }

    @GetMapping("/{id}")
    public String showNoteDetails(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Note> noteOptional = noteService.findById(id);

        if(noteOptional.isPresent()) {
            Note note = noteOptional.get();
            checkHasRights(note, user);
            model.addAttribute("note", note);
            return "notes/note";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/new")
    public String showNewForm(Note note) {
        return "notes/new_note";
    }

    @PostMapping
    public String saveNote(
            @AuthenticationPrincipal User user,
            @Valid Note note,
            BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) {
            return "/notes/new_note";
        }

        noteService.save(note, user);

        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "id") Long id,
            Model model)
    {
        Optional<Note> noteOptional = noteService.findById(id);

        if(noteOptional.isPresent()) {
            Note note = noteOptional.get();
            checkHasRights(note, user);
            model.addAttribute("note", note);
            return "notes/edit_note";
        } else {
            return "error/404";
        }
    }

    @PutMapping("/{id}")
    public String updateNote(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id,
            @Valid Note note,
            BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()) {
            return "notes/edit_note";
        }
        Optional<Note> noteOptional = noteService.findById(id);

        if(noteOptional.isPresent()) {
            Note existingNote = noteOptional.get();
            checkHasRights(existingNote, user);
            note.setId(id);
            noteService.save(note, user);
            return "redirect:/notes";
        } else {
            return "error/404";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteNote(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id)
    {
        Optional<Note> noteOptional = noteService.findById(id);

        if(noteOptional.isPresent()) {
            checkHasRights(noteOptional.get(), user);
            noteService.delete(id);
            return "redirect:/notes";
        } else {
            return "error/404";
        }
    }

    private void checkHasRights(Note note, User user) {
        if (!user.equals(note.getUser())) {
            throw new ForbiddenException();
        }
    }
}

package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.exception.ForbiddenException;
import home.work.filmolike.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private int pageNum = 1;

    private final NoteService service;

    @Autowired
    public NoteController(NoteService service) {
        this.service = service;
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

        Page<Note> page = service.findSeveral(user, pageNum, sortField, sortDir);

        List<Note> notes = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("notes", notes);

        return "notes/notes";
    }

    @GetMapping("/newpage/page/{pageNum}")
    public String showNotesWithNewPagination(
            @AuthenticationPrincipal User user,
            Model model,
            @PathVariable("pageNum") int pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @PageableDefault(sort = { "changed" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        this.pageNum = pageNum;

        Page<Note> page = service.findSeveral(user, pageNum, sortField, sortDir);

        List<Note> notes = page.getContent();

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
    public String showDetails(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long id,
            Model model) {

        Note note = service.get(id);

        if (!user.equals(note.getUser())) {
            throw new ForbiddenException();
        }

        model.addAttribute("note", note);

        return "notes/note";
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

        note.setUser(user);
        service.save(note);

        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "id") Long id,
            Model model)
    {
        Note note = service.get(id);

        if (!user.equals(note.getUser())) {
            throw new ForbiddenException();
        }

        model.addAttribute("note", note);
        return "notes/edit_note";
    }

    @PutMapping("/{id}")
    public String updateNote(@AuthenticationPrincipal User user,
                             @PathVariable("id") Long id,
                             @ModelAttribute("note") Note note,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "notes/edit_note";
        }

        note.setUser(user);
        service.save(note);

        return "redirect:/notes";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/notes";
    }

    @GetMapping("/403")
    public String get403() {
        throw new ForbiddenException();
    }


}

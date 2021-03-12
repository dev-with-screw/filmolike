package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private int pageNum = 1;

    private final NoteService service;

    @Autowired
    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping
    public String forward(Model model) {
        return showNotes(model, pageNum, "id", "asc");
    }

    @GetMapping("/page/{pageNum}")
    public String showNotes(Model model,
                               @PathVariable("pageNum") int pageNum,
                               @Param("sortField") String sortField,
                               @Param("sortDir") String sortDir) {
        this.pageNum = pageNum;

        Page<Note> page = service.findSeveral(pageNum, sortField, sortDir);

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
    public String showDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("note", service.get(id));
        return "notes/note";
    }

    @GetMapping("/new")
    public String showNewForm(Note note) {
        return "notes/new_note";
    }

    @PostMapping
    public String saveNote(@Valid Note note, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/notes/new_note";
        }
        Long noteId = service.save(note);
        return String.format("redirect:/notes/%d", noteId.intValue());
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        service.get(id);
        model.addAttribute("note", service.get(id));
        return "notes/edit_note";
    }

    @PutMapping("/{id}")
    public String updateNote(@PathVariable("id") Long id,
                         @ModelAttribute("note") Note note,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "notes/edit_note";
        }
        service.save(note);
        return String.format("redirect:/notes/%d", id.intValue());
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/notes";
    }
}

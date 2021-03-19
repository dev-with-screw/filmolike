package home.work.filmolike.controller;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class UserNotesController {
    private UserService userService;

    @Autowired
    public UserNotesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userNotes")
    public String userNotes(
            @AuthenticationPrincipal User currentUser,
            Model model) {
        Set<Note> notes = currentUser.getNotes();

        model.addAttribute("notes", notes);

        return "notes";
    }
}

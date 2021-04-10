package home.work.filmolike.service;

import home.work.filmolike.controller.NoteController;
import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.repository.NoteRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class NoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    private int pageSize = 10;

    private final NoteRepo repo;

    @Autowired
    public NoteService(NoteRepo repo) {
        this.repo = repo;
    }

    public Page<Note> findSeveral(User user, int pageNum, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        return repo.findByUser(user, pageable);
    }

    public void save(Note note) {
        note.setChanged(LocalDateTime.now());
        LOGGER.info("Save note to db {}", note.toString());
        repo.save(note);
    }

    public Note get(long id) {
        return repo.getOne(id);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }
}

package home.work.filmolike.service;

import home.work.filmolike.domain.Note;
import home.work.filmolike.repository.NoteRepo;
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
    private int pageSize = 10;

    private final NoteRepo repo;

    @Autowired
    public NoteService(NoteRepo repo) {
        this.repo = repo;
    }

    public Page<Note> findSeveral(int pageNum, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        return repo.findAll(pageable);
    }

    public void save(Note note) {
        note.setChanged(LocalDateTime.now());
        repo.save(note);
    }



    public Note get(long id) {
        return repo.getOne(id);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}

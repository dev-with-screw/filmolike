package home.work.filmolike.service;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import home.work.filmolike.dto.NoteDto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    private int pageSize = 10;

    private final NoteRepo noteRepo;

    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public List<Note> findAll() {
        return noteRepo.findAll();
    }

    public Page<Note> findSeveral(User user, int pageNum, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        return noteRepo.findByUser(user, pageable);
    }

    public Note save(Note note) {
        return noteRepo.save(note);
    }

    public Note save(Note note, User user) {
        note.setUser(user);
        note.setChanged(LocalDateTime.now());

        return noteRepo.save(note);
    }

    public Note get(long id) {
        return noteRepo.getOne(id);
    }

    public Optional<Note> findById(Long id) {
        return noteRepo.findById(id);
    }



    public void delete(long id) {
        noteRepo.deleteById(id);
    }

    public NoteDto findByIdDto(Long id) {
        Optional<Note> noteFromDb = noteRepo.findById(id);

        return noteFromDb.map(NoteDto::toDto).orElse(NoteDto.NULL_NOTE);
    }

    public List<NoteDto> findAllDto() {
        return noteRepo.findAll().stream().map(NoteDto::toDto).collect(Collectors.toList());
    }

    public List<NoteDto> findByUser(User user) {
        return noteRepo.findByUser(user).stream().map(NoteDto::toDto).collect(Collectors.toList());
    }
}

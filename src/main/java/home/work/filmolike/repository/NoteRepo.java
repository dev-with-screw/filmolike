package home.work.filmolike.repository;

import home.work.filmolike.domain.Note;
import home.work.filmolike.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    Page<Note> findByUser(User user, Pageable pageable);
    List<Note> findByUser(User user);
}

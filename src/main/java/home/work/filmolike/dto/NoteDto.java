package home.work.filmolike.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.Note;

import java.time.LocalDateTime;

public class NoteDto {
    private Long id;
    private String title;
    private boolean watched;
    private Estimate estimate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changed;

    public NoteDto() {
    }

    public NoteDto(String title, boolean watched, Estimate estimate) {
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
    }

    public NoteDto(Long id, String title, boolean watched, Estimate estimate, LocalDateTime changed) {
        this.id = id;
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
        this.changed = changed;
    }

    public static final NoteDto NULL_NOTE = new NoteDto();

    public static NoteDto toDto(Note note) {
        return new NoteDto() {{
            setId(note.getId());
            setTitle(note.getTitle());
            setWatched(note.isWatched());
            setEstimate(note.getEstimate());
            setChanged(note.getChanged());
        }};
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }
}

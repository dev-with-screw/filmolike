package home.work.filmolike.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.Note;
import home.work.filmolike.validation.EnumValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

public class NoteDto {
    private Long id;

    @NotBlank(message = "Shouldn't be empty")
    @Length(max=50, message = "Should contain no more than 50 characters")
    private String title;

    @NotNull
    @Pattern(regexp = "^true$|^false$", message = "Should be true or false")
    private String watched;

    @EnumValidator(enumClazz = Estimate.class, message = "Should contain value from enum Estimate")
    private String estimate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changed;

    public NoteDto() {
    }

    public NoteDto(@NotBlank(message = "Shouldn't be empty") @Length(max = 50, message = "Should contain no more than 50 characters") String title, @NotNull @Pattern(regexp = "^true$|^false$", message = "Should be true or false") String watched, String estimate) {
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
    }

    public NoteDto(Long id, @NotBlank(message = "Shouldn't be empty") @Length(max = 50, message = "Should contain no more than 50 characters") String title, @NotNull @Pattern(regexp = "^true$|^false$", message = "Should be true or false") String watched, String estimate, LocalDateTime changed) {
        this.id = id;
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
        this.changed = changed;
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

    public String getWatched() {
        return watched;
    }

    public void setWatched(String watched) {
        this.watched = watched;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteDto)) return false;
        NoteDto noteDto = (NoteDto) o;
        return id.equals(noteDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package home.work.filmolike.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Поле не должно быть пустым")
    @Length(max=50, message = "Должно содержать не более 50 символов")
    private String title;
    private boolean watched;
    @Enumerated(EnumType.STRING)
    private Estimate estimate;
    private LocalDateTime changed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "note")
    private List<Commentary> commentary;
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    public Note() {
    }

    public Note(@NotBlank(message = "Поле не должно быть пустым") @Length(max = 50, message = "Должно содержать не более 50 символов") String title, boolean watched, Estimate estimate) {
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Commentary> getCommentary() {
        return commentary;
    }

    public void setCommentary(List<Commentary> commentary) {
        this.commentary = commentary;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", watched=" + watched +
                ", estimate=" + estimate +
                ", changed=" + changed +
                ", user_id=" + user.getId() +
                '}';
    }
}

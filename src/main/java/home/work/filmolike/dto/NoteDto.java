package home.work.filmolike.dto;

import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.User;


public class NotesDto {
    private Long id;
    private String title;
    private boolean watched;
    private Estimate estimate;

    public NotesDto() {
    }

    public NotesDto(Long id, String title, boolean watched, Estimate estimate) {
        this.id = id;
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
}

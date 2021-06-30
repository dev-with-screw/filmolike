package home.work.filmolike.dto;

import home.work.filmolike.domain.Estimate;
import home.work.filmolike.domain.Note;

public class NoteConverter {
    public static final NoteDto NULL_NOTE = new NoteDto();

    public static NoteDto toDto(Note note) {
        NoteDto noteDto = new NoteDto();

        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setWatched(note.isWatched() ? "true" : "false");
        noteDto.setEstimate(note.getEstimate().toString());
        noteDto.setChanged(note.getChanged());

        return noteDto;
    }

    public static Note toDomain(NoteDto noteDto) {
        Note note = new Note();

        note.setTitle(noteDto.getTitle());
        note.setWatched(noteDto.getWatched().equals("true"));
        note.setEstimate(Estimate.valueOf(noteDto.getEstimate()));

        return note;
    }
}

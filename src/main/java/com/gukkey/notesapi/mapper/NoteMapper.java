package com.gukkey.notesapi.mapper;

import org.springframework.stereotype.Component;

import com.gukkey.notesapi.domain.Note;
import com.gukkey.notesapi.model.NoteDTO;

@Component
public class NoteMapper {
    public Note toNote(NoteDTO noteDTO) {
        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setTag(noteDTO.getTag());
        note.setBody(noteDTO.getBody());
        return note;
    }
}

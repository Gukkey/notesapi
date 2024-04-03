package com.gukkey.notesapi.mapper;

import com.gukkey.notesapi.domain.Note;
import com.gukkey.notesapi.model.NoteDTO;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
  public Note toNote(NoteDTO noteDTO) {
    Note note = new Note();
    note.setUpdatedAt(noteDTO.getUpdatedAt());
    note.setTitle(noteDTO.getTitle());
    note.setTags(noteDTO.getTags());
    note.setBody(noteDTO.getBody());
    return note;
  }
}

package com.gukkey.notesapi.service;

import org.springframework.http.ResponseEntity;

import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;

public interface NoteService {
    ResponseEntity<ListResponse> getAllNotes();
    ResponseEntity<Response> getNoteById(Long id);
    ResponseEntity<Response> addNote(NoteDTO noteDTO);
    ResponseEntity<Response> editNote(Long id, NoteDTO noteDTO);
    ResponseEntity<Response> deleteNoteById(Long id);
    ResponseEntity<ListResponse> findNotesByFilter(String sort, String by, NoteDTO noteDTO);
}

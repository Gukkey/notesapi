package com.gukkey.notesapi.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gukkey.notesapi.domain.Note;
import com.gukkey.notesapi.mapper.NoteMapper;
import com.gukkey.notesapi.repository.NoteRepository;
import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

        @Mock
        private NoteRepository noteRepository;

        @Mock
        private NoteMapper noteMapper;

        @InjectMocks
        private NoteServiceImpl noteService;

        private List<Note> notes;

        private NoteDTO noteDTO;
        private Note note;


        // Example ids
        UUID exampleId1 = UUID.randomUUID();
        UUID exampleId2 = UUID.randomUUID();
        UUID exampleId3 = UUID.randomUUID();


        @BeforeEach
        void setUp() {
                Note note1 = Note.builder().id(exampleId1).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                                .title("Books for him").body("1. Harry Potter set, 2. Dune").tag("List").build();
                Note note2 = Note.builder().id(exampleId2).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                                .title("Interview").body("Interview at Greenstich @ Monday 3 pm")
                                .tag("Remainder")
                                .build();
                Note note3 = Note.builder().id(exampleId3).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                                .title("Movies").body("1. Bramayugam").tag("Watch List")
                                .build();
                notes = Arrays.asList(note1, note2, note3);

                noteDTO = NoteDTO.builder().title("Books for her").body("1. Harry Potter set, 2. Dune").tag("List")
                                .build();

                note = Note.builder().title("Books for her").createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now()).title("Example Title")
                                .tag("Example Title").body("Example Body").build();

                NoteDTO.builder().title("Books for her")
                                .body("1. Harry Potter set, 2. Dune, 3. Palace of illusions").tag("List").build();

        }

        // Test code for get all notes

        @Test
        void getAllNotesShouldReturnNotesWhenFound() {
                // Arrange
                when(noteRepository.findAll()).thenReturn(notes);

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.getAllNotes();

                // Assert
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                ListResponse listResponse = responseEntity.getBody();
                assertEquals(notes.size(), listResponse.getNotes().size());
        }

        // test code for add note

        @Test
        void addNoteShouldCreateNoteWhenNoteDTOIsValid() {
                // Arrange
                when(noteMapper.toNote(any(NoteDTO.class))).thenReturn(note);
                when(noteRepository.save(any(Note.class))).thenReturn(note);

                // Act
                ResponseEntity<Response> responseEntity = noteService.addNote(noteDTO);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                Response responseBody = responseEntity.getBody();
                assertNotNull(responseBody);
                assertEquals("Note has been created", responseBody.getMessage());
                assertEquals(note.getTitle(), responseBody.getTitle());
                assertEquals(note.getBody(), responseBody.getBody());
                assertEquals(note.getTag(), responseBody.getTag());
        }

        // test code for edit note

        @Test
        void editNoteShouldEditNoteWhenNoteDTOIsValid() {
                // Arrange
                Note existingNote = Note.builder().id(exampleId1).createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .title("Original Title").body("Original Body").tag("Original Tag").build();

                when(noteRepository.findById(exampleId1)).thenReturn(Optional.of(existingNote));

                NoteDTO updatedNoteDTO = NoteDTO.builder().title("Updated Title").body("Updated Body")
                                .tag("Updated Tag")
                                .build();

                // Act
                ResponseEntity<Response> responseEntity = noteService.editNote(exampleId1, updatedNoteDTO);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                Response responseBody = responseEntity.getBody();
                assertNotNull(responseBody);
                assertEquals("Note has been updated", responseBody.getMessage());
                assertEquals(updatedNoteDTO.getTitle(), responseBody.getTitle());
                assertEquals(updatedNoteDTO.getBody(), responseBody.getBody());
                assertEquals(updatedNoteDTO.getTag(), responseBody.getTag());
        }

        // test code for delete by id

        @Test
        void deleteNotesShouldDeleteIfIdIsValid() {
                // Arrange
                Optional<Note> optionalNote = Optional.of(note);
                when(noteRepository.findById(exampleId1)).thenReturn(optionalNote);
                doNothing().when(noteRepository).deleteById(exampleId1);

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(exampleId1);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test 
        void deleteNoteShouldThrowBadRequestIfIdIsNull() {
                // Arrange
                UUID nullId = null;

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(nullId);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }


        @Test
        void deleteNoteShouldThrowNotFoundIfNoteIsNotFound() {
                // Arrange
                Optional<Note> optionalNote = Optional.empty();
                when(noteRepository.findById(exampleId1)).thenReturn(optionalNote);

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(exampleId1);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }
}
package com.gukkey.notesapi.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

        @BeforeEach
        void setUp() {
                Note note1 = Note.builder().id(1L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                                .title("Books for him").body("1. Harry Potter set, 2. Dune").tag("List").build();
                Note note2 = Note.builder().id(2L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                                .title("Interview").body("Interview at Greenstich @ Monday 3 pm")
                                .tag("Remainder")
                                .build();
                Note note3 = Note.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
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

        @Test
        void addNoteShouldThrowBadRequestWhenNoteDTOBodyIsEmpty() {
                var emptyBodyNoteDTO = NoteDTO.builder().tag("tag").title("title").body("").build();
                var emptyBodyNote = Note.builder().id(4L).createdAt(LocalDateTime.now()).createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .tag("tag").title("title").body("").build();

                // Arrange
                when(noteMapper.toNote(any(NoteDTO.class))).thenReturn(emptyBodyNote);

                // Act
                ResponseEntity<Response> responseEntity = noteService.addNote(emptyBodyNoteDTO);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        // test code for edit note

        @Test
        void editNoteShouldEditNoteWhenNoteDTOIsValid() {
                // Arrange
                Long noteId = 1L; // Assume this is the ID of the note to be edited

                Note existingNote = Note.builder().id(noteId).createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .title("Original Title").body("Original Body").tag("Original Tag").build();

                when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
                when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

                NoteDTO updatedNoteDTO = NoteDTO.builder().title("Updated Title").body("Updated Body")
                                .tag("Updated Tag")
                                .build();

                // Act
                ResponseEntity<Response> responseEntity = noteService.editNote(noteId, updatedNoteDTO);

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

        @Test
        void editNoteShouldThrowBadRequestWhenBodyIsEmpty() {
                // Arrange
                Long noteId = 1L; // Assume this is the ID of the note to be edited
                NoteDTO updatedNoteDTO = NoteDTO.builder().title("Updated Title").body("").tag("Updated Tag").build();

                // Act
                ResponseEntity<Response> responseEntity = noteService.editNote(noteId, updatedNoteDTO);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
                Response responseBody = responseEntity.getBody();
                assertNotNull(responseBody);
                assertEquals("Body cannot be empty", responseBody.getMessage());
        }

        // test code for delete by id

        @Test
        void deleteNotesShouldDeleteIfIdIsValid() {
                // Arrange
                Long noteId = 1L;
                Optional<Note> optionalNote = Optional.of(note);
                when(noteRepository.findById(noteId)).thenReturn(optionalNote);
                doNothing().when(noteRepository).deleteById(noteId);

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(noteId);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test 
        void deleteNoteShouldThrowBadRequestIfIdIsNull() {
                // Arrange
                Long noteId = null;

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(noteId);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }


        @Test
        void deleteNoteShouldThrowNotFoundIfNoteIsNotFound() {
                // Arrange
                Long noteId = 1L;
                Optional<Note> optionalNote = Optional.empty();
                when(noteRepository.findById(noteId)).thenReturn(optionalNote);

                // Act
                ResponseEntity<Response> responseEntity = noteService.deleteNoteById(noteId);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        // test code for find by tag

        @Test
        void getNotesByTagShouldReturnNotesWhenFound() {
                // Arrange
                String tag = "List";
                when(noteRepository.findByTag(tag)).thenReturn(notes);

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTag(tag);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                ListResponse listResponse = responseEntity.getBody();
                assertNotNull(listResponse);
                assertEquals(notes.size(), listResponse.getNotes().size());
        }

        @Test
        void getNotesByTagShouldReturnEmptyListWhenNotFound() {
                // Arrange
                String tag = "Unknown";
                when(noteRepository.findByTag(anyString())).thenReturn(List.of());

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTag(tag);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void getNotesByTagShouldThrowBadRequestWhenTagIsNull() {
                // Arrange
                String tag = null;

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTag(tag);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        // test code for find by title

        @Test
        void getNotesByTitleShouldReturnNotesWhenFound() {
                // Arrange
                String title = "Books for him";
                when(noteRepository.findByTitle(anyString())).thenReturn(notes);

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTitle(title);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                ListResponse listResponse = responseEntity.getBody();
                assertNotNull(listResponse);
                assertEquals(notes.size(), listResponse.getNotes().size());
        }

        @Test
        void getNotesByTitleShouldReturnEmptyListWhenNotFound() {
                // Arrange
                String title = "Unknown";
                when(noteRepository.findByTitle(anyString())).thenReturn(List.of());

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTitle(title);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void getNotesByTitleShouldThrowBadRequestWhenTitleIsNull() {
                // Arrange
                String title = null;

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTitle(title);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        // test code for find by tag and title

        @Test
        void getNotesByTagAndTitleShouldReturnNotesWhenFound() {
                // Arrange
                when(noteRepository.findByTagAndTitle(anyString(), anyString())).thenReturn(notes);

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTagAndTitle(anyString(), anyString());

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                ListResponse listResponse = responseEntity.getBody();
                assertNotNull(listResponse);
                assertEquals(notes.size(), listResponse.getNotes().size());
        }

        @Test
        void getNotesByTagAndTitleShouldReturnEmptyListWhenNotFound() {
                // Arrange
                when(noteRepository.findByTagAndTitle(anyString(), anyString())).thenReturn(List.of());

                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTagAndTitle(anyString(), anyString());

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @ParameterizedTest
        @CsvSource({ ", title", "tag, ", ", " })
        void getNotesByTagAndTitleShouldThrowBadRequestWhenTagOrTitleIsNull(String tag, String title) {
                // Act
                ResponseEntity<ListResponse> responseEntity = noteService.findNotesByTagAndTitle(tag, title);

                // Assert
                assertNotNull(responseEntity);
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
}

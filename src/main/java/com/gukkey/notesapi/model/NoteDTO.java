package com.gukkey.notesapi.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NoteDTO {
    private String tag;
    
    private String title;

    private String body;

    private LocalDateTime updatedAt;

}

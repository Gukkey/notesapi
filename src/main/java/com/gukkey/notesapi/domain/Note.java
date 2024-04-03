package com.gukkey.notesapi.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "notes")
@Table(name = "notes")
public class Note {
    @Id
    @Column(name = "notes_id", nullable = false)
    @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", type = org.hibernate.id.uuid.UuidGenerator.class)
    private UUID id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Type(ListArrayType.class)
    @Column(name = "tags", columnDefinition = "varchar[]")
    private List<String> tags;

    @Column(name = "title")
    private String title;

    @Column(name = "body", nullable = false)
    private String body;   
}

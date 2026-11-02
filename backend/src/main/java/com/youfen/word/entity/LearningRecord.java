package com.youfen.word.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "learning_record")
public class LearningRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long wordId;
    /** known / unknown */
    private String result;
    private LocalDate learnDate;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.learnDate == null) this.learnDate = LocalDate.now();
    }
}

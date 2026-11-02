package com.youfen.word.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 错题本：标记为不认识的单词自动进入 */
@Data
@Entity
@Table(name = "review_item", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "word_id"}))
public class ReviewItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long wordId;
    private Integer wrongCount;
    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
        if (this.wrongCount == null) this.wrongCount = 1;
    }
}

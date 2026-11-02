package com.youfen.word.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "word")
public class Word {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String spelling;
    @Column(length = 100)
    private String phonetic;
    @Column(length = 500)
    private String meaning;
    private Long categoryId;
    /** 难度 1-3 */
    private Integer difficulty;
}

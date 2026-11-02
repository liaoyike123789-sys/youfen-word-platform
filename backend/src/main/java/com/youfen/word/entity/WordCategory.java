package com.youfen.word.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "word_category")
public class WordCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 200)
    private String description;
}

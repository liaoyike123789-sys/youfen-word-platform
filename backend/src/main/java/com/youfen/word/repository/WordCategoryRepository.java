package com.youfen.word.repository;

import com.youfen.word.entity.WordCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCategoryRepository extends JpaRepository<WordCategory, Long> {
}

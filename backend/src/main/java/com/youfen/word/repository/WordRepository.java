package com.youfen.word.repository;

import com.youfen.word.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("select w from Word w where w.spelling like %:keyword% or w.meaning like %:keyword%")
    Page<Word> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("select w from Word w where lower(w.spelling) like lower(concat(:letter, '%'))")
    Page<Word> findByLetter(@Param("letter") String letter, Pageable pageable);
}

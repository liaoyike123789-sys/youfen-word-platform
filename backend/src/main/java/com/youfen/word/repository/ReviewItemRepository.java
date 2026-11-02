package com.youfen.word.repository;

import com.youfen.word.entity.ReviewItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewItemRepository extends JpaRepository<ReviewItem, Long> {
    List<ReviewItem> findByUserId(Long userId);
    Optional<ReviewItem> findByUserIdAndWordId(Long userId, Long wordId);
    void deleteByUserIdAndWordId(Long userId, Long wordId);
}

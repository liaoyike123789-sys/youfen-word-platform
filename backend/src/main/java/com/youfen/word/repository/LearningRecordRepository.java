package com.youfen.word.repository;

import com.youfen.word.entity.LearningRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> {
    Page<LearningRecord> findByUserId(Long userId, Pageable pageable);

    long countByUserIdAndLearnDate(Long userId, LocalDate date);

    long countByUserIdAndResultAndLearnDate(Long userId, String result, LocalDate date);

    @Query("select r.learnDate d, count(r) c from LearningRecord r where r.userId = :userId group by r.learnDate order by r.learnDate")
    List<Object[]> trend(@Param("userId") Long userId);
}

package com.youfen.word.service;

import com.youfen.word.repository.LearningRecordRepository;
import com.youfen.word.repository.ReviewItemRepository;
import com.youfen.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final WordRepository wordRepository;
    private final LearningRecordRepository recordRepository;
    private final ReviewItemRepository reviewRepository;

    public Map<String, Object> overview(Long userId) {
        long totalWords = wordRepository.count();
        long totalRecords = recordRepository.findByUserId(userId,
                org.springframework.data.domain.PageRequest.of(0, 1000)).getTotalElements();
        long wrongCount = reviewRepository.findByUserId(userId).size();
        Map<String, Object> m = new HashMap<>();
        m.put("totalWords", totalWords);
        m.put("totalReviews", totalRecords);
        m.put("wrongBookSize", wrongCount);
        return m;
    }

    public Map<String, Object> today(Long userId) {
        LocalDate d = LocalDate.now();
        long all = recordRepository.countByUserIdAndLearnDate(userId, d);
        long known = recordRepository.countByUserIdAndResultAndLearnDate(userId, "known", d);
        long unknown = recordRepository.countByUserIdAndResultAndLearnDate(userId, "unknown", d);
        Map<String, Object> m = new HashMap<>();
        m.put("date", d.toString());
        m.put("learned", all);
        m.put("known", known);
        m.put("unknown", unknown);
        m.put("accuracy", all == 0 ? 0 : Math.round(known * 100.0 / all));
        return m;
    }

    public List<Object[]> trend(Long userId) { return recordRepository.trend(userId); }

    public Map<String, Object> mastery(Long userId) {
        long wrong = reviewRepository.findByUserId(userId).size();
        long total = wordRepository.count();
        long mastered = Math.max(total - wrong, 0);
        Map<String, Object> m = new HashMap<>();
        m.put("mastered", mastered);
        m.put("learning", wrong);
        m.put("total", total);
        return m;
    }

    public long streak(Long userId) {
        // 简化：从今天往前数，有学习记录的连续天数
        LocalDate d = LocalDate.now();
        long s = 0;
        for (int i = 0; i < 365; i++) {
            long c = recordRepository.countByUserIdAndLearnDate(userId, d.minusDays(i));
            if (c > 0) s++;
            else if (i == 0) continue; // 今天还没学，不断签
            else break;
        }
        return s;
    }

    public double accuracy(Long userId) {
        LocalDate d = LocalDate.now();
        long all = recordRepository.countByUserIdAndLearnDate(userId, d);
        long known = recordRepository.countByUserIdAndResultAndLearnDate(userId, "known", d);
        return all == 0 ? 0 : Math.round(known * 1000.0 / all) / 10.0;
    }
}

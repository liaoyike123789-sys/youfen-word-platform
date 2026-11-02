package com.youfen.word.service;

import com.youfen.word.common.BusinessException;
import com.youfen.word.entity.LearningRecord;
import com.youfen.word.entity.ReviewItem;
import com.youfen.word.entity.Word;
import com.youfen.word.repository.LearningRecordRepository;
import com.youfen.word.repository.ReviewItemRepository;
import com.youfen.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LearningService {

    private final LearningRecordRepository recordRepository;
    private final ReviewItemRepository reviewRepository;
    private final WordRepository wordRepository;

    /** 今日学习队列：从未学过的单词里取前 10 个 */
    public List<Word> todayQueue(Long userId, int limit) {
        List<Word> all = wordRepository.findAll();
        List<LearningRecord> done = recordRepository.findByUserId(userId, PageRequest.of(0, 1000)).getContent();
        List<Long> doneIds = done.stream().map(LearningRecord::getWordId).toList();
        List<Word> rest = all.stream().filter(w -> !doneIds.contains(w.getId())).limit(limit).toList();
        return new ArrayList<>(rest);
    }

    @Transactional
    public Map<String, Object> review(Long userId, Long wordId, String result) {
        if (!"known".equals(result) && !"unknown".equals(result)) {
            throw new BusinessException(400, "result 只能是 known 或 unknown");
        }
        if (!wordRepository.existsById(wordId)) throw new BusinessException(404, "单词不存在");
        LearningRecord r = new LearningRecord();
        r.setUserId(userId); r.setWordId(wordId); r.setResult(result);
        recordRepository.save(r);

        if ("unknown".equals(result)) {
            ReviewItem item = reviewRepository.findByUserIdAndWordId(userId, wordId).orElse(null);
            if (item == null) {
                item = new ReviewItem();
                item.setUserId(userId); item.setWordId(wordId);
            } else {
                item.setWrongCount(item.getWrongCount() + 1);
            }
            reviewRepository.save(item);
        }
        return Map.of("recordId", r.getId(), "result", result);
    }

    public Page<LearningRecord> records(Long userId, int page, int size) {
        return recordRepository.findByUserId(userId, PageRequest.of(Math.max(page, 0), Math.min(size, 100)));
    }

    public List<Map<String, Object>> wrongbook(Long userId) {
        List<ReviewItem> items = reviewRepository.findByUserId(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReviewItem it : items) {
            Word w = wordRepository.findById(it.getWordId()).orElse(null);
            if (w != null) {
                list.add(Map.of("wordId", w.getId(), "spelling", w.getSpelling(),
                        "meaning", w.getMeaning(), "wrongCount", it.getWrongCount()));
            }
        }
        return list;
    }

    @Transactional
    public void removeWrong(Long userId, Long wordId) {
        reviewRepository.deleteByUserIdAndWordId(userId, wordId);
    }

    public Map<String, Object> checkin(Long userId) {
        LocalDate today = LocalDate.now();
        long cnt = recordRepository.countByUserIdAndLearnDate(userId, today);
        return Map.of("date", today.toString(), "learnedCount", cnt, "checkedIn", cnt > 0);
    }

    public Map<String, Object> checkinStatus(Long userId) {
        LocalDate today = LocalDate.now();
        long cnt = recordRepository.countByUserIdAndLearnDate(userId, today);
        return Map.of("date", today.toString(), "learnedCount", cnt);
    }

    public List<Object[]> history(Long userId) {
        return recordRepository.trend(userId);
    }
}

package com.youfen.word.service;

import com.youfen.word.common.BusinessException;
import com.youfen.word.entity.Word;
import com.youfen.word.entity.WordCategory;
import com.youfen.word.repository.WordCategoryRepository;
import com.youfen.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final WordCategoryRepository categoryRepository;

    public Page<Word> list(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 50), Sort.by("id"));
        if (categoryId == null) return wordRepository.findAll(pageable);
        return wordRepository.findByCategoryId(categoryId, pageable);
    }

    public Word detail(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new BusinessException(404, "单词不存在"));
    }

    public Page<Word> search(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) throw new BusinessException(400, "搜索关键词不能为空");
        return wordRepository.search(keyword, PageRequest.of(Math.max(page, 0), Math.min(size, 50)));
    }

    public Page<Word> byLetter(String letter, int page, int size) {
        if (letter == null || letter.length() != 1) throw new BusinessException(400, "字母参数必须是单个字母");
        return wordRepository.findByLetter(letter, PageRequest.of(Math.max(page, 0), Math.min(size, 50)));
    }

    public Word create(Word w) { return wordRepository.save(w); }

    public Word update(Long id, Word in) {
        Word w = detail(id);
        if (in.getSpelling() != null) w.setSpelling(in.getSpelling());
        if (in.getPhonetic() != null) w.setPhonetic(in.getPhonetic());
        if (in.getMeaning() != null) w.setMeaning(in.getMeaning());
        if (in.getCategoryId() != null) w.setCategoryId(in.getCategoryId());
        if (in.getDifficulty() != null) w.setDifficulty(in.getDifficulty());
        return wordRepository.save(w);
    }

    public void delete(Long id) { wordRepository.deleteById(id); }

    public java.util.List<WordCategory> categories() { return categoryRepository.findAll(); }

    public WordCategory createCategory(WordCategory c) { return categoryRepository.save(c); }

    public void deleteCategory(Long id) { categoryRepository.deleteById(id); }
}

package com.youfen.word.controller;

import com.youfen.word.common.Result;
import com.youfen.word.entity.Word;
import com.youfen.word.entity.WordCategory;
import com.youfen.word.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    /** 7. 分页查询单词 */
    @GetMapping("/words")
    public Result<Page<Word>> list(@RequestParam(required = false) Long categoryId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return Result.ok(wordService.list(categoryId, page, size));
    }

    /** 8. 单词详情 */
    @GetMapping("/words/{id}")
    public Result<Word> detail(@PathVariable Long id) { return Result.ok(wordService.detail(id)); }

    /** 9. 按分类查询 */
    @GetMapping("/words/category/{cid}")
    public Result<Page<Word>> byCategory(@PathVariable Long cid,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return Result.ok(wordService.list(cid, page, size));
    }

    /** 10. 搜索 */
    @GetMapping("/words/search")
    public Result<Page<Word>> search(@RequestParam String keyword,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return Result.ok(wordService.search(keyword, page, size));
    }

    /** 11. 按字母分组 */
    @GetMapping("/words/letter/{letter}")
    public Result<Page<Word>> byLetter(@PathVariable String letter,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return Result.ok(wordService.byLetter(letter, page, size));
    }

    /** 12. 新增单词（管理） */
    @PostMapping("/words")
    public Result<Word> create(@RequestBody Word w) { return Result.ok(wordService.create(w)); }

    /** 13. 修改单词 */
    @PutMapping("/words/{id}")
    public Result<Word> update(@PathVariable Long id, @RequestBody Word w) {
        return Result.ok(wordService.update(id, w));
    }

    /** 14. 删除单词 */
    @DeleteMapping("/words/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        wordService.delete(id);
        return Result.ok();
    }

    /** 15. 分类列表 */
    @GetMapping("/categories")
    public Result<List<WordCategory>> categories() { return Result.ok(wordService.categories()); }

    /** 16. 新增分类 */
    @PostMapping("/categories")
    public Result<WordCategory> createCategory(@RequestBody WordCategory c) {
        return Result.ok(wordService.createCategory(c));
    }

    /** 17. 删除分类 */
    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        wordService.deleteCategory(id);
        return Result.ok();
    }
}

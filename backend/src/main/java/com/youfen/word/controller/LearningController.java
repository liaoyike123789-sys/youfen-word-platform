package com.youfen.word.controller;

import com.youfen.word.common.Result;
import com.youfen.word.entity.LearningRecord;
import com.youfen.word.entity.Word;
import com.youfen.word.service.AuthService;
import com.youfen.word.service.LearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;
    private final AuthService authService;

    private Long uid(String token) { return authService.currentUser(token.replace("Bearer ", "")).getId(); }

    /** 18. 今日学习队列 */
    @GetMapping("/today")
    public Result<List<Word>> today(@RequestHeader("Authorization") String token,
                                    @RequestParam(defaultValue = "10") int limit) {
        return Result.ok(learningService.todayQueue(uid(token), limit));
    }

    /** 19. 标记认识/不认识 */
    @PostMapping("/review")
    public Result<Map<String, Object>> review(@RequestHeader("Authorization") String token,
                                              @RequestBody Map<String, Object> body) {
        Long wordId = Long.valueOf(body.get("wordId").toString());
        String result = (String) body.get("result");
        return Result.ok(learningService.review(uid(token), wordId, result));
    }

    /** 20. 学习记录列表 */
    @GetMapping("/records")
    public Result<Page<LearningRecord>> records(@RequestHeader("Authorization") String token,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        return Result.ok(learningService.records(uid(token), page, size));
    }

    /** 21. 错题本 */
    @GetMapping("/wrongbook")
    public Result<List<Map<String, Object>>> wrongbook(@RequestHeader("Authorization") String token) {
        return Result.ok(learningService.wrongbook(uid(token)));
    }

    /** 22. 移出错题本 */
    @DeleteMapping("/wrongbook/{wordId}")
    public Result<Void> removeWrong(@RequestHeader("Authorization") String token, @PathVariable Long wordId) {
        learningService.removeWrong(uid(token), wordId);
        return Result.ok();
    }

    /** 23. 打卡 */
    @PostMapping("/checkin")
    public Result<Map<String, Object>> checkin(@RequestHeader("Authorization") String token) {
        return Result.ok(learningService.checkin(uid(token)));
    }

    /** 24. 今日打卡状态 */
    @GetMapping("/checkin/status")
    public Result<Map<String, Object>> checkinStatus(@RequestHeader("Authorization") String token) {
        return Result.ok(learningService.checkinStatus(uid(token)));
    }

    /** 25. 历史学习趋势 */
    @GetMapping("/history")
    public Result<List<Object[]>> history(@RequestHeader("Authorization") String token) {
        return Result.ok(learningService.history(uid(token)));
    }
}

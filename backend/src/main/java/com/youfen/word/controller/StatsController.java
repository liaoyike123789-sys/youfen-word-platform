package com.youfen.word.controller;

import com.youfen.word.common.Result;
import com.youfen.word.service.AuthService;
import com.youfen.word.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final AuthService authService;

    private Long uid(String token) { return authService.currentUser(token.replace("Bearer ", "")).getId(); }

    /** 26. 总览 */
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.overview(uid(token)));
    }

    /** 27. 今日数据 */
    @GetMapping("/today")
    public Result<Map<String, Object>> today(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.today(uid(token)));
    }

    /** 28. 学习趋势 */
    @GetMapping("/trend")
    public Result<List<Object[]>> trend(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.trend(uid(token)));
    }

    /** 29. 掌握分布 */
    @GetMapping("/mastery")
    public Result<Map<String, Object>> mastery(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.mastery(uid(token)));
    }

    /** 30. 连续打卡天数 */
    @GetMapping("/streak")
    public Result<Long> streak(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.streak(uid(token)));
    }

    /** 31. 今日正确率 */
    @GetMapping("/accuracy")
    public Result<Double> accuracy(@RequestHeader("Authorization") String token) {
        return Result.ok(statsService.accuracy(uid(token)));
    }
}

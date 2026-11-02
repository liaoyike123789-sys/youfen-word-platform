package com.youfen.word.controller;

import com.youfen.word.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    /** 32. 健康检查 */
    @GetMapping("/api/health")
    public Result<Map<String, Object>> health() {
        return Result.ok(Map.of("status", "UP", "service", "youfen-word-platform"));
    }
}

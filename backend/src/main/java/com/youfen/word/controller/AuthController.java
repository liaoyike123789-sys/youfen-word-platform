package com.youfen.word.controller;

import com.youfen.word.common.Result;
import com.youfen.word.entity.User;
import com.youfen.word.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** 1. 注册 */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        return Result.ok(authService.register(body.get("username"), body.get("password"), body.get("nickname")));
    }

    /** 2. 登录 */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        return Result.ok(authService.login(body.get("username"), body.get("password")));
    }

    /** 3. 登出 */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null) authService.logout(token.replace("Bearer ", ""));
        return Result.ok();
    }

    /** 4. 当前用户 */
    @GetMapping("/me")
    public Result<User> me(@RequestHeader("Authorization") String token) {
        return Result.ok(authService.currentUser(token.replace("Bearer ", "")));
    }

    /** 5. 修改密码 */
    @PutMapping("/password")
    public Result<Void> changePwd(@RequestHeader("Authorization") String token,
                                   @RequestBody Map<String, String> body) {
        User u = authService.currentUser(token.replace("Bearer ", ""));
        authService.changePassword(u.getId(), body.get("oldPassword"), body.get("newPassword"));
        return Result.ok();
    }

    /** 6. 完善资料 */
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestHeader("Authorization") String token,
                                       @RequestBody Map<String, String> body) {
        User u = authService.currentUser(token.replace("Bearer ", ""));
        return Result.ok(authService.updateProfile(u.getId(), body.get("nickname"), body.get("email")));
    }
}

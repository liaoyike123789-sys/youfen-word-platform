package com.youfen.word.service;

import com.youfen.word.common.BusinessException;
import com.youfen.word.entity.User;
import com.youfen.word.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    /** token -> userId，演示用内存表；生产环境应换 Redis */
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    public Map<String, Object> register(String username, String password, String nickname) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(400, "用户名已存在");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // 演示用明文；生产环境必须 BCrypt
        u.setNickname(nickname == null ? username : nickname);
        userRepository.save(u);
        return Map.of("userId", u.getId(), "username", u.getUsername());
    }

    public Map<String, Object> login(String username, String password) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(401, "用户不存在"));
        if (!u.getPassword().equals(password)) {
            throw new BusinessException(401, "密码错误");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, u.getId());
        return Map.of("token", token, "userId", u.getId(), "nickname", u.getNickname());
    }

    public void logout(String token) { tokenStore.remove(token); }

    public User currentUser(String token) {
        Long uid = tokenStore.get(token);
        if (uid == null) throw new BusinessException(401, "未登录或登录已过期");
        return userRepository.findById(uid).orElseThrow(() -> new BusinessException(401, "用户不存在"));
    }

    public void changePassword(Long userId, String oldPwd, String newPwd) {
        User u = userRepository.findById(userId).orElseThrow();
        if (!u.getPassword().equals(oldPwd)) throw new BusinessException(400, "原密码错误");
        u.setPassword(newPwd);
        userRepository.save(u);
    }

    public User updateProfile(Long userId, String nickname, String email) {
        User u = userRepository.findById(userId).orElseThrow();
        if (nickname != null) u.setNickname(nickname);
        if (email != null) u.setEmail(email);
        return userRepository.save(u);
    }
}

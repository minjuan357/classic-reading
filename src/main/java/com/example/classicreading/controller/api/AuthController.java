package com.example.classicreading.controller.api;

import com.example.classicreading.dto.LoginRequest;
import com.example.classicreading.dto.RegisterRequest;
import com.example.classicreading.entity.User;
import com.example.classicreading.service.UserService;
import com.example.classicreading.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名已存在"));
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "注册成功"));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成 JWT
            String token = jwtUtil.generateToken(request.getUsername());

            // 获取用户详细信息
            User user = userService.findByUsername(request.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("id", user.getId());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", "用户名或密码错误"));
        }
    }

    /**
     * 获取当前登录用户信息（用于个人中心）
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("email", user.getEmail());
        info.put("createdAt", user.getCreatedAt());

        return ResponseEntity.ok(info);
    }
}
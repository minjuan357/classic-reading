package com.example.classicreading.service;

import com.example.classicreading.entity.User;
import com.example.classicreading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return true-存在，false-不存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体
     * @throws RuntimeException 如果用户不存在
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 根据 ID 查找用户
     * @param id 用户 ID
     * @return 用户实体
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 保存用户
     * @param user 用户实体
     * @return 保存后的用户（含生成的主键）
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}
// RegisterRequest.java
package com.example.classicreading.dto;

/**
 * 用户注册请求数据传输对象
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;   // 可选字段

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
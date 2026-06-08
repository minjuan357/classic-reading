// LoginRequest.java
package com.example.classicreading.dto;

/**
 * 登录请求数据传输对象
 */
public class LoginRequest {
    private String username;
    private String password;

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
    public void validate() {
        // 验证用户名和密码是否为空
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
    }
    public void setName(String name) {
        this.username = name;
    }

   }

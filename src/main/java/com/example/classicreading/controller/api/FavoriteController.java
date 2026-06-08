package com.example.classicreading.controller.api;

import com.example.classicreading.entity.Book;
import com.example.classicreading.entity.Favorite;
import com.example.classicreading.entity.User;
import com.example.classicreading.repository.BookRepository;
import com.example.classicreading.repository.FavoriteRepository;
import com.example.classicreading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    // 获取当前用户收藏的书籍列表
    @GetMapping("/favorites")
    public List<Book> getFavorites() {
        User user = getCurrentUser();
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        return favorites.stream().map(Favorite::getBook).collect(Collectors.toList());
    }

    // 添加收藏
    @PostMapping("/favorites")
    public Map<String, Object> addFavorite(@RequestBody Map<String, Long> payload) {
        User user = getCurrentUser();
        Long bookId = payload.get("bookId");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("书籍不存在"));

        // 检查是否已收藏
        if (favoriteRepository.findByUserIdAndBookId(user.getId(), bookId).isPresent()) {
            throw new RuntimeException("已经收藏过本书");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBook(book);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "收藏成功");
        response.put("favoriteId", favorite.getId());
        return response;
    }

    // 取消收藏
    @DeleteMapping("/favorites")
    public Map<String, String> removeFavorite(@RequestParam Long bookId) {
        User user = getCurrentUser();
        favoriteRepository.deleteByUserIdAndBookId(user.getId(), bookId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "取消收藏成功");
        return response;
    }

    // 检查某本书是否已被当前用户收藏
    @GetMapping("/favorites/check")
    public Map<String, Boolean> checkFavorite(@RequestParam Long bookId) {
        User user = getCurrentUser();
        boolean isFavorite = favoriteRepository.findByUserIdAndBookId(user.getId(), bookId).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFavorite", isFavorite);
        return response;
    }
}
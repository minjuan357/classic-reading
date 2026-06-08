package com.example.classicreading.controller.api;

import com.example.classicreading.entity.Book;
import com.example.classicreading.entity.Chapter;
import com.example.classicreading.entity.History;
import com.example.classicreading.entity.User;
import com.example.classicreading.repository.BookRepository;
import com.example.classicreading.repository.ChapterRepository;
import com.example.classicreading.repository.HistoryRepository;
import com.example.classicreading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    // 获取当前登录用户
    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    // 记录浏览历史
    @PostMapping("/history")
    public Map<String, String> addHistory(@RequestBody Map<String, Object> payload) {
        User user = getCurrentUser();
        Long chapterId = Long.valueOf(payload.get("chapterId").toString());
        Long bookId = Long.valueOf(payload.get("bookId").toString());
        String chapterTitle = payload.get("chapterTitle") == null ? null : payload.get("chapterTitle").toString();
        String bookTitle = payload.get("bookTitle") == null ? null : payload.get("bookTitle").toString();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("书籍不存在"));
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new RuntimeException("篇章不存在"));

        if (chapterTitle == null || chapterTitle.isBlank()) {
            chapterTitle = chapter.getTitle();
        }
        if (bookTitle == null || bookTitle.isBlank()) {
            bookTitle = book.getTitle();
        }

        History history = new History();
        history.setUser(user);
        history.setBook(book);
        history.setChapter(chapter);
        history.setChapterTitle(chapterTitle);
        history.setBookTitle(bookTitle);
        history.setViewedAt(LocalDateTime.now());

        historyRepository.save(history);

        Map<String, String> response = new HashMap<>();
        response.put("message", "记录成功");
        return response;
    }

    // 获取当前用户的浏览历史
    @GetMapping("/history")
    public List<History> getHistory() {
        User user = getCurrentUser();
        return historyRepository.findByUserIdOrderByViewedAtDesc(user.getId());
    }
}
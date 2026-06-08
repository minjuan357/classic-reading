package com.example.classicreading.controller.api;

import com.example.classicreading.entity.Book;
import com.example.classicreading.entity.Chapter;
import com.example.classicreading.repository.BookRepository;
import com.example.classicreading.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// BookApiController.java
@RestController
@RequestMapping("/api/books")
public class BookApiController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/byCategory")
    public List<Book> getByCategory(@RequestParam Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @GetMapping("/countByCategory")
    public long countByCategory(@RequestParam Long categoryId) {
        return bookRepository.countByCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @GetMapping("/{bookId}/chapters")
    public List<Chapter> getChapters(@PathVariable Long bookId) {
        return chapterRepository.findOrderedByBookId(bookId);
    }

    @GetMapping("/chapters/{chapterId}")
    public Chapter getChapter(@PathVariable Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContainingOrAuthorUrlContaining(keyword, keyword, keyword);
    }
}
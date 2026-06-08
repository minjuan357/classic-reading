package com.example.classicreading.controller.api;

import com.example.classicreading.entity.Chapter;
import com.example.classicreading.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapters")
public class ChapterApiController {
    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/{id}")
    public Chapter getChapter(@PathVariable Long id) {
        return chapterRepository.findById(id).orElseThrow(() -> new RuntimeException("Chapter not found"));
    }
}
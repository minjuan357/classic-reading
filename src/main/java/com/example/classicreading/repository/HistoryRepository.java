package com.example.classicreading.repository;

import com.example.classicreading.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserIdOrderByViewedAtDesc(Long userId);
    // 用于检查是否已存在（可选）
    boolean existsByUserIdAndChapterId(Long userId, Long chapterId);
}
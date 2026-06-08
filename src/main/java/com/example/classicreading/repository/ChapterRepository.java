package com.example.classicreading.repository;

import com.example.classicreading.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    // 统一排序策略：优先 sort_order，其次 order_num，最后 id
    @Query("""
            SELECT c FROM Chapter c
            WHERE c.bookId = :bookId
            ORDER BY
              CASE WHEN c.sortOrder IS NULL THEN 1 ELSE 0 END,
              c.sortOrder ASC,
              CASE WHEN c.orderNum IS NULL THEN 1 ELSE 0 END,
              c.orderNum ASC,
              c.id ASC
            """)
    List<Chapter> findOrderedByBookId(@Param("bookId") Long bookId);

    Optional<Chapter> findByBookIdAndTitle(Long bookId, String title);

    void deleteByBookId(Long bookId);
}
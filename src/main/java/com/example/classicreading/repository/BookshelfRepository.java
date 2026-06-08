package com.example.classicreading.repository;

import com.example.classicreading.entity.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {
    List<Bookshelf> findByUserIdOrderByAddedAtDesc(Long userId);
    Optional<Bookshelf> findByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
package com.example.classicreading.repository;

import com.example.classicreading.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
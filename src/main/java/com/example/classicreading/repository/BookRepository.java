package com.example.classicreading.repository;

import com.example.classicreading.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    List<Book> findByTitleContainingOrAuthorUrlContaining(String title, String authorUrl);

    List<Book> findByTitleContainingOrAuthorContainingOrAuthorUrlContaining(String title, String author, String authorUrl);
    List<Book> findByCategoryId(Long categoryId);

    long countByCategoryId(Long categoryId);
}
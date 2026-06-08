package com.example.classicreading.repository;

import com.example.classicreading.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 只保留 Optional 版本，删除 List 版本
    Optional<Category> findByName(String name);
}
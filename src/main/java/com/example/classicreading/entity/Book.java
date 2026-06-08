package com.example.classicreading.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_url", length = 100)
    private String authorUrl;      // 作者URL

    @Column(name = "cover_url", length = 255)
    private String coverUrl;       // 封面URL

    @Column(columnDefinition = "TEXT")
    private String description;     // 简介

    @Column(length = 50)
    private String dynasty;         // 朝代

    @Column(nullable = false, length = 100)
    private String title;           // 书名

    @Column(name = "category_id")
    private Long categoryId;        // 分类ID

    @Column(length = 100)
    private String author;          // 作者

    // 无参构造
    public Book() {}

    // getter/setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
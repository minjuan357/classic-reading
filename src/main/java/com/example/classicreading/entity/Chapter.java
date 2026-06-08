package com.example.classicreading.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;     // 原文

    @Column(columnDefinition = "TEXT")
    private String translation;      // 译文

    @Column(columnDefinition = "TEXT")
    private String annotation;       // 注释

    @Column(columnDefinition = "TEXT")
    private String content;          // 内容（兼容字段）

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(columnDefinition = "TEXT")
    private String appreciation;

    @Column(columnDefinition = "TEXT")
    private String pinyin;

    // 无参构造
    public Chapter() {}

    // getter/setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(id, chapter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
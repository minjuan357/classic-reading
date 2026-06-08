# 数据导入与验证 SQL 清单

以下 SQL 以 MySQL 为例，用于“导入前检查、导入后验收”。

## 1) 导入前结构检查

```sql
SHOW CREATE TABLE category;
SHOW CREATE TABLE book;
SHOW CREATE TABLE users;
SHOW CREATE TABLE chapter;
SHOW CREATE TABLE conversation;
SHOW CREATE TABLE message;
SHOW CREATE TABLE favorite;
SHOW CREATE TABLE bookshelf;
SHOW CREATE TABLE history;
```

重点检查：
- `chapter` 是否有 `sort_order`（建议主排序字段）
- `conversation` 是否包含 `user_message`、`ai_reply`（若有非空约束，后端已做兼容写入）

## 2) 导入顺序（建议）

1. `category`
2. `book`
3. `users`
4. `chapter`
5. `conversation`
6. `message`
7. `favorite`
8. `bookshelf`
9. `history`

## 3) 导入后快速校验

```sql
SELECT COUNT(*) AS c FROM category;
SELECT COUNT(*) AS c FROM book;
SELECT COUNT(*) AS c FROM users;
SELECT COUNT(*) AS c FROM chapter;
SELECT COUNT(*) AS c FROM conversation;
SELECT COUNT(*) AS c FROM message;
SELECT COUNT(*) AS c FROM favorite;
SELECT COUNT(*) AS c FROM bookshelf;
SELECT COUNT(*) AS c FROM history;
```

```sql
-- 校验章节排序字段覆盖情况
SELECT
  SUM(CASE WHEN sort_order IS NOT NULL THEN 1 ELSE 0 END) AS has_sort_order,
  SUM(CASE WHEN order_num IS NOT NULL THEN 1 ELSE 0 END) AS has_order_num
FROM chapter;
```

```sql
-- 检查外键是否存在孤儿记录
SELECT COUNT(*) AS orphan_chapter_book
FROM chapter c LEFT JOIN book b ON c.book_id = b.id
WHERE b.id IS NULL;

SELECT COUNT(*) AS orphan_favorite_user
FROM favorite f LEFT JOIN users u ON f.user_id = u.id
WHERE u.id IS NULL;

SELECT COUNT(*) AS orphan_favorite_book
FROM favorite f LEFT JOIN book b ON f.book_id = b.id
WHERE b.id IS NULL;

SELECT COUNT(*) AS orphan_history_user
FROM history h LEFT JOIN users u ON h.user_id = u.id
WHERE u.id IS NULL;

SELECT COUNT(*) AS orphan_history_book
FROM history h LEFT JOIN book b ON h.book_id = b.id
WHERE b.id IS NULL;

SELECT COUNT(*) AS orphan_history_chapter
FROM history h LEFT JOIN chapter c ON h.chapter_id = c.id
WHERE c.id IS NULL;
```

## 4) 与页面联调的抽样 SQL

```sql
-- 首页/典籍库
SELECT id, title, author, cover_url, category_id FROM book ORDER BY id DESC LIMIT 10;

-- 书籍详情章节（按推荐排序）
SELECT id, title, sort_order, order_num
FROM chapter
WHERE book_id = ?
ORDER BY
  CASE WHEN sort_order IS NULL THEN 1 ELSE 0 END,
  sort_order ASC,
  CASE WHEN order_num IS NULL THEN 1 ELSE 0 END,
  order_num ASC,
  id ASC;

-- 浏览历史
SELECT id, user_id, book_id, chapter_id, book_title, chapter_title, viewed_at
FROM history
ORDER BY viewed_at DESC
LIMIT 20;
```

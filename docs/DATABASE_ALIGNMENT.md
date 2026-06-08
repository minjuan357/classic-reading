# 数据库字段对齐与导入清单

本文件用于在“从数据库导入数据”前，快速核对数据库字段与当前代码映射是否一致。

## 1) 已对齐的核心表

- `book`
  - 实体：`Book`
  - 关键字段映射：`author_url -> authorUrl`、`cover_url -> coverUrl`、`category_id -> categoryId`

- `category`
  - 实体：`Category`
  - 关键字段映射：`id`、`name`、`description`

- `chapter`
  - 实体：`Chapter`
  - 关键字段映射：`book_id -> bookId`、`original_text -> originalText`、`sort_order -> sortOrder`
  - 已补齐字段：`order_num -> orderNum`、`appreciation`、`pinyin`

- `favorite`
  - 实体：`Favorite`
  - 关键字段映射：`created_at -> createdAt`、`book_id`、`user_id`

- `history`
  - 实体：`History`
  - 关键字段映射：`viewed_at -> viewedAt`、`book_title -> bookTitle`、`chapter_title -> chapterTitle`
  - 后端已增强：当 `bookTitle/chapterTitle` 未传时，自动回填书名和章节名

- `bookshelf`
  - 实体：`Bookshelf`
  - 关键字段映射：`added_at -> addedAt`、`book_id`、`user_id`

- `conversation`
  - 实体：`Conversation`
  - 当前主要使用：`id`、`user_id`、`title`、`content`、`type`、`created_at`、`updated_at`

- `message`
  - 实体：`Message`
  - 关键字段映射：`conversation_id`、`user_id`、`role`、`content`、`created_at`

- `users`
  - 实体：`User`
  - 表名已显式映射为 `users`

## 2) 导入顺序（推荐）

按外键依赖顺序导入，避免外键失败：

1. `category`
2. `book`
3. `users`
4. `chapter`
5. `conversation`
6. `message`
7. `favorite`
8. `bookshelf`
9. `history`

## 3) 导入后验收（最小清单）

- 典籍库可正常显示书名、作者、封面
- 书籍详情可加载章节列表且排序正常
- 章节页可打开且能写入历史
- 收藏页可加载数据
- 浏览历史可显示“书名 · 章节名”，点击可跳章节
- AI 历史对话列表可读取

## 4) 当前仍需你最终确认的规则

- `chapter` 排序以哪个字段为准：
  - 建议统一用 `sort_order`，`order_num` 仅兼容保留
- `conversation` 中 `ai_reply/user_message` 字段是否强依赖：
  - 当前代码主要用 `message` 表做多轮消息
  - 如果你的库里这两个字段是非空约束，需要再补实体与写入逻辑

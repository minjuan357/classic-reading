package com.example.classicreading.service;

// 必要的导入语句
import com.example.classicreading.entity.Book;
import com.example.classicreading.entity.Category;
import com.example.classicreading.entity.Chapter;
import com.example.classicreading.repository.BookRepository;
import com.example.classicreading.repository.CategoryRepository;
import com.example.classicreading.repository.ChapterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

@Service
public class DataImportService {

    // 正确的字段声明
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void importFromJson(String jsonFileName) {
        try {
            // 兼容：从 resources/data 读取小文件导入
            try (InputStream inputStream = getClassPathInputStream("data/" + jsonFileName)) {
                List<JsonNode> items = objectMapper.readValue(inputStream, new TypeReference<List<JsonNode>>() {
                });

                if (items.isEmpty()) {
                    System.out.println("文件为空: " + jsonFileName);
                    return;
                }

                JsonNode first = items.get(0);
                // 兼容两种格式：
                // 1) 老格式：数组中每项是章节，首项包含 book/category 字段
                // 2) 新格式：数组中每项是书籍，书籍下有 chapters 数组
                if (first.has("chapters")) {
                    importBookWithNestedChapters(items);
                } else {
                    importLegacyChapterList(items);
                }
                System.out.println("导入完成: " + jsonFileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导入失败: " + jsonFileName, e);
        }
    }

    public void importHistoryMergedShiFromExternalFile(String externalJsonFilePath) {
        ensureChapterUtf8mb4();

        Category shiCategory = categoryRepository.findByName("史")
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName("史");
                    newCat.setDescription("历史典籍");
                    return categoryRepository.save(newCat);
                });

        if (externalJsonFilePath == null || externalJsonFilePath.isBlank()) {
            System.out.println("跳过导入：externalJsonFilePath 为空");
            return;
        }

        Path path = Path.of(externalJsonFilePath);
        if (!Files.exists(path)) {
            System.out.println("跳过导入：文件不存在 -> " + externalJsonFilePath);
            return;
        }

        // 大文件：流式解析，避免一次性 read 全量 into memory
        int bookCount = 0;
        int chapterCount = 0;

        try (InputStream inputStream = Files.newInputStream(path);
             JsonParser parser = objectMapper.getFactory().createParser(inputStream)) {

            JsonToken token = parser.nextToken();
            if (token != JsonToken.START_ARRAY) {
                throw new RuntimeException("历史文件格式错误：期望 JSON 数组顶层");
            }

            // 由于 @Transactional 会维持持久化上下文过大，这里采用周期性 flush/clear
            while ((token = parser.nextToken()) != null && token != JsonToken.END_ARRAY) {
                if (token != JsonToken.START_OBJECT) {
                    parser.skipChildren();
                    continue;
                }

                JsonNode bookNode = objectMapper.readTree(parser);
                if (!isShiBook(bookNode)) {
                    continue;
                }

                Book book = upsertBookWithExplicitCategory(bookNode, shiCategory.getId());
                JsonNode chaptersNode = bookNode.get("chapters");
                if (chaptersNode != null && chaptersNode.isArray()) {
                    int order = 1;
                    for (JsonNode chapterNode : chaptersNode) {
                        try {
                            importChapterNode(book.getId(), chapterNode, order++);
                            chapterCount++;
                        } catch (Exception e) {
                            // 单章节失败就跳过，避免中断整个大文件导入
                            String chapterTitle = chapterNode.has("title") ? chapterNode.get("title").asText() : "(unknown)";
                            System.err.println("跳过章节导入失败：book=" + book.getTitle() + ", chapter=" + chapterTitle);
                        }
                        if ((chapterCount % 200) == 0) {
                            entityManager.clear();
                        }
                    }
                }

                bookCount++;
                if ((bookCount % 10) == 0) {
                    System.out.println("导入进度：已处理书籍 " + bookCount);
                    entityManager.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("history_merged 导入失败：" + externalJsonFilePath, e);
        }

        System.out.println("导入完成：史部书籍 " + bookCount + "，章节 " + chapterCount);
    }

    @Transactional
    public void importMissingJingBooksFromExternalFile(String externalJsonFilePath, String preservedTitle) {
        if (externalJsonFilePath == null || externalJsonFilePath.isBlank()) {
            System.out.println("跳过导入：经部文件路径为空");
            return;
        }

        Path path = Path.of(externalJsonFilePath);
        if (!Files.exists(path)) {
            System.out.println("跳过导入：经部文件不存在 -> " + externalJsonFilePath);
            return;
        }

        Category jingCategory = categoryRepository.findByName("经")
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName("经");
                    newCat.setDescription("儒家经典著作");
                    return categoryRepository.save(newCat);
                });

        int createdBookCount = 0;
        int skippedBookCount = 0;
        int createdChapterCount = 0;

        try (InputStream inputStream = Files.newInputStream(path);
             JsonParser parser = objectMapper.getFactory().createParser(inputStream)) {

            JsonToken token = parser.nextToken();
            if (token != JsonToken.START_ARRAY) {
                throw new RuntimeException("经部文件格式错误：期望 JSON 数组顶层");
            }

            while ((token = parser.nextToken()) != null && token != JsonToken.END_ARRAY) {
                if (token != JsonToken.START_OBJECT) {
                    parser.skipChildren();
                    continue;
                }

                JsonNode bookNode = objectMapper.readTree(parser);
                String title = bookNode.has("title") ? sanitizeText(bookNode.get("title").asText()) : "";
                if (title == null || title.isBlank()) {
                    continue;
                }

                if (preservedTitle != null && !preservedTitle.isBlank() && preservedTitle.equals(title)) {
                    skippedBookCount++;
                    continue;
                }

                if (bookRepository.findByTitle(title).isPresent()) {
                    skippedBookCount++;
                    continue;
                }

                Book book = new Book();
                book.setTitle(title);
                book.setCategoryId(jingCategory.getId());
                book.setAuthor(sanitizeText(bookNode.has("author") ? bookNode.get("author").asText() : "佚名"));
                book.setAuthorUrl(sanitizeText(bookNode.has("author_url") ? bookNode.get("author_url").asText() : ""));
                book.setDynasty(sanitizeText(bookNode.has("dynasty") ? bookNode.get("dynasty").asText() : ""));
                book.setDescription(sanitizeText(bookNode.has("description") ? bookNode.get("description").asText() : ""));
                if (bookNode.has("cover_url")) {
                    book.setCoverUrl(sanitizeText(bookNode.get("cover_url").asText()));
                }
                book = bookRepository.save(book);

                JsonNode chaptersNode = bookNode.get("chapters");
                if (chaptersNode != null && chaptersNode.isArray()) {
                    int order = 1;
                    for (JsonNode chapterNode : chaptersNode) {
                        importChapterNode(book.getId(), chapterNode, order++);
                        createdChapterCount++;
                    }
                }
                createdBookCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("经部补充导入失败：" + externalJsonFilePath, e);
        }

        System.out.println("经部补充导入完成：新增书籍 " + createdBookCount +
                "，跳过（已存在/保留）" + skippedBookCount +
                "，新增章节 " + createdChapterCount);
    }

    @Transactional
    public void replaceBookFromFlatPoemFile(String externalJsonFilePath,
                                            String targetBookTitle,
                                            String categoryName,
                                            String defaultAuthor,
                                            String defaultDynasty) {
        if (externalJsonFilePath == null || externalJsonFilePath.isBlank()) {
            System.out.println("跳过导入：externalJsonFilePath 为空 -> " + targetBookTitle);
            return;
        }

        Path path = Path.of(externalJsonFilePath);
        if (!Files.exists(path)) {
            System.out.println("跳过导入：文件不存在 -> " + externalJsonFilePath);
            return;
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            List<JsonNode> items = objectMapper.readValue(inputStream, new TypeReference<List<JsonNode>>() {});
            if (items.isEmpty()) {
                System.out.println("跳过导入：文件为空 -> " + externalJsonFilePath);
                return;
            }

            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCat = new Category();
                        newCat.setName(categoryName);
                        return categoryRepository.save(newCat);
                    });

            Book book = bookRepository.findByTitle(targetBookTitle).orElseGet(Book::new);
            book.setTitle(targetBookTitle);
            book.setCategoryId(category.getId());
            book.setAuthor(defaultAuthor);
            book.setDynasty(defaultDynasty);
            book.setDescription(targetBookTitle + "（外部增强版，含拼音、注释、译文等）");
            book = bookRepository.save(book);

            chapterRepository.deleteByBookId(book.getId());

            int order = 1;
            for (JsonNode item : items) {
                String title = item.has("title") ? item.get("title").asText() : ("第" + order + "篇");
                Chapter chapter = new Chapter();
                chapter.setBookId(book.getId());
                chapter.setTitle(title);
                chapter.setOriginalText(sanitizeText(item.has("original_text") ? item.get("original_text").asText() : ""));
                chapter.setContent(sanitizeText(item.has("original_text") ? item.get("original_text").asText() : ""));
                chapter.setTranslation(sanitizeText(item.has("translation") ? item.get("translation").asText() : ""));
                chapter.setAnnotation(sanitizeText(item.has("annotation") ? item.get("annotation").asText() : ""));
                chapter.setAppreciation(sanitizeText(item.has("appreciation") ? item.get("appreciation").asText() : ""));
                chapter.setPinyin(sanitizeText(item.has("pinyin") ? item.get("pinyin").asText() : ""));
                chapter.setOrderNum(order);
                chapter.setSortOrder(order);
                chapterRepository.save(chapter);
                order++;
            }

            System.out.println("替换导入完成：" + targetBookTitle + "，章节数 = " + (order - 1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("替换导入失败：" + targetBookTitle + " <- " + externalJsonFilePath, e);
        }
    }

    private void ensureChapterUtf8mb4() {
        try (Connection conn = dataSource.getConnection()) {
            String charsetSql = """
                    SELECT CHARACTER_SET_NAME
                    FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME = 'chapter'
                      AND COLUMN_NAME = 'translation'
                    """;

            String charsetName = null;
            try (var ps = conn.prepareStatement(charsetSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    charsetName = rs.getString(1);
                }
            }

            // 打印会话字符集：用于定位是否 MySQL 端真正接收到了 utf8mb4 编码
            try (var st = conn.createStatement();
                 var rs = st.executeQuery("SELECT @@character_set_client, @@character_set_connection, @@character_set_results")) {
                if (rs.next()) {
                    String clientCs = rs.getString(1);
                    String connCs = rs.getString(2);
                    String resultsCs = rs.getString(3);
                    System.out.println("MySQL charset check: translation.charset=" + charsetName +
                            ", session(client/connection/results)=(" + clientCs + "/" + connCs + "/" + resultsCs + ")");
                }
            }

            if ("utf8mb4".equalsIgnoreCase(charsetName)) {
                return; // 已是目标字符集
            }

            System.out.println("将 chapter 表转换为 utf8mb4（当前 translation 字符集=" + charsetName + "）");
            try (var st = conn.createStatement()) {
                st.execute("ALTER TABLE chapter CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }
        } catch (Exception e) {
            throw new RuntimeException("ensureChapterUtf8mb4 失败：请检查 MySQL 字符集/权限", e);
        }
    }

    private boolean isShiBook(JsonNode bookNode) {
        if (bookNode == null) return false;
        // 数据里一般会有 category 字段：如“史部编年类”
        if (bookNode.has("category")) {
            String cat = bookNode.get("category").asText("");
            return cat.contains("史");
        }
        // 兜底：如果没有 category，就用 category_id（示例里史部通常是 100）
        if (bookNode.has("category_id")) {
            return bookNode.get("category_id").asLong() == 100L;
        }
        return false;
    }

    private Book upsertBookWithExplicitCategory(JsonNode bookNode, Long forcedCategoryId) {
        String title = bookNode.has("title") ? bookNode.get("title").asText() : "";
        if (title.isBlank()) {
            throw new RuntimeException("书籍 title 为空，无法导入");
        }

        Book book = bookRepository.findByTitle(title).orElseGet(() -> new Book());
        book.setTitle(title);

        if (bookNode.has("author")) book.setAuthor(sanitizeText(bookNode.get("author").asText()));
        if (bookNode.has("author_url")) book.setAuthorUrl(bookNode.get("author_url").asText());
        if (bookNode.has("cover_url")) book.setCoverUrl(bookNode.get("cover_url").asText());
        if (bookNode.has("description")) book.setDescription(sanitizeText(bookNode.get("description").asText()));
        if (bookNode.has("dynasty")) book.setDynasty(sanitizeText(bookNode.get("dynasty").asText()));
        book.setCategoryId(forcedCategoryId); // 关键：把史部统一映射到数据库“史”分类

        return bookRepository.save(book);
    }

    private InputStream getClassPathInputStream(String classpathRelativePath) throws Exception {
        // 避免在 importFromJson 里重复写 ClassPathResource
        org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(classpathRelativePath);
        return resource.getInputStream();
    }

    private void importLegacyChapterList(List<JsonNode> items) {
        JsonNode first = items.get(0);
        String bookName = first.has("book") ? first.get("book").asText() : "Unknown Book";
        String categoryName = first.has("category") ? first.get("category").asText() : "Unknown Category";

        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(categoryName);
                    return categoryRepository.save(newCat);
                });

        Book book = bookRepository.findByTitle(bookName)
                .orElseGet(() -> {
                    Book newBook = new Book();
                    newBook.setTitle(bookName);
                    if (first.has("author")) newBook.setAuthor(first.get("author").asText());
                    if (first.has("author_url")) newBook.setAuthorUrl(first.get("author_url").asText());
                    if (first.has("cover_url")) newBook.setCoverUrl(first.get("cover_url").asText());
                    newBook.setCategoryId(category.getId());
                    if (first.has("description")) newBook.setDescription(first.get("description").asText());
                    if (first.has("dynasty")) newBook.setDynasty(first.get("dynasty").asText());
                    return bookRepository.save(newBook);
                });

        int order = 1;
        for (JsonNode item : items) {
            importChapterNode(book.getId(), item, order++);
        }
    }

    private void importBookWithNestedChapters(List<JsonNode> books) {
        for (JsonNode bookNode : books) {
            Book book = upsertBook(bookNode);
            JsonNode chaptersNode = bookNode.get("chapters");
            if (chaptersNode == null || !chaptersNode.isArray()) {
                continue;
            }

            int order = 1;
            Iterator<JsonNode> iterator = chaptersNode.elements();
            while (iterator.hasNext()) {
                JsonNode chapterNode = iterator.next();
                importChapterNode(book.getId(), chapterNode, order++);
            }
        }
    }

    private Book upsertBook(JsonNode bookNode) {
        String title = bookNode.has("title") ? bookNode.get("title").asText() : "Unknown Book";
        Long categoryId = bookNode.has("category_id") ? bookNode.get("category_id").asLong() : null;

        // upsert：存在就更新字段，避免多次导入后数据不完整
        Book book = bookRepository.findByTitle(title).orElseGet(Book::new);
        book.setTitle(title);
        if (bookNode.has("author")) book.setAuthor(bookNode.get("author").asText());
        if (bookNode.has("author_url")) book.setAuthorUrl(bookNode.get("author_url").asText());
        if (bookNode.has("cover_url")) book.setCoverUrl(bookNode.get("cover_url").asText());
        if (bookNode.has("description")) book.setDescription(bookNode.get("description").asText());
        if (bookNode.has("dynasty")) book.setDynasty(bookNode.get("dynasty").asText());
        if (categoryId != null) book.setCategoryId(categoryId);
        return bookRepository.save(book);
    }

    private void importChapterNode(Long bookId, JsonNode chapterNode, int order) {
        String title = chapterNode.has("title") ? chapterNode.get("title").asText() : "Unknown Title";
        String content = sanitizeText(chapterNode.has("content") ? chapterNode.get("content").asText() : "");
        String annotation = sanitizeText(chapterNode.has("annotation") ? chapterNode.get("annotation").asText() : "");
        String originalText = sanitizeText(chapterNode.has("original_text") ? chapterNode.get("original_text").asText() : "");
        String translation = sanitizeText(chapterNode.has("translation") ? chapterNode.get("translation").asText() : "");
        String appreciation = sanitizeText(chapterNode.has("appreciation") ? chapterNode.get("appreciation").asText() : "");
        String pinyin = sanitizeText(chapterNode.has("pinyin") ? chapterNode.get("pinyin").asText() : "");

        // JSON 里如果有 sort_order/order_num，就优先使用；否则用顺序号填充
        int sortOrder = chapterNode.has("sort_order") ? chapterNode.get("sort_order").asInt() : order;
        int orderNum = chapterNode.has("order_num") ? chapterNode.get("order_num").asInt() : order;

        Chapter chapter = chapterRepository.findByBookIdAndTitle(bookId, title).orElseGet(() -> {
            Chapter c = new Chapter();
            c.setBookId(bookId);
            return c;
        });
        chapter.setTitle(title);
        chapter.setContent(content);
        chapter.setOriginalText(originalText);
        chapter.setTranslation(translation);
        chapter.setAnnotation(annotation);
        chapter.setAppreciation(appreciation);
        chapter.setPinyin(pinyin);
        chapter.setOrderNum(orderNum);
        chapter.setSortOrder(sortOrder);
        chapterRepository.save(chapter);
    }

    /**
     * MySQL 在某些字符集/严格模式下会对不可见控制字符直接报 "Incorrect string value"。
     * 这里移除 \u0000 以及除 \r\n\t 之外的所有 < 0x20 控制字符，保证写库尽可能成功。
     */
    private String sanitizeText(String s) {
        if (s == null || s.isEmpty()) return s == null ? null : "";

        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            // null char
            if (ch == '\u0000') continue;

            // control chars except newline/carriage return/tab
            if (ch < 0x20 && ch != '\n' && ch != '\r' && ch != '\t') {
                continue;
            }

            sb.append(ch);
        }
        return sb.toString();
    }
}
<template>
  <div class="book-list">
    <BreadcrumbNav :items="breadcrumbItems" />
    <h1>典籍库</h1>

    <div class="search-section">
      <div class="search-bar">
        <span class="search-icon">&#128269;</span>
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="搜索书名、作者..."
          @keyup.enter="handleSearch"
        />
        <button
          v-if="searchKeyword"
          class="search-clear"
          @click="clearSearch"
        >
          &#10005;
        </button>
        <button class="search-btn" @click="handleSearch">搜索</button>
      </div>
      <div v-if="isSearching" class="search-info">
        搜索"{{ searchKeyword }}"的结果（共 {{ books.length }} 条）
      </div>
    </div>

    <div class="filter-section">
      <select v-model="selectedCategory" @change="fetchBooks">
        <option value="">全部分类</option>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.name }}
        </option>
      </select>
    </div>
    <div class="books" v-if="loading">
      <div class="book-card" v-for="n in 6" :key="n">
        <el-skeleton :rows="3" animated />
      </div>
    </div>
    <div class="books" v-else-if="books.length > 0">
      <div
          v-for="book in books"
          :key="book.id"
          class="book-card"
          @click="goToDetail(book.id)"
      >
        <img
            :src="getBookCover(book)"
            :alt="book.title"
            loading="eager"
        />
        <h3>{{ book.title }}</h3>
        <p>{{ book.author || '佚名' }}</p>
        <div class="book-footer">
          <span class="view-link">查看典籍 →</span>
        </div>
      </div>
    </div>
    <div v-else class="no-books">
      {{ isSearching ? '未找到相关典籍，请尝试其他关键词' : '暂无典籍' }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '../utils/request'
import { generateSvgCover } from '../utils/bookCover'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'
import { ElMessage } from 'element-plus'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '典籍库' }
]

const books = ref([])
const categories = ref([])
const selectedCategory = ref('')
const searchKeyword = ref('')
const isSearching = ref(false)
const loading = ref(true)
const router = useRouter()
const route = useRoute()
const coverCache = new Map()

// 获取所有分类
const fetchCategories = async () => {
  try {
    const response = await request.get('/categories')
    categories.value = response.data
    console.log('分类加载成功:', categories.value)
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

// 获取书籍列表
const fetchBooks = async () => {
  loading.value = true
  try {
    let url = '/books'
    if (selectedCategory.value) {
      url = `/books/byCategory?categoryId=${selectedCategory.value}`
    }
    const response = await request.get(url)
    books.value = response.data
  } catch (error) {
    console.error('获取书籍失败', error)
    ElMessage.error('获取典籍列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索书籍
const searchBooks = async () => {
  const keyword = searchKeyword.value.trim()
  if (!keyword) {
    clearSearch()
    return
  }
  isSearching.value = true
  selectedCategory.value = ''
  try {
    const response = await request.get('/books/search', {
      params: { keyword }
    })
    books.value = response.data
  } catch (error) {
    console.error('搜索失败', error)
    ElMessage.error('搜索失败，请稍后重试')
    books.value = []
  }
}

const handleSearch = () => {
  searchBooks()
  router.replace({ query: { keyword: searchKeyword.value.trim() || undefined } })
}

const clearSearch = () => {
  searchKeyword.value = ''
  isSearching.value = false
  router.replace({ query: { keyword: undefined } })
  fetchBooks()
}

// 获取书籍分类名称
const getCategoryName = (categoryId) => {
  const category = categories.value.find(c => c.id === categoryId)
  return category?.name || ''
}

// 获取书籍封面
const getBookCover = (book) => {
  const cacheKey = `${book.id}_${book.title}`
  if (coverCache.has(cacheKey)) {
    return coverCache.get(cacheKey)
  }

  const categoryName = getCategoryName(book.categoryId)
  console.log(`生成封面: ${book.title}, 分类: ${categoryName}`)

  const cover = generateSvgCover(book.title, book.author, categoryName)
  coverCache.set(cacheKey, cover)
  return cover
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/books/${id}`)
}

// 监听路由参数
watch(() => route.query.categoryId, (newCategoryId) => {
  if (newCategoryId && !isSearching.value) {
    selectedCategory.value = newCategoryId
    fetchBooks()
  }
}, { immediate: true })

onMounted(async () => {
  await fetchCategories()
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword
    await searchBooks()
  } else if (route.query.categoryId) {
    selectedCategory.value = route.query.categoryId
    await fetchBooks()
  } else {
    await fetchBooks()
  }
})
</script>

<style scoped>
.book-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.book-list h1 {
  text-align: center;
  color: #3e2e1f;
  margin-bottom: 30px;
  font-size: 2rem;
}

.search-section {
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
  background: #fff;
  border: 2px solid #e3d5c3;
  border-radius: 50px;
  padding: 4px 4px 4px 18px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.search-bar:focus-within {
  border-color: #b89a6e;
  box-shadow: 0 2px 12px rgba(184, 154, 110, 0.2);
}

.search-icon {
  color: #b89a6e;
  font-size: 1.1rem;
  margin-right: 8px;
  line-height: 1;
}

.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 15px;
  padding: 10px 0;
  background: transparent;
  color: #3e2e1f;
  font-family: inherit;
}

.search-bar input::placeholder {
  color: #b8a58e;
}

.search-clear {
  background: none;
  border: none;
  color: #b8a58e;
  cursor: pointer;
  padding: 6px 10px;
  font-size: 14px;
  line-height: 1;
  border-radius: 50%;
  transition: all 0.2s;
}

.search-clear:hover {
  background: #f0e8dd;
  color: #7a6856;
}

.search-btn {
  background: #b89a6e;
  color: #fff;
  border: none;
  border-radius: 50px;
  padding: 8px 22px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s;
  font-family: inherit;
  font-weight: 500;
  letter-spacing: 1px;
  margin-left: 4px;
}

.search-btn:hover {
  background: #8b6b42;
}

.search-info {
  text-align: center;
  color: #8b7355;
  font-size: 14px;
  margin-top: 12px;
  font-style: italic;
}

.filter-section {
  margin-bottom: 30px;
  text-align: center;
}

.filter-section select {
  padding: 10px 20px;
  border: 1px solid #e3d5c3;
  border-radius: 8px;
  font-size: 16px;
  background-color: white;
  cursor: pointer;
  color: #3e2e1f;
  font-weight: 500;
  transition: all 0.3s ease;
}

.filter-section select:hover {
  border-color: #c9b28b;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.books {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
}

.book-card {
  cursor: pointer;
  border: 1px solid #e3d5c3;
  padding: 15px;
  border-radius: 12px;
  transition: all 0.3s ease;
  background-color: #fffbf5;
  text-align: center;
  position: relative;
}

.book-card:hover {
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
  transform: translateY(-5px);
  border-color: #c9b28b;
}

.book-card img {
  width: 100%;
  height: 250px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  margin-bottom: 12px;
  background-color: #f5f5f5;
}

.book-card h3 {
  margin: 12px 0 8px;
  font-size: 1.2rem;
  color: #3e2e1f;
  font-weight: 600;
}

.book-card p {
  margin: 0 0 12px;
  color: #7a6856;
  font-size: 0.9rem;
}

.book-footer {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid #f0e8dd;
}

.view-link {
  color: #b89a6e;
  font-size: 0.85rem;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-block;
}

.book-card:hover .view-link {
  color: #8b6b42;
  transform: translateX(4px);
}

.no-books {
  text-align: center;
  color: #a28b72;
  padding: 60px 0;
  font-style: italic;
  font-size: 1.2rem;
  background: #faf9f7;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .book-list {
    padding: 15px;
  }

  .books {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
  }

  .book-card img {
    height: 180px;
  }

  .book-card h3 {
    font-size: 1rem;
  }
}
</style>
<template>
  <div class="home">
    <!-- 头部横幅区 -->
    <section class="hero">
      <h1 class="site-title">经典重读·AI 伴学</h1>
      <p class="site-subtitle">让经典更容易读、更容易懂、更容易传播</p>
      <router-link to="/ai" class="ai-entry-btn">🤖 AI 助手</router-link>
    </section>

    <!-- 典籍分类区 -->
    <section class="categories">
      <h2 class="section-title">典籍分类</h2>
      <div class="category-grid" v-if="categories.length > 0">
        <div class="category-card" v-for="category in categories" :key="category.id">
          <h3 class="category-name">{{ category.name }}</h3>
          <p class="category-desc">{{ category.description || '' }}</p>
          <div class="book-list">
            <div class="book-item" v-for="book in getBooksByCategory(category.id)" :key="book.id" @click="goToBookDetail(book.id)">
              <img :src="getBookCover(book, category)" :alt="book.title" class="book-cover" />
              <div class="book-info">
                <h4>{{ book.title }}</h4>
                <p>{{ book.author || '佚名' }}</p>
              </div>
            </div>
            <div v-if="getBooksByCategory(category.id).length === 0" class="no-books">
              暂无典籍
            </div>
          </div>
          <router-link :to="{ path: '/books', query: { categoryId: category.id } }" class="view-more">查看典籍 →</router-link>
        </div>
      </div>
      <div v-else>
        <el-skeleton :rows="3" animated />
      </div>
    </section>

    <!-- 主题专栏标题 + 轮转图（内嵌版） -->
    <section class="theme-section">
      <h2 class="section-title">主题专栏</h2>
      <div class="inline-carousel" @mouseenter="pauseCarousel" @mouseleave="startCarousel" @touchstart="onTouchStart" @touchend="onTouchEnd">
        <div class="carousel-wrapper">
          <div class="carousel-slides" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
            <div
                v-for="(item, index) in carouselItems"
                :key="index"
                class="carousel-slide"
                @click="goToTheme(item.link)"
            >
              <img :src="item.image" :alt="item.title" />
              <div class="slide-caption">
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </div>
          <button class="carousel-btn prev" @click="prevSlide">❮</button>
          <button class="carousel-btn next" @click="nextSlide">❯</button>
        </div>
        <div class="carousel-dots">
          <span
              v-for="(item, index) in carouselItems"
              :key="index"
              :class="{ active: index === currentSlide }"
              @click="goToSlide(index)"
          ></span>
        </div>
      </div>
    </section>

    <!-- 最新讨论区 -->
    <section class="latest-discussions">
      <h2 class="section-title">最新讨论</h2>
      <el-skeleton :rows="3" animated v-if="discussionsLoading && latestConversations.length === 0" />
      <div class="discussion-list" v-else-if="latestConversations.length > 0">
        <div class="discussion-item" v-for="conv in latestConversations" :key="conv.id" @click="goToConversation(conv.id)">
          <div class="discussion-header">
            <span class="user">{{ conv.creatorName }}</span>
            <span class="time">{{ formatTime(conv.createdAt) }}</span>
          </div>
          <p class="discussion-title">{{ conv.title }}</p>
          <p class="discussion-preview">{{ conv.content ? conv.content.substring(0, 30) + '...' : '暂无内容' }}</p>
        </div>
      </div>
      <p v-else class="no-discussion">暂无讨论，快去发布第一条吧！</p>
      <p class="more" @click="goToCommunity">点击查看更多讨论 ></p>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { generateSvgCover } from '../utils/bookCover'
import sportsImg from '@/assets/carousel/sports_etiquette.jpg'
import civilizationImg from '@/assets/carousel/civilization_exchange.jpg'
import familyImg from '@/assets/carousel/family_education.jpg'

const router = useRouter()

// 从后端获取的数据
const categories = ref([])
const books = ref([])
const coverCache = new Map()

// 获取所有分类
const fetchCategories = async () => {
  try {
    const response = await request.get('/categories')
    categories.value = response.data
  } catch (error) {
    console.error('获取分类失败', error)
  }
}

// 获取所有书籍
const fetchBooks = async () => {
  try {
    const response = await request.get('/books')
    books.value = response.data
  } catch (error) {
    console.error('获取书籍失败', error)
  }
}

// 根据分类ID获取书籍
const getBooksByCategory = (categoryId) => {
  return books.value.filter(book => book.categoryId === categoryId).slice(0, 2) // 只显示前2本
}

// 获取书籍封面
const getBookCover = (book, category) => {
  const cacheKey = `${book.id}_${book.title}`
  if (coverCache.has(cacheKey)) {
    return coverCache.get(cacheKey)
  }

  const cover = generateSvgCover(book.title, book.author, category?.name || '')
  coverCache.set(cacheKey, cover)
  return cover
}

// 最新讨论数据
const latestConversations = ref([])
const discussionsLoading = ref(true)

// 轮转图数据
const carouselItems = ref([
  {
    image: sportsImg,
    link: '/theme/sports-virtue',
    title: '翰墨传家·体育古韵',
    desc: '挖掘古籍中的体育精神，弘扬传统美德'
  },
  {
    image: civilizationImg,
    link: '/theme/civilization-exchange',
    title: '文明互鉴·黄河西行',
    desc: '展现黄河文明与世界文明的交流互鉴'
  },
  {
    image: familyImg,
    link: '/theme/family-education',
    title: '童蒙养正·诗礼传家',
    desc: '聚焦家风家训与儿童启蒙'
  }
])

// 轮转图相关
const currentSlide = ref(0)
let carouselTimer = null

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % carouselItems.value.length
}
const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + carouselItems.value.length) % carouselItems.value.length
}
const goToSlide = (index) => {
  currentSlide.value = index
}
const goToTheme = (link) => {
  router.push(link)
}
const startCarousel = () => {
  if (carouselTimer) clearInterval(carouselTimer)
  carouselTimer = setInterval(nextSlide, 4000)
}
const pauseCarousel = () => {
  if (carouselTimer) clearInterval(carouselTimer)
  carouselTimer = null
}

// 获取最新讨论
const fetchLatestDiscussions = async () => {
  try {
    const response = await request.get('/community/conversations', {
      params: { page: 0, size: 5 }
    })
    latestConversations.value = response.data.content || []
  } catch (error) {
    console.error('获取最新讨论失败', error)
  } finally {
    discussionsLoading.value = false
  }
}

const goToCommunity = () => router.push('/community')
const goToBookDetail = (id) => router.push(`/books/${id}`)
const goToConversation = (id) => router.push(`/community/conversation/${id}`)

// 轮播触摸滑动
const touchStartX = ref(0)
const onTouchStart = (e) => {
  touchStartX.value = e.touches[0].clientX
}
const onTouchEnd = (e) => {
  const diff = touchStartX.value - e.changedTouches[0].clientX
  if (Math.abs(diff) > 50) {
    diff > 0 ? nextSlide() : prevSlide()
  }
}

const formatTime = (datetime) => {
  if (!datetime) return ''
  const date = new Date(datetime)
  const now = new Date()
  const diff = (now - date) / 1000
  if (diff < 60) return '刚刚'
  if (diff < 3600) return `${Math.floor(diff / 60)}分钟前`
  if (diff < 86400) return `${Math.floor(diff / 3600)}小时前`
  return `${Math.floor(diff / 86400)}天前`
}

onMounted(async () => {
  await fetchCategories()
  await fetchBooks()
  fetchLatestDiscussions()
  startCarousel()
})
onBeforeUnmount(() => {
  if (carouselTimer) clearInterval(carouselTimer)
})
</script>

<style scoped>
/* 原有样式保持不变，只需添加轮转图相关样式（见下方） */
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
}

/* 头部区域 */
.hero {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem 0;
  background: linear-gradient(to bottom, rgba(244, 235, 220, 0.8), rgba(249, 243, 233, 0.9));
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.site-title {
  font-size: 3rem;
  font-weight: 700;
  color: #5a3e2b;
  margin-bottom: 0.5rem;
  letter-spacing: 2px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.site-subtitle {
  font-size: 1.3rem;
  color: #7d5f47;
  margin-bottom: 1.5rem;
  font-style: italic;
}

.ai-entry-btn {
  display: inline-block;
  background-color: #8b6b4d;
  color: #fef7e9;
  padding: 0.8rem 2rem;
  border-radius: 40px;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: 500;
  transition: background-color 0.3s, transform 0.2s;
  box-shadow: 0 4px 8px rgba(139, 107, 77, 0.3);
}

.ai-entry-btn:hover {
  background-color: #6f523b;
  transform: translateY(-2px);
}

/* 通用章节标题 */
.section-title {
  font-size: 2.2rem;
  font-weight: 600;
  color: #4a3727;
  position: relative;
  margin-bottom: 2rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #c9ad8e;
}

/* 典籍分类区 */
.categories {
  margin-bottom: 3rem;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.category-card {
  background: #fffbf5;
  border: 1px solid #e3d5c3;
  border-radius: 16px;
  padding: 1.8rem;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.3s, transform 0.3s;
}

.category-card:hover {
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.1);
  transform: translateY(-6px);
}

.category-name {
  font-size: 2.2rem;
  font-weight: 700;
  color: #6f4f2f;
  margin: 0 0 0.3rem;
}

.category-desc {
  font-size: 1.1rem;
  color: #a28b72;
  margin-bottom: 1.5rem;
  font-style: italic;
}

.book-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.book-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.8rem;
  border-radius: 12px;
  background-color: #fcf8f2;
  transition: all 0.3s ease;
  cursor: pointer;
}

.book-item:hover {
  background-color: #f5ebe0;
  transform: scale(1.03);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.book-cover {
  width: 70px;
  height: 90px;
  object-fit: cover;
  border-radius: 6px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #f5f5f5;
}

.book-info h4 {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0 0 0.2rem;
  color: #3e2e1f;
}

.book-info p {
  font-size: 1rem;
  color: #7a6856;
  margin: 0;
}

.no-books {
  text-align: center;
  color: #a28b72;
  padding: 1rem 0;
  font-style: italic;
}

.loading {
  text-align: center;
  color: #8b6b4d;
  padding: 2rem 0;
  font-size: 1.2rem;
}

.view-more {
  display: inline-block;
  margin-top: 0.8rem;
  color: #8b6b4d;
  text-decoration: none;
  font-weight: 500;
  font-size: 1.1rem;
  transition: color 0.2s;
}

.view-more:hover {
  color: #5a402b;
  text-decoration: underline;
}

/* 主题专栏区域（轮转图） */
.theme-section {
  margin-bottom: 3rem;
}

/* 内嵌轮转图样式 */
.inline-carousel {
  position: relative;
  width: 100%;
  margin: 2rem 0;
  overflow: hidden;
}
.carousel-wrapper {
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
}
.carousel-slides {
  display: flex;
  transition: transform 0.5s ease-in-out;
}
.carousel-slide {
  flex: 0 0 100%;
  position: relative;
  cursor: pointer;
}
.carousel-slide img {
  width: 100%;
  height: 400px;
  object-fit: contain;
  background-color: #2c2c2c;
  display: block;
}
.slide-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(0deg, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0) 100%);
  color: white;
  padding: 20px;
  text-align: left;
}
.slide-caption h3 {
  font-size: 24px;
  margin-bottom: 8px;
}
.slide-caption p {
  font-size: 14px;
  opacity: 0.9;
}
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0,0,0,0.5);
  color: white;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 20px;
  z-index: 2;
  transition: background 0.3s;
}
.carousel-btn:hover {
  background: rgba(0,0,0,0.8);
}
.carousel-btn.prev {
  left: 20px;
}
.carousel-btn.next {
  right: 20px;
}
.carousel-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 12px;
  z-index: 2;
}
.carousel-dots span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255,255,255,0.6);
  cursor: pointer;
  transition: all 0.3s;
}
.carousel-dots span.active {
  background: #ffd966;
  width: 24px;
  border-radius: 6px;
}

/* 最新讨论区 */
.latest-discussions {
  background-color: #fcf8f2;
  border-radius: 20px;
  padding: 1.5rem 2rem;
  border: 1px solid #e9dccc;
  box-shadow: 0 8px 18px rgba(0, 0, 0, 0.03);
  margin-top: 2rem;
}

.latest-discussions .section-title {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}

.discussion-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
}

.discussion-item {
  border-right: 1px dashed #dacbb8;
  padding-right: 1.5rem;
}

.discussion-item:last-child {
  border-right: none;
}

.discussion-item {
  cursor: pointer;
}

.discussion-header {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: #8f7b67;
  margin-bottom: 0.4rem;
}

.user {
  font-weight: 600;
  color: #6f4f2f;
}

.time {
  font-style: italic;
}

.discussion-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #3e2e1f;
  margin: 0.2rem 0;
}

.discussion-preview {
  font-size: 0.95rem;
  color: #675544;
  line-height: 1.5;
}

.no-discussion {
  text-align: center;
  color: #a28b72;
  padding: 2rem 0;
}

.more {
  text-align: right;
  color: #007bff;
  margin-top: 1rem;
  cursor: pointer;
}

.more:hover {
  text-decoration: underline;
}

/* 响应式调整 */
@media (max-width: 900px) {
  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .discussion-list {
    grid-template-columns: 1fr;
  }
  .discussion-item {
    border-right: none;
    border-bottom: 1px dashed #dacbb8;
    padding-bottom: 1rem;
  }
  .carousel-slide img {
    height: 300px;
  }
}

@media (max-width: 600px) {
  .category-grid {
    grid-template-columns: 1fr;
  }
  .carousel-slide img {
    height: 250px;
  }
}
</style>
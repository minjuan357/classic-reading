<template>
  <div class="book-detail">
    <BreadcrumbNav :items="breadcrumbItems" />
    <div class="container">
      <el-skeleton :rows="6" animated v-if="detailLoading" style="padding: 40px 20px" />
      <template v-else>
      <div class="row">
        <div class="col-md-4">
          <img :src="displayCover" class="img-fluid rounded book-cover" :alt="book.title" style="cursor: pointer" @click="showImagePreview = true">
          <div v-if="showImagePreview" class="image-preview-overlay" @click="showImagePreview = false">
            <img :src="displayCover" :alt="book.title" class="image-preview-full">
          </div>
        </div>
        <div class="col-md-8">
          <div class="book-header">
            <h1 class="book-title">{{ book.title }}</h1>
            <button
                v-if="userStore.isLoggedIn"
                class="btn-favorite"
                :class="isFavorite ? 'favorited' : ''"
                @click="toggleFavorite"
                :disabled="favoriteLoading"
            >
              <span v-if="favoriteLoading">处理中...</span>
              <span v-else>{{ isFavorite ? '★ 已收藏' : '☆ 收藏' }}</span>
            </button>
          </div>
          <p class="book-meta"><strong>作者：</strong>{{ book.author || '佚名' }} · {{ book.dynasty || '未知朝代' }}</p>
          <p class="book-description">{{ book.description || '暂无简介' }}</p>
          <button
              v-if="chapters.length > 0"
              @click="goToFirstChapter"
              class="btn-read"
          >
            📖 开始阅读
          </button>
          <hr class="divider">
          <h3 class="section-title">篇章目录</h3>
          <div v-if="chapters.length > 0">
            <div class="chapters-grid" :class="{ collapsed: !chaptersExpanded && chapters.length > 8 }">
              <div
                  v-for="chapter in chaptersExpanded || chapters.length <= 8 ? chapters : chapters.slice(0, 8)"
                  :key="chapter.id"
                  class="chapter-item"
                  @click="goToChapter(chapter.id)"
              >
                <span class="chapter-title">{{ chapter.title }}</span>
                <span class="chapter-order">第{{ chapter.orderNum || chapter.id }}章</span>
              </div>
            </div>
            <div v-if="chapters.length > 8" class="expand-toggle">
              <button class="expand-btn" @click="chaptersExpanded = !chaptersExpanded">
                {{ chaptersExpanded ? '收起章节 ▲' : `展开全部章节（共 ${chapters.length} 章）▼` }}
              </button>
            </div>
          </div>
          <div v-else class="no-chapters">
            <p>暂无章节内容</p>
          </div>
        </div>
      </div>
      </template>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { useUserStore } from '../stores/user'
import { generateSvgCover } from '../utils/bookCover'
import { ElMessage } from 'element-plus'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

export default {
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    const book = ref({})
    const chapters = ref([])
    const isFavorite = ref(false)
    const favoriteLoading = ref(false)
    const displayCover = ref('')
    const breadcrumbItems = ref([
      { label: '首页', path: '/' },
      { label: '典籍库', path: '/books' },
      { label: '加载中...' }
    ])
    const chaptersExpanded = ref(false)
    const detailLoading = ref(true)
    const showImagePreview = ref(false)

    // 获取分类名称
    const getCategoryName = async (categoryId) => {
      try {
        const res = await request.get(`/categories/${categoryId}`)
        return res.data?.name || ''
      } catch (error) {
        console.error('获取分类失败', error)
        return ''
      }
    }

    // 生成封面（忽略数据库的 cover_url）
    const generateCover = async (bookData) => {
      if (!bookData || !bookData.title) return ''

      // 获取分类名称
      let categoryName = ''
      if (bookData.categoryId) {
        try {
          const res = await request.get(`/categories/${bookData.categoryId}`)
          categoryName = res.data?.name || ''
        } catch (error) {
          console.error('获取分类失败', error)
        }
      }

      // 直接生成 SVG 封面，不使用 cover_url
      const cover = generateSvgCover(bookData.title, bookData.author || '佚名', categoryName)
      console.log('生成封面:', bookData.title, categoryName)
      return cover
    }

    const goToFirstChapter = () => {
      if (chapters.value.length > 0) {
        router.push(`/chapters/${chapters.value[0].id}`)
      }
    }

    const goToChapter = (chapterId) => {
      router.push(`/chapters/${chapterId}`)
    }

    const checkFavoriteStatus = async () => {
      if (!userStore.isLoggedIn || !book.value.id) return
      try {
        const res = await request.get('/user/favorites/check', { params: { bookId: book.value.id } })
        isFavorite.value = res.data.isFavorite
      } catch (error) {
        console.error('检查收藏状态失败', error)
      }
    }

    const toggleFavorite = async () => {
      if (!userStore.isLoggedIn) {
        router.push('/login')
        return
      }
      favoriteLoading.value = true
      try {
        if (isFavorite.value) {
          await request.delete('/user/favorites', { params: { bookId: book.value.id } })
          isFavorite.value = false
        } else {
          await request.post('/user/favorites', { bookId: book.value.id })
          isFavorite.value = true
        }
      } catch (error) {
        console.error('操作收藏失败', error)
        ElMessage.error(error.response?.data?.message || '操作失败')
      } finally {
        favoriteLoading.value = false
      }
    }

    onMounted(async () => {
      const bookId = route.params.id
      try {
        const bookRes = await request.get(`/books/${bookId}`)
        book.value = bookRes.data
        breadcrumbItems.value = [
          { label: '首页', path: '/' },
          { label: '典籍库', path: '/books' },
          { label: book.value.title }
        ]
        // 生成封面（忽略 cover_url）
        displayCover.value = await generateCover(book.value)

        const chapterRes = await request.get(`/books/${bookId}/chapters`, {
          params: { page: 0, size: 9999 }
        })
        const data = chapterRes.data
        chapters.value = Array.isArray(data) ? data : (data?.content || [])

        await checkFavoriteStatus()
      } catch (error) {
        console.error('获取书籍详情失败', error)
        ElMessage.error('获取典籍详情失败')
      } finally {
        detailLoading.value = false
      }
    })

    return {
      book,
      chapters,
      goToFirstChapter,
      goToChapter,
      userStore,
      isFavorite,
      favoriteLoading,
      toggleFavorite,
      displayCover,
      breadcrumbItems,
      chaptersExpanded,
      detailLoading,
      showImagePreview
    }
  }
}
</script>

<style scoped>
.book-detail {
  min-height: calc(100vh - 200px);
  background: linear-gradient(135deg, #faf9f7 0%, #fffbf5 100%);
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.row {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}

.col-md-4 {
  flex: 0 0 calc(33.333% - 40px);
}

.col-md-8 {
  flex: 0 0 calc(66.666% - 40px);
}

@media (max-width: 768px) {
  .col-md-4,
  .col-md-8 {
    flex: 0 0 100%;
  }
}

/* 书籍封面 */
.book-cover {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-radius: 16px;
  background-color: #f5f5f5;
}

/* 书籍头部 */
.book-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 16px;
}

.book-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #5a3e2b;
  margin: 0;
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', serif;
}

/* 收藏按钮 */
.btn-favorite {
  background: none;
  border: 1.5px solid #c9ad8e;
  color: #b89a6e;
  padding: 8px 20px;
  border-radius: 40px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

.btn-favorite:hover {
  background: #f5ebe0;
  border-color: #b89a6e;
  transform: translateY(-1px);
}

.btn-favorite.favorited {
  background: #b89a6e;
  border-color: #b89a6e;
  color: #fff;
}

.btn-favorite.favorited:hover {
  background: #8b6b4d;
  border-color: #8b6b4d;
}

.btn-favorite:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 书籍元信息 */
.book-meta {
  color: #7a6856;
  font-size: 1rem;
  margin-bottom: 16px;
}

.book-meta strong {
  color: #5a3e2b;
}

/* 书籍简介 */
.book-description {
  color: #7a6856;
  line-height: 1.8;
  margin: 16px 0 24px;
  font-size: 1rem;
  text-align: justify;
}

/* ========== 开始阅读按钮 - 古风棕色渐变 ========== */
.btn-read {
  background: linear-gradient(135deg, #8b6b4d, #6f523b);
  border: none;
  padding: 12px 36px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 48px;
  transition: all 0.3s ease;
  color: #fef7e9;
  box-shadow: 0 4px 12px rgba(111, 82, 59, 0.25);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-family: inherit;
  margin-bottom: 24px;
}

.btn-read:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(111, 82, 59, 0.35);
  background: linear-gradient(135deg, #7a5c3e, #5a402b);
}

.btn-read:active {
  transform: translateY(0);
}

/* 分割线 */
.divider {
  border: none;
  height: 2px;
  background: linear-gradient(90deg, #e3d5c3, #c9ad8e, #e3d5c3);
  margin: 24px 0;
}

/* 章节标题 */
.section-title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #5a3e2b;
  margin: 0 0 20px 0;
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', serif;
  position: relative;
  display: inline-block;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #c9ad8e, #b89a6e);
  border-radius: 2px;
}

/* 章节网格布局 */
.chapters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 14px;
  margin-top: 8px;
  padding-right: 8px;
}

.chapters-grid.collapsed {
  max-height: 500px;
  overflow-y: hidden;
  position: relative;
}

.chapters-grid.collapsed::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(transparent, #fffbf5);
  pointer-events: none;
}

.expand-toggle {
  text-align: center;
  margin-top: 12px;
}

.expand-btn {
  background: none;
  border: 1px solid #c9ad8e;
  color: #8b6b4d;
  padding: 8px 24px;
  border-radius: 40px;
  font-size: 0.95rem;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s ease;
}

.expand-btn:hover {
  background: #f5ebe0;
  border-color: #8b6b4d;
}

.chapter-item {
  background: #ffffff;
  border: 1px solid #e3d5c3;
  border-radius: 12px;
  padding: 14px 18px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

.chapter-item:hover {
  background: #fffbf5;
  border-color: #b89a6e;
  transform: translateX(6px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.chapter-title {
  font-size: 15px;
  font-weight: 500;
  color: #3e2e1f;
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', serif;
}

.chapter-order {
  font-size: 12px;
  color: #b89a6e;
  background: #faf9f7;
  padding: 4px 12px;
  border-radius: 20px;
  font-weight: 500;
}

.no-chapters {
  text-align: center;
  padding: 40px 20px;
  background: #ffffff;
  border-radius: 16px;
  margin-top: 16px;
  color: #a28b72;
  border: 1px dashed #e3d5c3;
  font-style: italic;
}

/* 滚动条样式 */
.chapters-grid::-webkit-scrollbar {
  width: 6px;
}

.chapters-grid::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chapters-grid::-webkit-scrollbar-thumb {
  background: #c9b28b;
  border-radius: 3px;
}

.chapters-grid::-webkit-scrollbar-thumb:hover {
  background: #b89a6e;
}

/* 响应式 */
@media (max-width: 768px) {
  .book-detail {
    padding: 24px 0;
  }

  .container {
    padding: 0 16px;
  }

  .book-title {
    font-size: 1.8rem;
  }

  .chapters-grid {
    grid-template-columns: 1fr;
    max-height: 400px;
  }

  .book-cover {
    margin-bottom: 20px;
    max-height: 300px;
  }

  .btn-read {
    padding: 10px 28px;
    font-size: 1rem;
    width: 100%;
    justify-content: center;
  }

  .section-title {
    font-size: 1.4rem;
  }
}

/* 图片预览大图 */
.image-preview-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  cursor: zoom-out;
}

.image-preview-full {
  max-width: 90vw;
  max-height: 90vh;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.5);
}
</style>
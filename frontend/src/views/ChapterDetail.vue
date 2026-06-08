<template>
  <div class="chapter-detail">
    <div class="container">
      <BreadcrumbNav :items="breadcrumbItems" />
      <el-skeleton :rows="6" animated v-if="chaptersLoading" style="padding: 40px 0" />
      <template v-else>
      <!-- 头部导航 -->
      <div class="chapter-header">
        <button class="back-btn" @click="goBack">
          ← 返回书籍
        </button>
        <h1 class="chapter-title">{{ chapter.title || '加载中...' }}</h1>
        <div class="font-controls">
          <button class="font-btn" @click="fontSize -= 1" :disabled="fontSize <= 12">A-</button>
          <span class="font-size">{{ fontSize }}px</span>
          <button class="font-btn" @click="fontSize += 1" :disabled="fontSize >= 24">A+</button>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content-section">
        <!-- 原文区域 -->
        <div class="card original-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">📜</span>
              <h3>原文</h3>
            </div>
          </div>
          <div class="card-content">
            <div class="original-text">
              <p :style="{ fontSize: fontSize + 'px', lineHeight: 1.9 }">
                {{ originalContent }}
              </p>
            </div>
          </div>
        </div>

        <!-- 译文区域 -->
        <div v-if="translationContent" class="card translation-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">📖</span>
              <h3>现代译文</h3>
            </div>
          </div>
          <div class="card-content">
            <div class="translation-text">
              <p :style="{ fontSize: fontSize + 'px', lineHeight: 1.8 }">
                {{ translationContent }}
              </p>
            </div>
          </div>
        </div>

        <!-- 注释区域 -->
        <div v-if="annotationContent" class="card annotation-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">📝</span>
              <h3>注释</h3>
            </div>
          </div>
          <div class="card-content">
            <div class="annotation-text">
              <p :style="{ fontSize: fontSize + 'px', lineHeight: 1.8 }">
                {{ annotationContent }}
              </p>
            </div>
          </div>
        </div>

        <!-- 赏析区域 -->
        <div v-if="appreciationContent" class="card appreciation-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">✨</span>
              <h3>作品赏析</h3>
            </div>
          </div>
          <div class="card-content">
            <div class="appreciation-text">
              <p :style="{ fontSize: fontSize + 'px', lineHeight: 1.8 }">
                {{ appreciationContent }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 上下章导航 -->
      <div class="nav-buttons-bottom">
        <button class="nav-btn prev" @click="goToPrev" :disabled="!prevChapter">
          ← 上一章
        </button>
        <button class="nav-btn next" @click="goToNext" :disabled="!nextChapter">
          下一章 →
        </button>
      </div>

      <!-- 章节信息 -->
      <div class="chapter-info" v-if="allChapters.length > 0">
        <span>第 {{ currentIndex + 1 }} / {{ allChapters.length }} 章</span>
      </div>
      </template>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { useUserStore } from '../stores/user'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

export default {
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    const chapter = ref({})
    const allChapters = ref([])
    const chaptersLoading = ref(true)
    const breadcrumbItems = ref([
      { label: '首页', path: '/' },
      { label: '典籍库', path: '/books' },
      { label: '加载中...' },
      { label: '加载中...' }
    ])
    // 修改初始字体大小为18px
    const fontSize = ref(18)

    // 兼容字段名
    const originalContent = computed(() => {
      return chapter.value.original_text ||
          chapter.value.originalText ||
          chapter.value.content ||
          '暂无原文内容'
    })

    const translationContent = computed(() => {
      return chapter.value.translation ||
          chapter.value.translate ||
          ''
    })

    const annotationContent = computed(() => {
      return chapter.value.annotation ||
          chapter.value.annotate ||
          ''
    })

    const appreciationContent = computed(() => {
      return chapter.value.appreciation ||
          chapter.value.analyze ||
          ''
    })

    const currentIndex = computed(() => {
      if (!chapter.value.id || allChapters.value.length === 0) return -1
      return allChapters.value.findIndex(c => c.id === chapter.value.id)
    })

    const prevChapter = computed(() => {
      if (currentIndex.value > 0 && currentIndex.value !== -1) {
        return allChapters.value[currentIndex.value - 1]
      }
      return null
    })

    const nextChapter = computed(() => {
      if (currentIndex.value < allChapters.value.length - 1 && currentIndex.value !== -1) {
        return allChapters.value[currentIndex.value + 1]
      }
      return null
    })

    const goToPrev = () => {
      if (prevChapter.value) {
        console.log('跳转到上一章:', prevChapter.value.title)
        router.push(`/chapters/${prevChapter.value.id}`)
      }
    }

    const goToNext = () => {
      if (nextChapter.value) {
        console.log('跳转到下一章:', nextChapter.value.title)
        router.push(`/chapters/${nextChapter.value.id}`)
      }
    }

    const goBack = () => {
      if (chapter.value.bookId) {
        router.push(`/books/${chapter.value.bookId}`)
      } else {
        router.push('/books')
      }
    }

    const loadChapter = async (chapterId) => {
      try {
        // 使用正确的API路径：/books/chapters/${chapterId}
        const chapterRes = await request.get(`/books/chapters/${chapterId}`)
        chapter.value = chapterRes.data || {}

        const bookId = chapter.value.bookId
        console.log('章节数据:', chapter.value)
        console.log('书籍ID:', bookId)

        if (bookId) {
          // 获取本书所有章节
          const chaptersRes = await request.get(`/books/${bookId}/chapters`)
          allChapters.value = chaptersRes.data || []

          // 找到当前章节
          const currentChapter = allChapters.value.find(c => c.id == chapterId)
          if (currentChapter) {
            chapter.value = currentChapter
            console.log('当前章节:', currentChapter.title)
            console.log('章节总数:', allChapters.value.length)
            console.log('当前索引:', currentIndex.value)
            console.log('上一章:', prevChapter.value ? prevChapter.value.title : '无')
            console.log('下一章:', nextChapter.value ? nextChapter.value.title : '无')
          }
        }

        const bookTitle = chapter.value.bookTitle || (chapterRes.data && chapterRes.data.bookTitle) || ''
        breadcrumbItems.value = [
          { label: '首页', path: '/' },
          { label: '典籍库', path: '/books' },
          { label: bookTitle || '书籍', path: bookTitle ? `/books/${chapter.value.bookId}` : undefined },
          { label: chapter.value.title || '加载中...' }
        ]

        // 记录浏览历史
        if (userStore.isLoggedIn && bookId) {
          try {
            await request.post('/user/history', {
              chapterId: chapterId,
              bookId: bookId,
              chapterTitle: chapter.value.title,
              bookTitle: chapter.value.bookTitle || ''
            })
          } catch (err) {
            console.warn('记录历史失败', err)
          }
        }

      } catch (error) {
        console.error('获取篇章失败', error)
        ElMessage.error('获取篇章失败，请稍后重试')
      } finally {
        chaptersLoading.value = false
      }
    }

    watch(
        () => route.params.id,
        (newChapterId) => {
          if (newChapterId) loadChapter(newChapterId)
        },
        { immediate: true }
    )

    return {
      chapter,
      originalContent,
      translationContent,
      annotationContent,
      appreciationContent,
      fontSize,
      prevChapter,
      nextChapter,
      currentIndex,
      allChapters,
      goToPrev,
      goToNext,
      goBack,
      breadcrumbItems
    }
  }
}
</script>

<style scoped>
.chapter-detail {
  min-height: calc(100vh - 200px);
  background: linear-gradient(135deg, #f9f3e8 0%, #f0e7db 100%);
  padding: 40px 0;
  font-family: 'SimSun', '宋体', 'Microsoft YaHei', 'Noto Serif SC', serif;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 头部区域 */
.chapter-header {
  margin-bottom: 48px;
  padding-bottom: 24px;
  border-bottom: 2px solid #e3d5c3;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: none;
  color: #b89a6e;
  font-size: 1rem;
  cursor: pointer;
  padding: 8px 0;
  transition: all 0.3s ease;
  font-weight: 500;
  font-family: inherit;
}

.back-btn:hover {
  color: #8b6b4d;
  transform: translateX(-4px);
}

.chapter-title {
  font-size: 2rem;
  font-weight: 700;
  color: #5a3e2b;
  margin: 0;
  text-align: center;
  letter-spacing: 2px;
  flex: 1;
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', 'Noto Serif SC', serif;
  position: relative;
}

.chapter-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, #c9ad8e, #b89a6e);
  border-radius: 2px;
}

/* 字体控制按钮 */
.font-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #ffffff;
  padding: 6px 12px;
  border-radius: 40px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.font-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f5ebe0;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  color: #5a3e2b;
  transition: all 0.3s ease;
  font-family: inherit;
}

.font-btn:hover:not(:disabled) {
  background: #e3d5c3;
}

.font-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.font-size {
  font-size: 0.9rem;
  color: #5a3e2b;
  min-width: 45px;
  text-align: center;
  font-family: inherit;
}

/* 卡片样式 */
.content-section {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.card {
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  border: 1px solid #f0e8dd;
}

.card-header {
  display: flex;
  align-items: center;
  padding: 20px 28px;
  background: linear-gradient(135deg, #fefaf5, #fef7ef);
  border-bottom: 2px solid #e9ddce;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  font-size: 1.6rem;
}

.card-header h3 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #5a3e2b;
  margin: 0;
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', 'Noto Serif SC', serif;
}

.card-content {
  padding: 28px;
}

/* ========== 字体样式设置 ========== */
/* 所有段落通用样式 */
.original-text p,
.translation-text p,
.annotation-text p,
.appreciation-text p {
  margin: 0 0 12px 0;
  text-align: justify;
}

.original-text p:last-child,
.translation-text p:last-child,
.annotation-text p:last-child,
.appreciation-text p:last-child {
  margin-bottom: 0;
}

/* 原文 - 使用楷体，古风典雅 */
.original-text {
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', 'Noto Serif SC', serif;
  color: #3e2e1f;
}

.original-text p {
  font-family: inherit;
  letter-spacing: 0.5px;
  line-height: 1.9;
}

/* 译文 - 使用宋体，清晰易读 */
.translation-text {
  font-family: 'SimSun', '宋体', 'Noto Serif SC', serif;
  color: #7a6856;
}

.translation-text p {
  font-family: inherit;
  line-height: 1.8;
}

/* 注释 - 使用宋体 */
.annotation-text {
  font-family: 'SimSun', '宋体', 'Noto Serif SC', serif;
  color: #5a3e2b;
}

.annotation-text p {
  font-family: inherit;
  line-height: 1.8;
}

/* 赏析 - 使用楷体，更有韵味 */
.appreciation-text {
  font-family: 'KaiTi', '华文楷书', 'STKaiti', '楷体', 'Noto Serif SC', serif;
  color: #5a3e2b;
}

.appreciation-text p {
  font-family: inherit;
  background: #fefaf5;
  padding: 20px 24px;
  border-radius: 12px;
  border-left: 4px solid #b89a6e;
  margin: 0;
  line-height: 1.8;
}

/* 上下章按钮 */
.nav-buttons-bottom {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 40px;
  padding-top: 24px;
  border-top: 1px solid #e3d5c3;
}

.nav-btn {
  padding: 12px 32px;
  background: linear-gradient(135deg, #8b6b4d, #6f523b);
  color: #fef7e9;
  border: none;
  border-radius: 40px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(111, 82, 59, 0.2);
  font-family: inherit;
}

.nav-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(111, 82, 59, 0.3);
}

.nav-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  transform: none;
}

/* 章节信息 */
.chapter-info {
  text-align: center;
  margin-top: 24px;
  padding-top: 16px;
  color: #a28b72;
  font-size: 0.85rem;
  font-family: inherit;
}

/* 响应式 */
@media (max-width: 768px) {
  .chapter-detail {
    padding: 24px 0;
  }

  .container {
    padding: 0 16px;
  }

  .chapter-header {
    flex-direction: column;
    text-align: center;
  }

  .chapter-title {
    font-size: 1.5rem;
    order: -1;
  }

  .back-btn {
    align-self: flex-start;
  }

  .card-header {
    padding: 16px 20px;
  }

  .card-content {
    padding: 20px;
  }

  .original-text p,
  .translation-text p,
  .annotation-text p,
  .appreciation-text p {
    font-size: 16px;
  }

  .nav-buttons-bottom {
    flex-direction: column;
    gap: 12px;
    align-items: center;
  }

  .nav-btn {
    width: 160px;
    text-align: center;
  }
}
</style>
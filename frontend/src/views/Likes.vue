<template>
  <div>
    <div class="container mt-4">
      <BreadcrumbNav :items="breadcrumbItems" />
      <h1>我的收藏</h1>
      <div v-if="favorites.length > 0" class="favorites-grid">
        <div 
          v-for="favorite in favorites" 
          :key="favorite.id"
          class="favorite-card"
          @click="goToBookDetail(favorite.bookId)"
        >
          <img :src="favorite.coverUrl || '/src/assets/covers/default.jpg'" :alt="favorite.title" />
          <div class="favorite-info">
            <h3>{{ favorite.title }}</h3>
            <p>{{ favorite.author || '佚名' }}</p>
            <span class="favorite-date">{{ formatDate(favorite.createdAt) }}</span>
          </div>
        </div>
      </div>
      <div v-else class="no-favorites">
        <p>暂无收藏的书籍</p>
        <router-link to="/books" class="btn btn-primary">去典籍库看看</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request from '../utils/request'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '我的收藏' }
]

const userStore = useUserStore()
const favorites = ref([])

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const goToBookDetail = (bookId) => {
  router.push(`/books/${bookId}`)
}

onMounted(async () => {
  if (userStore.isLoggedIn) {
    try {
      const response = await request.get('/user/favorites')
      favorites.value = response.data
    } catch (error) {
      console.error('获取收藏列表失败', error)
      ElMessage.error('获取收藏列表失败')
      // 模拟数据用于测试
      favorites.value = []
    }
  }
})
</script>

<style scoped>
.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 1rem;
}

.favorite-card {
  background: #fffbf5;
  border: 1px solid #e3d5c3;
  border-radius: 12px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.favorite-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.favorite-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.favorite-info h3 {
  color: #5a3e2b;
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
}

.favorite-info p {
  color: #7a6856;
  margin-bottom: 0.5rem;
}

.favorite-date {
  color: #a28b72;
  font-size: 0.9rem;
}

.no-favorites {
  text-align: center;
  padding: 3rem 0;
  color: #7a6856;
}

.no-favorites p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.btn-primary {
  background-color: #8b6b4d;
  color: #fef7e9;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 40px;
  text-decoration: none;
  display: inline-block;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #6f523b;
  color: #fef7e9;
}
</style>
<template>
  <div>
    <div class="container mt-4">
      <BreadcrumbNav :items="breadcrumbItems" />
      <h2>我的书架</h2>
      <div v-if="loading" class="text-center py-4">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">加载中...</span>
        </div>
      </div>
      <div v-else-if="books.length === 0" class="alert alert-info">
        暂无收藏的书籍
      </div>
      <div v-else class="row">
        <div class="col-md-4 mb-4" v-for="book in books" :key="book.id">
          <div class="card h-100">
            <img :src="book.coverUrl || 'https://picsum.photos/300/200?random=' + book.id" class="card-img-top" style="height:200px; object-fit:cover;">
            <div class="card-body">
              <h5 class="card-title">{{ book.title }}</h5>
              <p class="card-text">{{ book.author }} · {{ book.dynasty }}</p>
              <router-link :to="'/books/' + book.id" class="btn btn-primary">查看详情</router-link>
              <button class="btn btn-outline-danger btn-sm mt-2" @click="removeFavorite(book.id)">取消收藏</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessageBox, ElMessage } from 'element-plus'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

export default {
  setup() {
    const breadcrumbItems = [
      { label: '首页', path: '/' },
      { label: '我的书架' }
    ]

    const books = ref([])
    const loading = ref(true)

    const fetchFavorites = async () => {
      try {
        const res = await request.get('/user/favorites')
        books.value = res.data
      } catch (error) {
        console.error('获取收藏失败', error)
      } finally {
        loading.value = false
      }
    }

    const removeFavorite = async (bookId) => {
      try {
        await ElMessageBox.confirm('确定取消收藏吗？', '提示', { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' })
      } catch {
        return
      }
      try {
        await request.delete('/user/favorites', { params: { bookId } })
        ElMessage.success('已取消收藏')
        fetchFavorites()
      } catch (error) {
        console.error('取消收藏失败', error)
        ElMessage.error('取消收藏失败')
      }
    }

    onMounted(() => {
      fetchFavorites()
    })

    return { books, loading, removeFavorite, breadcrumbItems }
  }
}
</script>
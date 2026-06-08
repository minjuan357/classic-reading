<template>
  <div>
    <div class="container mt-4">
      <BreadcrumbNav :items="breadcrumbItems" />
      <h1>个人中心</h1>
      <div class="profile-card">
        <div class="profile-header">
          <h2>用户信息</h2>
        </div>
        <div class="profile-content">
          <div class="info-item">
            <label>用户名：</label>
            <span>{{ userInfo.username || '加载中...' }}</span>
          </div>
          <div class="info-item">
            <label>邮箱：</label>
            <span>{{ userInfo.email || '未设置' }}</span>
          </div>
          <div class="info-item">
            <label>注册时间：</label>
            <span>{{ formatDate(userInfo.createdAt) || '未知' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import request from '../utils/request'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'
import { ElMessage } from 'element-plus'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '个人中心' }
]

const userStore = useUserStore()
const userInfo = ref({})

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

onMounted(async () => {
  if (userStore.isLoggedIn) {
    try {
      const response = await request.get('/auth/me')
      userInfo.value = response.data
    } catch (error) {
      console.error('获取用户信息失败', error)
      ElMessage.error('获取用户信息失败')
    }
  }
})
</script>

<style scoped>
.profile-card {
  background: #fffbf5;
  border: 1px solid #e3d5c3;
  border-radius: 16px;
  padding: 2rem;
  margin-top: 1rem;
}

.profile-header h2 {
  color: #5a3e2b;
  margin-bottom: 1.5rem;
  border-bottom: 2px solid #c9ad8e;
  padding-bottom: 0.5rem;
}

.info-item {
  display: flex;
  margin-bottom: 1rem;
  padding: 0.5rem 0;
}

.info-item label {
  font-weight: 600;
  color: #5a3e2b;
  min-width: 100px;
}

.info-item span {
  color: #7a6856;
}
</style>
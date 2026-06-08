<template>
  <div class="forgot-container">
    <div class="forgot-card">
      <h2 class="forgot-title">重置密码</h2>
      <p class="forgot-desc">请联系管理员重置密码，或发送邮件至：</p>
      <p class="forgot-email">admin@classic-reading.com</p>
      <router-link to="/login" class="back-link">← 返回登录</router-link>
    </div>
  </div>
</template>

<script setup>
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '重置密码' }
]
</script>

<style scoped>
.forgot-container {
  min-height: calc(100vh - 200px);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  background: linear-gradient(135deg, #f9f3e8 0%, #f0e7db 100%);
}

.forgot-card {
  background: #fffbf5;
  border-radius: 24px;
  padding: 40px 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
  border: 1px solid #e3d5c3;
  text-align: center;
}

.forgot-title {
  font-size: 2rem;
  font-weight: 600;
  color: #5a3e2b;
  margin-bottom: 16px;
  letter-spacing: 2px;
}

.forgot-desc {
  color: #7a6856;
  font-size: 1rem;
  margin-bottom: 12px;
}

.forgot-email {
  color: #8b6b4d;
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 24px;
  padding: 8px 16px;
  background: #fcf8f2;
  border-radius: 8px;
  display: inline-block;
}

.back-link {
  display: inline-block;
  color: #b89a6e;
  text-decoration: none;
  font-weight: 500;
  margin-top: 16px;
  transition: color 0.3s ease;
}

.back-link:hover {
  color: #8b6b4d;
}

@media (max-width: 480px) {
  .forgot-card {
    padding: 32px 24px;
  }
}
</style>

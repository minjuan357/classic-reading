<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">用户登录</h2>
      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
              type="text"
              id="username"
              v-model="username"
              placeholder="请输入用户名"
              :class="{ 'input-error': usernameError }"
              @blur="usernameTouched = true"
              required
          >
          <p v-if="usernameError" class="field-error">{{ usernameError }}</p>
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
              type="password"
              id="password"
              v-model="password"
              placeholder="请输入密码"
              :class="{ 'input-error': passwordError }"
              @blur="passwordTouched = true"
              required
          >
          <p v-if="passwordError" class="field-error">{{ passwordError }}</p>
        </div>
        <button type="submit" class="login-btn" :disabled="loading">
          <span v-if="!loading">登录</span>
          <span v-else>登录中...</span>
        </button>
      </form>
      <div class="register-link">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
      <div class="forgot-link">
        <router-link to="/forgot-password">忘记密码？</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const username = ref('')
const password = ref('')
const loading = ref(false)
const usernameTouched = ref(false)
const passwordTouched = ref(false)

const usernameError = computed(() => {
  if (!usernameTouched.value || username.value) return ''
  return '请输入用户名'
})

const passwordError = computed(() => {
  if (!passwordTouched.value || password.value) return ''
  return '请输入密码'
})

const valid = computed(() => username.value && password.value)

const handleLogin = async () => {
  usernameTouched.value = true
  passwordTouched.value = true
  if (!username.value || !password.value) {
    ElMessage.warning('请填写用户名和密码')
    return
  }

  loading.value = true
  try {
    const response = await request.post('/auth/login', {
      username: username.value,
      password: password.value
    })

    if (response.data.token) {
      userStore.setUser(response.data)
      ElMessage.success('登录成功')
      router.push('/')
    }
  } catch (error) {
    console.error('登录失败', error)
    ElMessage.error(error.response?.data?.message || '登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: calc(100vh - 200px);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  background: linear-gradient(135deg, #f9f3e8 0%, #f0e7db 100%);
}

.login-card {
  background: #fffbf5;
  border-radius: 24px;
  padding: 40px 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08), 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #e3d5c3;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 24px 48px rgba(0, 0, 0, 0.12);
}

.login-title {
  font-size: 2rem;
  font-weight: 600;
  color: #5a3e2b;
  text-align: center;
  margin-bottom: 32px;
  letter-spacing: 2px;
  position: relative;
}

.login-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #c9ad8e, #b89a6e);
  border-radius: 2px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form-group label {
  font-size: 0.95rem;
  font-weight: 500;
  color: #6f4f2f;
  letter-spacing: 0.5px;
}

.input-error {
  border-color: #e74c3c !important;
}

.field-error {
  font-size: 0.8rem;
  color: #e74c3c;
  margin: 0;
  line-height: 1.4;
}

.form-group input {
  padding: 12px 16px;
  border: 1.5px solid #e3d5c3;
  border-radius: 12px;
  font-size: 1rem;
  background-color: #ffffff;
  transition: all 0.3s ease;
  outline: none;
  color: #3e2e1f;
}

.form-group input:focus {
  border-color: #b89a6e;
  box-shadow: 0 0 0 3px rgba(184, 154, 110, 0.1);
}

.form-group input::placeholder {
  color: #c9b9a4;
}

.login-btn {
  margin-top: 8px;
  padding: 14px 24px;
  background: linear-gradient(135deg, #8b6b4d 0%, #6f523b 100%);
  color: #fef7e9;
  border: none;
  border-radius: 40px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  letter-spacing: 1px;
  box-shadow: 0 4px 12px rgba(111, 82, 59, 0.3);
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(111, 82, 59, 0.4);
  background: linear-gradient(135deg, #7a5c3e 0%, #5a402b 100%);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  margin-top: 28px;
  padding-top: 20px;
  border-top: 1px solid #f0e8dd;
  font-size: 0.95rem;
  color: #7a6856;
}

.register-link a {
  color: #b89a6e;
  text-decoration: none;
  font-weight: 600;
  margin-left: 8px;
  transition: color 0.3s ease;
}

.register-link a:hover {
  color: #8b6b4d;
  text-decoration: underline;
}

.forgot-link {
  text-align: center;
  margin-top: 12px;
  font-size: 0.9rem;
}

.forgot-link a {
  color: #a28b72;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-link a:hover {
  color: #8b6b4d;
  text-decoration: underline;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .login-card {
    padding: 32px 24px;
  }

  .login-title {
    font-size: 1.8rem;
  }

  .form-group input {
    padding: 10px 14px;
  }

  .login-btn {
    padding: 12px 20px;
    font-size: 1rem;
  }
}
</style>
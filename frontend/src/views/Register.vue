<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">注 册</h2>
      <form @submit.prevent="handleRegister">
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
          />
          <p v-if="usernameError" class="field-error">{{ usernameError }}</p>
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
              type="password"
              id="password"
              v-model="password"
              placeholder="请输入密码（至少6位）"
              :class="{ 'input-error': passwordError }"
              @blur="passwordTouched = true"
              required
          />
          <p v-if="passwordError" class="field-error">{{ passwordError }}</p>
        </div>
        <div class="form-group">
          <label for="email">邮箱（可选）</label>
          <input
              type="email"
              id="email"
              v-model="email"
              placeholder="请输入邮箱"
              :class="{ 'input-error': emailError }"
              @blur="emailTouched = true"
          />
          <p v-if="emailError" class="field-error">{{ emailError }}</p>
        </div>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <!-- 成功提示 -->
        <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const username = ref('')
const password = ref('')
const email = ref('')
const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const usernameTouched = ref(false)
const passwordTouched = ref(false)
const emailTouched = ref(false)

const usernameError = computed(() => {
  if (!usernameTouched.value) return ''
  if (!username.value) return '请输入用户名'
  if (username.value.length < 2) return '用户名至少2个字符'
  return ''
})

const passwordError = computed(() => {
  if (!passwordTouched.value) return ''
  if (!password.value) return '请输入密码'
  if (password.value.length < 6) return '密码至少6个字符'
  return ''
})

const emailError = computed(() => {
  if (!emailTouched.value || !email.value) return ''
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) return '邮箱格式不正确'
  return ''
})

const valid = computed(() => username.value.length >= 2 && password.value.length >= 6)

const handleRegister = async () => {
  usernameTouched.value = true
  passwordTouched.value = true
  if (!username.value) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!password.value || password.value.length < 6) {
    ElMessage.warning('密码至少6个字符')
    return
  }
  loading.value = true
  errorMessage.value = ''
  successMessage.value = ''
  try {
    await request.post('/auth/register', {
      username: username.value,
      password: password.value,
      email: email.value || undefined
    })
    successMessage.value = '注册成功，即将跳转到登录页面...'
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    console.error('注册失败', error)
    errorMessage.value = error.response?.data?.message || '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 与登录页面保持完全一致的视觉风格 */
.login-container {
  min-height: 70vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f9f3e9; /* 宣纸底色 */
  padding: 2rem;
}

.login-card {
  background: #fffbf5;
  border: 1px solid #e3d5c3;
  border-radius: 24px;
  padding: 3rem 2.5rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
}

.login-title {
  font-size: 2.5rem;
  font-weight: 600;
  color: #5a3e2b;
  text-align: center;
  margin-bottom: 2rem;
  letter-spacing: 4px;
  border-bottom: 2px solid #c9ad8e;
  padding-bottom: 0.8rem;
}

.form-group {
  margin-bottom: 1.8rem;
}

.form-group label {
  display: block;
  font-size: 1rem;
  color: #5f4a36;
  margin-bottom: 0.4rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 0.9rem 1.2rem;
  border: 1px solid #dacbb8;
  border-radius: 40px;
  background-color: #fefcf8;
  font-size: 1rem;
  font-family: 'Noto Serif SC', serif;
  transition: border 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: #8b6b4d;
  box-shadow: 0 0 0 3px rgba(139, 107, 77, 0.2);
}

.input-error {
  border-color: #e74c3c !important;
}

.field-error {
  font-size: 0.8rem;
  color: #e74c3c;
  margin: 4px 0 0 12px;
  line-height: 1.4;
}

.login-btn {
  width: 100%;
  padding: 1rem;
  background-color: #8b6b4d;
  color: #fef7e9;
  border: none;
  border-radius: 40px;
  font-size: 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.1s;
  margin-top: 1rem;
  font-family: 'Noto Serif SC', serif;
}

.login-btn:hover:not(:disabled) {
  background-color: #6f523b;
  transform: translateY(-2px);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 错误与成功提示样式 */
.error-message {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 40px;
  padding: 0.8rem 1.2rem;
  margin-bottom: 1.5rem;
  text-align: center;
  font-size: 0.95rem;
}

.success-message {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
  border-radius: 40px;
  padding: 0.8rem 1.2rem;
  margin-bottom: 1.5rem;
  text-align: center;
  font-size: 0.95rem;
}

/* 登录链接样式（与登录页面的 .register-link 对称） */
.login-link {
  text-align: center;
  margin-top: 1.8rem;
  font-size: 1rem;
  color: #6f5a47;
}

.login-link a {
  color: #8b6b4d;
  text-decoration: none;
  font-weight: 600;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
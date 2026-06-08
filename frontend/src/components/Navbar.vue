<template>
  <nav class="navbar">
    <div class="nav-container">
      <router-link to="/" class="logo">经典重读·AI 伴学</router-link>

      <button class="hamburger" :class="{ open: isMobileMenuOpen }" @click="toggleMobileMenu" aria-label="菜单">
        <span></span><span></span><span></span>
      </button>

      <div class="nav-links" :class="{ 'mobile-open': isMobileMenuOpen }">
        <router-link to="/">首页</router-link>
        <router-link to="/books">典籍库</router-link>
        <router-link to="/community">社区</router-link>
        <router-link to="/knowledge">知识科普</router-link>
        <router-link to="/theme-topics">主题专栏</router-link>
        <router-link to="/ai">AI 助手</router-link>

        <div class="nav-search">
          <span class="nav-search-icon">&#128269;</span>
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索典籍..."
            @keyup.enter="goToSearch"
          />
        </div>

        <!-- 用户菜单 -->
        <div class="user-menu" v-if="userStore.isLoggedIn">
          <button class="user-dropdown-btn" @click="toggleDropdown">
            <span class="username">{{ userStore.username }}</span>
            <span class="dropdown-arrow">▼</span>
          </button>

          <div class="user-dropdown-menu" :class="{ open: isDropdownOpen }">
            <router-link to="/profile" class="dropdown-item" @click="closeDropdown">
              <span class="icon">👤</span>个人中心
            </router-link>
            <router-link to="/favorites" class="dropdown-item" @click="closeDropdown">
              <span class="icon">❤️</span>我的收藏
            </router-link>
            <router-link to="/history" class="dropdown-item" @click="closeDropdown">
              <span class="icon">📚</span>浏览历史
            </router-link>
            <div class="dropdown-divider"></div>
            <button class="dropdown-item logout-btn" @click="handleLogout">
              <span class="icon">🚪</span>退出登录
            </button>
          </div>
        </div>

        <router-link v-else to="/login" class="login-btn">登录</router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const isDropdownOpen = ref(false)
const isMobileMenuOpen = ref(false)
const searchKeyword = ref('')

const goToSearch = () => {
  const keyword = searchKeyword.value.trim()
  if (keyword) {
    searchKeyword.value = ''
    router.push(`/books?keyword=${encodeURIComponent(keyword)}`)
  }
}

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false
}

const toggleDropdown = (event) => {
  event.preventDefault()
  event.stopPropagation()
  isDropdownOpen.value = !isDropdownOpen.value
}

const closeDropdown = () => {
  isDropdownOpen.value = false
}

const handleClickOutside = (event) => {
  const userMenu = document.querySelector('.user-menu')
  const dropdownBtn = document.querySelector('.user-dropdown-btn')

  if (userMenu && !userMenu.contains(event.target) && event.target !== dropdownBtn) {
    closeDropdown()
  }
}

const handleLogout = () => {
  userStore.logout()
  closeDropdown()
  router.push('/login')
}

watch(() => router.currentRoute.value, () => {
  isMobileMenuOpen.value = false
})

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.navbar {
  background: #5a3e2b;
  color: #fef7e9;
  padding: 0.8rem 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  font-family: 'Noto Serif SC', serif;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1.5rem;
}

.logo {
  font-size: 1.6rem;
  font-weight: 700;
  color: #f3e1c2;
  text-decoration: none;
  letter-spacing: 1px;
}

/* 导航链接布局 */
.nav-links {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  gap: 1rem;
}

.nav-links a {
  color: #f0e2d2;
  text-decoration: none;
  font-size: 1.1rem;
  transition: color 0.2s;
  white-space: nowrap;
  padding: 0.5rem 0;
}

.nav-links a:hover {
  color: #ffd9a3;
}

.nav-links a.router-link-active {
  color: #ffffff;
  font-weight: 500;
  border-bottom: 2px solid #e2c28b;
}

/* 用户菜单样式 - 关键修复 */
.user-menu {
  position: relative;
  display: inline-block;
}

.user-dropdown-btn {
  background: none;
  border: none;
  color: #f0e2d2;
  cursor: pointer;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.1rem;
  white-space: nowrap;
  outline: none;
}

.user-dropdown-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.username {
  font-weight: 500;
}

.dropdown-arrow {
  font-size: 0.8rem;
  transition: transform 0.2s;
}

.user-dropdown-btn:hover .dropdown-arrow {
  transform: translateY(1px);
}

/* 登录链接样式 */
.login-link {
  white-space: nowrap;
}

/* 下拉菜单样式 - 关键修复 */
.user-dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  display: block;
  background: #fffdf9;
  border: 1px solid #d8c4aa;
  border-radius: 12px;
  box-shadow: 0 10px 26px rgba(0, 0, 0, 0.18);
  min-width: 180px;
  z-index: 9999;
  margin-top: 0.5rem;
  pointer-events: none;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

.user-dropdown-menu.open {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
  pointer-events: auto;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 0.8rem 1rem;
  color: #2f2418;
  font-weight: 500;
  text-decoration: none;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 1rem;
  border-radius: 0;
}

.dropdown-item:first-child {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.dropdown-item:last-child {
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}

.dropdown-item:hover {
  background-color: #efe2d3;
  color: #1f1710;
}

.dropdown-item .icon {
  font-size: 1.1rem;
  width: 20px;
  text-align: center;
}

.dropdown-divider {
  height: 1px;
  background-color: #d9c6ad;
  margin: 0.5rem 0;
}

.logout-btn {
  color: #b42318;
}

.logout-btn:hover {
  background-color: #fde8e8;
  color: #8f1d14;
}

.nav-search {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50px;
  padding: 2px 12px;
  transition: all 0.3s;
}

.nav-search:focus-within {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.35);
}

.nav-search-icon {
  color: #d4c5b0;
  font-size: 0.85rem;
  margin-right: 6px;
  line-height: 1;
}

.nav-search input {
  background: transparent;
  border: none;
  outline: none;
  color: #fef7e9;
  font-size: 0.9rem;
  width: 100px;
  padding: 6px 0;
  font-family: inherit;
  transition: width 0.3s;
}

.nav-search input:focus {
  width: 150px;
}

.nav-search input::placeholder {
  color: #c4b49e;
}

.login-btn {
  background-color: #8b6b4d;
  color: #fef7e9;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  text-decoration: none;
  transition: background-color 0.2s;
}

.login-btn:hover {
  background-color: #6f523b;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .nav-links {
    gap: 0.8rem;
  }

  .nav-links a {
    font-size: 1rem;
  }

  .user-dropdown-btn {
    padding: 0.4rem 0.8rem;
    font-size: 1rem;
  }
}

@media (max-width: 900px) {
  .nav-links {
    gap: 0.5rem;
  }

  .nav-links a {
    font-size: 0.9rem;
  }

  .username {
    max-width: 80px;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

/* 汉堡按钮 */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 36px;
  height: 36px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  gap: 5px;
}

.hamburger span {
  display: block;
  width: 24px;
  height: 2px;
  background: #f3e1c2;
  border-radius: 2px;
  transition: all 0.3s;
}

.hamburger.open span:nth-child(1) {
  transform: translateY(7px) rotate(45deg);
}

.hamburger.open span:nth-child(2) {
  opacity: 0;
}

.hamburger.open span:nth-child(3) {
  transform: translateY(-7px) rotate(-45deg);
}

@media (max-width: 768px) {
  .hamburger {
    display: flex;
  }

  .nav-links {
    display: none;
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    flex-direction: column;
    background: #5a3e2b;
    padding: 1rem;
    gap: 0.5rem;
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
    z-index: 999;
  }

  .nav-links.mobile-open {
    display: flex;
  }

  .nav-links a {
    font-size: 1rem;
    padding: 0.6rem 0;
    border-bottom: 1px solid rgba(255,255,255,0.1);
  }

  .nav-container {
    position: relative;
  }

  .nav-search {
    width: 100%;
    margin: 0.5rem 0;
  }

  .nav-search input {
    width: 100%;
  }

  .nav-search input:focus {
    width: 100%;
  }

  .user-menu {
    width: 100%;
  }

  .user-dropdown-btn {
    width: 100%;
    justify-content: center;
  }

  .login-btn {
    text-align: center;
    width: 100%;
  }

  .logo {
    font-size: 1.3rem;
  }
}
</style>
<template>
  <div id="app">
    <!-- 波浪背景组件，位于最底层（组件内已设置 z-index: -1） -->
    <WaveBackground />

    <!-- 导航栏：根据路由元信息决定是否显示 -->
    <Navbar v-if="!$route.meta.hideNavbar" />

    <!-- 主视图区域（带过渡动画） -->
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>

    <!-- 页脚：根据路由元信息决定是否显示 -->
    <Footer v-if="!$route.meta.hideFooter" />

    <!-- AI 侧边栏：默认显示，仅当路由元信息 showSidebar 明确设为 false 时隐藏 -->
    <AISidebar v-if="$route.meta.showSidebar !== false" />
  </div>
</template>

<script setup>
import WaveBackground from '@/components/WaveBackground.vue'
import Navbar from '@/components/Navbar.vue'
import Footer from '@/components/Footer.vue'
import AISidebar from '@/components/AISidebar.vue'

// ESC 键关闭弹窗
import { onMounted, onBeforeUnmount } from 'vue'
import { ElMessageBox } from 'element-plus'

const handleKeydown = (e) => {
  if (e.key === 'Escape') {
    ElMessageBox.close()
  }
}

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
})

onBeforeUnmount(() => {
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<style>
@import 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css';

body {
  background-color: #f9f5f0;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* 全局滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f5f0ea;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c9b28b;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #b89a6e;
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
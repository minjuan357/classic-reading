<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-links">
        <router-link to="/">首页</router-link>
        <span class="sep">·</span>
        <router-link to="/books">典籍库</router-link>
        <span class="sep">·</span>
        <router-link to="/community">社区</router-link>
        <span class="sep">·</span>
        <router-link to="/knowledge">知识科普</router-link>
        <span class="sep">·</span>
        <router-link to="/theme-topics">主题专栏</router-link>
      </div>
      <div class="footer-info">
        <p>© {{ currentYear }} 经典重读·AI伴学 团队 | 响应 "全民阅读" 号召，用科技传承经典</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
const currentYear = new Date().getFullYear()
</script>

<style scoped>
.footer {
  background-color: #4a3727;
  color: #cdb89e;
  text-align: center;
  padding: 1.5rem 0;
  margin-top: 3rem;
  font-size: 0.95rem;
  border-top: 3px solid #8b6b4d;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

.footer-links {
  margin-bottom: 0.8rem;
}

.footer-links a {
  color: #e2cfa5;
  text-decoration: none;
  margin: 0 0.3rem;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: #fff;
  text-decoration: underline;
}

.sep {
  color: #8b7355;
  margin: 0 0.2rem;
}

.footer-info p {
  margin: 0;
  font-size: 0.9rem;
}
</style>
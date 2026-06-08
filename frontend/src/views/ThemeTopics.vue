<template>
  <div class="theme-topics">
    <div class="container">
      <BreadcrumbNav :items="breadcrumbItems" />
      <h1 class="page-title">主题专栏</h1>
      <p class="subtitle">探寻古籍中的智慧，感受文化的力量</p>
      <div class="topic-grid">
        <div v-for="topic in topics" :key="topic.id" class="topic-card" @click="goToDetail(topic.link)">
          <div class="card-image">
            <img :src="topic.image" :alt="topic.title" />
            <div class="card-overlay"></div>
          </div>
          <div class="card-content">
            <h3>{{ topic.title }}</h3>
            <p>{{ topic.description }}</p>
            <span class="read-more">探索详情 →</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '主题专栏' }
]

const router = useRouter()
const topics = [
  {
    id: 1,
    title: '翰墨传家·体育古韵',
    description: '挖掘古籍中的体育精神，如射礼、蹴鞠、导引术，弘扬“强身健体、修身养性”的传统美德。',
    image: '/src/assets/carousel/sports_etiquette.jpg',
    link: '/theme/sports-virtue'
  },
  {
    id: 2,
    title: '文明互鉴·黄河西行',
    description: '展现黄河文明与世界文明的交流互鉴，体现文化自信与开放包容。',
    image:  '/src/assets/carousel/civilization_exchange.jpg',
    link: '/theme/civilization-exchange'
  },
  {
    id: 3,
    title: '童蒙养正·诗礼传家',
    description: '聚焦家风家训与儿童启蒙，呼应“双减”后家庭对传统文化教育的关注。',
    image: '/src/assets/carousel/family_education.jpg',
    link: '/theme/family-education'
  }
]

const goToDetail = (link) => {
  router.push(link)
}
</script>

<style scoped>
.theme-topics {
  background: #faf7f2;
  padding: 60px 0;
  min-height: 100vh;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.page-title {
  text-align: center;
  font-size: 42px;
  font-weight: 500;
  color: #3e2a1f;
  margin-bottom: 16px;
  letter-spacing: 2px;
}
.subtitle {
  text-align: center;
  color: #7d6b5a;
  font-size: 16px;
  margin-bottom: 48px;
}
.topic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 40px;
}
.topic-card {
  background: #fff;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
  cursor: pointer;
}
.topic-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 35px rgba(0,0,0,0.1);
}
.card-image {
  position: relative;
  height: 220px;
  overflow: hidden;
}
.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.topic-card:hover .card-image img {
  transform: scale(1.05);
}
.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(0deg, rgba(0,0,0,0.3) 0%, rgba(0,0,0,0) 100%);
}
.card-content {
  padding: 24px;
}
.card-content h3 {
  font-size: 22px;
  color: #3e2a1f;
  margin-bottom: 12px;
  font-weight: 500;
}
.card-content p {
  color: #6b5a48;
  line-height: 1.5;
  margin-bottom: 20px;
  font-size: 15px;
}
.read-more {
  display: inline-block;
  color: #b45f2b;
  font-weight: 500;
  border-bottom: 1px solid #b45f2b;
  padding-bottom: 2px;
  transition: color 0.2s;
}
.topic-card:hover .read-more {
  color: #8b4513;
}
</style>
<template>
  <div class="carousel-container" @mouseenter="pauseAuto" @mouseleave="startAuto">
    <div class="carousel">
      <div class="carousel-inner" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div
            v-for="(item, idx) in items"
            :key="idx"
            class="carousel-item"
            @click="goToDetail(item.link)"
        >
          <img
              :src="item.image"
              :alt="item.title"
              @error="handleImageError($event, idx)"
              loading="lazy"
          />
          <div class="carousel-caption">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>

      <div class="carousel-dots">
        <span
            v-for="(item, idx) in items"
            :key="idx"
            :class="{ active: idx === currentIndex }"
            @click="goToIndex(idx)"
        ></span>
      </div>

      <button class="carousel-prev" @click="prev">❮</button>
      <button class="carousel-next" @click="next">❯</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  items: { type: Array, required: true },
  interval: { type: Number, default: 4000 }
})

const router = useRouter()
const currentIndex = ref(0)
let timer = null

// 重置轮播（当 items 变化时调用）
const resetCarousel = () => {
  if (timer) clearInterval(timer)
  currentIndex.value = 0
  if (props.items.length > 0) startAuto()
}

const next = () => {
  if (props.items.length === 0) return
  currentIndex.value = (currentIndex.value + 1) % props.items.length
}
const prev = () => {
  if (props.items.length === 0) return
  currentIndex.value = (currentIndex.value - 1 + props.items.length) % props.items.length
}
const goToIndex = (index) => {
  currentIndex.value = index
}
const goToDetail = (link) => {
  router.push(link)
}
const startAuto = () => {
  if (timer) clearInterval(timer)
  timer = setInterval(next, props.interval)
}
const pauseAuto = () => {
  if (timer) clearInterval(timer)
  timer = null
}
const handleImageError = (event, idx) => {
  console.warn(`图片加载失败: ${props.items[idx].image}`)
  // 可以设置一个默认图片，这里留空
}

// 监听 items 变化（关键：解决热更新时数据丢失）
watch(() => props.items, resetCarousel, { immediate: true, deep: true })

onMounted(() => {
  resetCarousel()
})
onBeforeUnmount(() => pauseAuto())
</script>

<style scoped>
.carousel-container {
  width: 100%;
  margin: 40px 0;
  position: relative;
}
.carousel {
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  background: #f0f0f0;
}
.carousel-inner {
  display: flex;
  transition: transform 0.5s ease-in-out;
}
.carousel-item {
  flex: 0 0 100%;
  position: relative;
  cursor: pointer;
}
.carousel-item img {
  width: 100%;
  height: 400px;
  object-fit: contain;
  background-color: #2c2c2c;
  display: block;
}
.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(0deg, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0) 100%);
  color: white;
  padding: 20px;
  text-align: left;
}
.carousel-caption h3 {
  font-size: 24px;
  margin-bottom: 8px;
}
.carousel-caption p {
  font-size: 14px;
  opacity: 0.9;
}
.carousel-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 12px;
  z-index: 2;
}
.carousel-dots span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255,255,255,0.6);
  cursor: pointer;
  transition: all 0.3s;
}
.carousel-dots span.active {
  background: #ffd966;
  width: 24px;
  border-radius: 6px;
}
.carousel-prev,
.carousel-next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0,0,0,0.5);
  color: white;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 20px;
  z-index: 2;
  transition: background 0.3s;
}
.carousel-prev:hover,
.carousel-next:hover {
  background: rgba(0,0,0,0.8);
}
.carousel-prev {
  left: 20px;
}
.carousel-next {
  right: 20px;
}
</style>
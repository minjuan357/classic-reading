<template>
  <div class="wave-background">
    <!-- 水墨波纹层 -->
    <div class="ink-wave"></div>

    <!-- 浮动元素层（汉字、印章、竹简） -->
    <div class="floating-elements">
      <span
          v-for="(char, index) in characters"
          :key="'char' + index"
          class="float-item character"
          :style="getCharStyle(index)"
      >
        {{ char }}
      </span>
      <span
          v-for="(seal, index) in seals"
          :key="'seal' + index"
          class="float-item seal"
          :style="getSealStyle(index)"
      >
        {{ seal }}
      </span>
      <div
          v-for="i in 3"
          :key="'scroll' + i"
          class="float-item scroll"
          :style="getScrollStyle(i)"
      ></div>
    </div>
  </div>
</template>

<script setup>
const characters = ['经', '史', '子', '集', '诗', '书', '礼', '易', '春秋']
const seals = ['⚡', '☯', '文', '印'] // 印章符号

// 生成随机样式，使每个元素独立浮动
const getCharStyle = (index) => {
  const left = Math.random() * 90 + '%'
  const top = Math.random() * 90 + '%'
  const duration = 20 + Math.random() * 20 + 's'
  const delay = Math.random() * -20 + 's'
  const fontSize = 20 + Math.random() * 30 + 'px'
  const opacity = 0.1 + Math.random() * 0.2
  return {
    left,
    top,
    animation: `float ${duration} infinite linear`,
    animationDelay: delay,
    fontSize,
    opacity,
    transform: `rotate(${Math.random() * 360}deg)`
  }
}

const getSealStyle = (index) => {
  const left = Math.random() * 90 + '%'
  const top = Math.random() * 90 + '%'
  const duration = 25 + Math.random() * 20 + 's'
  const delay = Math.random() * -20 + 's'
  return {
    left,
    top,
    animation: `floatSeal ${duration} infinite ease-in-out`,
    animationDelay: delay,
    fontSize: '40px',
    opacity: 0.15
  }
}

const getScrollStyle = (i) => {
  const left = 10 + i * 30 + '%'
  const top = 70 + '%'
  const duration = 15 + i * 3 + 's'
  return {
    left,
    top,
    animation: `scrollFloat ${duration} infinite ease-in-out`,
    animationDelay: i * 2 + 's'
  }
}
</script>

<style scoped>
.wave-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1; /* 置于最底层 */
  overflow: hidden;
  background-color: #f7f3e8; /* 宣纸底色 */
}

/* 水墨波纹动画 */
.ink-wave {
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 30% 50%, rgba(0,0,0,0.02) 0%, transparent 30%),
  radial-gradient(circle at 70% 80%, rgba(0,0,0,0.02) 0%, transparent 30%),
  repeating-linear-gradient(45deg, rgba(0,0,0,0.01) 0px, rgba(0,0,0,0.01) 2px, transparent 2px, transparent 8px);
  animation: waveMove 30s infinite alternate;
}

@keyframes waveMove {
  0% { transform: scale(1) rotate(0deg); opacity: 0.5; }
  100% { transform: scale(1.2) rotate(1deg); opacity: 0.8; }
}

/* 浮动元素容器 */
.floating-elements {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 允许点击穿透，不影响交互 */
}

.float-item {
  position: absolute;
  color: rgba(60, 40, 20, 0.3);
  font-family: 'KaiTi', '楷体', 'STKaiti', serif;
  white-space: nowrap;
  user-select: none;
}

/* 汉字浮动 */
@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(20px, -20px) rotate(5deg); }
  50% { transform: translate(0, -40px) rotate(0deg); }
  75% { transform: translate(-20px, -20px) rotate(-5deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

/* 印章浮动（带缩放） */
@keyframes floatSeal {
  0% { transform: translate(0, 0) rotate(0deg) scale(1); }
  33% { transform: translate(30px, -30px) rotate(10deg) scale(1.1); }
  66% { transform: translate(-20px, 20px) rotate(-5deg) scale(0.9); }
  100% { transform: translate(0, 0) rotate(0deg) scale(1); }
}

/* 竹简模拟 */
.scroll {
  width: 60px;
  height: 20px;
  background: linear-gradient(to right, #c0a080, #e0c0a0);
  border-radius: 4px;
  opacity: 0.1;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

@keyframes scrollFloat {
  0% { transform: translateY(0) rotate(0deg); opacity: 0.1; }
  50% { transform: translateY(-100px) rotate(10deg); opacity: 0.3; }
  100% { transform: translateY(0) rotate(0deg); opacity: 0.1; }
}
</style>
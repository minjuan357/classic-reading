<template>
  <div class="knowledge-container">
    <BreadcrumbNav :items="breadcrumbItems" />
    <h1 class="page-title">知识科普</h1>
    <p class="subtitle">探寻古今学习文化变迁 · 对比智慧传承</p>

    <!-- 引言区域 -->
    <div class="quote-section">
      <div class="quote-card">
        <p class="quote-text">“学而时习之，不亦说乎。” —— 孔子</p>
        <p class="quote-text">“教育是点燃一把火，而不是灌满一桶水。” —— 叶芝</p>
      </div>
    </div>

    <!-- 学习方式对比（原有） -->
    <div class="comparison-section">
      <h2 class="section-title">📖 学习方式对比</h2>
      <div class="comparison-row">
        <div class="comparison-card ancient">
          <h3>古代学习方式</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/ancient_learning.jpg" alt="古代学习方式">
          </div>
          <p>以私塾、师徒传授为主，研习四书五经，强调背诵与科举应试，学习资源稀缺，注重礼仪规范。</p>
        </div>
        <div class="comparison-card modern">
          <h3>现代学习方式</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/modern_learning.jpg" alt="现代学习方式">
          </div>
          <p>数字化学习、在线课程、互动多媒体，终身教育普及，个性化学习路径，知识获取高效便捷。</p>
        </div>
      </div>
    </div>

    <!-- 学习地点对比（原有） -->
    <div class="comparison-section">
      <h2 class="section-title">🏛️ 学习地点对比</h2>
      <div class="comparison-row">
        <div class="comparison-card ancient">
          <h3>古代学习地点</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/extra_private_school.jpg" alt="古代私塾">
          </div>
          <p>私塾、书院、祠堂或家中，环境简朴，师生关系紧密，学习受地域和阶层限制较大。</p>
        </div>
        <div class="comparison-card modern">
          <h3>现代学习地点</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/modern_learning.jpg" alt="现代学习地点">
          </div>
          <p>学校、图书馆、线上平台、咖啡馆、移动场景，学习空间多元化，打破时空界限。</p>
        </div>
      </div>
    </div>

    <!-- 书籍保存与传阅对比（原有） -->
    <div class="comparison-section">
      <h2 class="section-title">📚 书籍保存与传阅对比</h2>
      <div class="comparison-row">
        <div class="comparison-card ancient">
          <h3>古代保存传阅</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/ancient_preservation.jpg" alt="古代书籍保存">
          </div>
          <p>竹简、帛书、手抄本，藏书楼与私人藏家，传阅范围小，复制困难，珍贵典籍易失传。</p>
        </div>
        <div class="comparison-card modern">
          <h3>现代保存传阅</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/modern_preservation.jpg" alt="现代书籍保存">
          </div>
          <p>数字图书馆、电子书、云端存储，网络分享与全球传播，保存永久，知识获取瞬间可达。</p>
        </div>
      </div>
    </div>

    <!-- 新增：学习工具对比 -->
    <div class="comparison-section">
      <h2 class="section-title">✍️ 学习工具对比</h2>
      <div class="comparison-row">
        <div class="comparison-card ancient">
          <h3>古代学习工具</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/ancient_tools.jpg" alt="古代学习工具">
          </div>
          <p>毛笔、墨、砚台、竹简、戒尺、算盘，工具原始但培养耐心与专注力。</p>
        </div>
        <div class="comparison-card modern">
          <h3>现代学习工具</h3>
          <div class="image-wrapper">
            <img src="@/assets/knowledge/modern_tools.jpg" alt="现代学习工具">
          </div>
          <p>电脑、平板、手机、AI辅助软件、电子笔记、VR/AR设备，工具智能高效，学习场景无限延伸。</p>
        </div>
      </div>
    </div>

    <!-- 新增：教育数据卡片 -->
    <div class="stats-section">
      <h2 class="section-title">📊 古今教育数据对比</h2>
      <div class="stats-row">
        <div class="stat-card">
          <div class="stat-number">~5%</div>
          <div class="stat-label">古代识字率（明清时期）</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">96%+</div>
          <div class="stat-label">现代识字率（中国）</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">科举录取率约0.1%</div>
          <div class="stat-label">古代科举</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">高等教育毛入学率59.6%</div>
          <div class="stat-label">2023年（中国）</div>
        </div>
      </div>
    </div>

    <!-- 视频区域：卡片样式 + 详细文字 -->
    <div class="video-section">
      <h2 class="section-title">🎥 纪录片推荐：<span class="video-sub">如果古代圣贤看到现代科技</span></h2>
      <div class="video-card">
        <div class="video-wrapper">
          <iframe
              src="https://player.bilibili.com/player.html?bvid=BV1ov4y1L7Lz&page=1"
              scrolling="no"
              frameborder="no"
              allowfullscreen="true"
              title="如果古代圣贤看到现代科技"
          ></iframe>
        </div>
        <div class="video-info">
          <h3>跨越千年的对话：当孔子遇见AI，张衡仰望天问</h3>
          <p>
            如果孔子穿越到今天的课堂，看到学生们通过平板电脑研读《论语》、与AI孔子对话，他会作何感想？
            如果张衡目睹“天问一号”传回火星影像，是否会惊叹于后人将他的浑天仪延伸到了星辰大海？
          </p>
          <p>
            本视频以创意手法，将古代圣贤置于现代科技场景中，展现传统智慧与当代创新的碰撞。
            孔子或许会感慨“学而时习之”有了全新的方式，王羲之或许会尝试用数字绘画挥毫，李时珍可能借助基因编辑重新修订《本草纲目》……
          </p>
          <p>
            科技并未割裂传统，反而让经典以更鲜活的方式传承。正如《典籍里的中国》所展现的，古今对话正是文化生命力的体现。
            让我们跟随镜头，开启一场穿越时空的求知之旅。
          </p>
          <p class="video-caption">视频来源：央视《典籍里的中国》片段（B站可观看）</p>
        </div>
      </div>
      <p class="video-note">
        ※ 若视频无法播放，请尝试访问原平台。更多内容可搜索“典籍里的中国 古代圣贤”。
      </p>
    </div>

    <!-- 新增：文化传承小故事 -->
    <div class="story-section">
      <h2 class="section-title">📜 文化传承小故事</h2>
      <div class="story-card">
        <p><strong>天一阁藏书楼</strong> —— 明代范钦创建的私家藏书楼，历经450余年，保存了大量珍贵典籍，如今成为数字化保护典范。</p>
        <p><strong>敦煌遗书数字化</strong> —— 通过国际协作，将散落全球的敦煌文献高清扫描，免费向公众开放，让千年智慧触手可及。</p>
      </div>
    </div>

    <!-- 结尾引用 -->
    <div class="footer-quote">
      <p>学习，从未停止；传承，就在当下。</p>
    </div>
  </div>
</template>

<script setup>
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '知识科普' }
]
</script>

<style scoped>
.knowledge-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
  font-family: 'Noto Serif SC', 'SimSun', serif;
}

.page-title {
  font-size: 2.2rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 0.5rem;
  color: #5a3e2b;
  letter-spacing: 2px;
}

.subtitle {
  text-align: center;
  color: #8f7b67;
  margin-bottom: 2rem;
  font-size: 1rem;
  border-bottom: 2px solid #c9ad8e;
  display: inline-block;
  padding-bottom: 0.5rem;
  width: auto;
  margin-left: auto;
  margin-right: auto;
  font-style: italic;
}

/* 引言区域 */
.quote-section {
  margin-bottom: 3rem;
}
.quote-card {
  background: #fcf8f2;
  border-left: 4px solid #c9ad8e;
  padding: 1.2rem 2rem;
  border-radius: 20px;
  text-align: center;
}
.quote-text {
  font-style: italic;
  color: #5a3e2b;
  font-size: 1.1rem;
  margin: 0.5rem 0;
  font-weight: 500;
}

/* 对比卡片通用样式 */
.comparison-section {
  margin-bottom: 4rem;
}
.section-title {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 1.8rem;
  padding-left: 1rem;
  border-left: 5px solid #c9ad8e;
  color: #4a3727;
}
.comparison-row {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  justify-content: center;
}
.comparison-card {
  flex: 1;
  min-width: 280px;
  background: #ffffff;
  border-radius: 28px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.comparison-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 35px rgba(0, 0, 0, 0.12);
}
.comparison-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  text-align: center;
  padding: 1rem 0 0.5rem;
  margin: 0;
  background: #f9fafb;
}
.ancient h3 {
  color: #7a5c3e;
  background: linear-gradient(135deg, #fcf8f2 0%, #f5ebe0 100%);
  border-bottom: 2px solid #e3d5c3;
}
.modern h3 {
  color: #6f523b;
  background: linear-gradient(135deg, #fefaf5 0%, #fcf8f2 100%);
  border-bottom: 2px solid #e3d5c3;
}
.image-wrapper {
  width: 100%;
  height: 220px;
  overflow: hidden;
  background-color: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
}
.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.comparison-card:hover .image-wrapper img {
  transform: scale(1.02);
}
.comparison-card p {
  padding: 1.2rem 1.5rem 1.5rem;
  line-height: 1.8;
  color: #5a3e2b;
  font-size: 0.95rem;
  margin: 0;
  background: white;
}

/* 数据卡片 */
.stats-section {
  margin-bottom: 4rem;
}
.stats-row {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  justify-content: center;
}
.stat-card {
  background: #fffbf5;
  border-radius: 24px;
  padding: 1.5rem;
  text-align: center;
  flex: 1;
  min-width: 180px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05);
  border: 1px solid #e3d5c3;
}
.stat-number {
  font-size: 1.8rem;
  font-weight: 700;
  color: #8b6b4d;
  margin-bottom: 0.5rem;
}
.stat-label {
  font-size: 0.9rem;
  color: #7a6856;
}

/* 视频区域 - 卡片样式 */
.video-section {
  margin-bottom: 4rem;
}
.video-sub {
  font-size: 1rem;
  color: #5d6d7e;
  font-weight: normal;
}
.video-card {
  background: #fffbf5;
  border-radius: 28px;
  overflow: hidden;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  border: 1px solid #e3d5c3;
}
.video-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 35px rgba(0, 0, 0, 0.12);
}
.video-wrapper {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 */
  height: 0;
  overflow: hidden;
  background: #000;
}
.video-wrapper iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: none;
}
.video-info {
  padding: 1.8rem;
}
.video-info h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin: 0 0 1rem;
  color: #5a3e2b;
}
.video-info p {
  margin: 0 0 1rem;
  line-height: 1.8;
  color: #5a3e2b;
  font-size: 1rem;
}
.video-caption {
  font-size: 0.85rem;
  color: #8f7b67;
  text-align: center;
  margin-top: 1rem;
  padding-top: 0.5rem;
  border-top: 1px solid #e3d5c3;
}
.video-note {
  text-align: center;
  font-size: 0.85rem;
  color: #8f7b67;
  margin-top: 0.8rem;
}

/* 故事区域 */
.story-section {
  margin-bottom: 3rem;
}
.story-card {
  background: #fcf8f2;
  border-radius: 20px;
  padding: 1.8rem;
  border-left: 4px solid #c9ad8e;
}
.story-card p {
  margin: 0.8rem 0;
  line-height: 1.8;
  color: #5a3e2b;
}

/* 结尾引用 */
.footer-quote {
  text-align: center;
  font-size: 1.2rem;
  font-weight: 500;
  padding: 2rem 0 1rem;
  color: #8b6b4d;
  border-top: 1px solid #e3d5c3;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .knowledge-container {
    padding: 1rem;
  }
  .page-title {
    font-size: 1.8rem;
  }
  .section-title {
    font-size: 1.4rem;
  }
  .comparison-row {
    gap: 1.5rem;
  }
  .comparison-card {
    min-width: 100%;
  }
  .image-wrapper {
    height: 180px;
  }
  .stat-card {
    min-width: 140px;
  }
  .video-info {
    padding: 1.2rem;
  }
  .video-info h3 {
    font-size: 1.2rem;
  }
}
</style>
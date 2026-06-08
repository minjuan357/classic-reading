<template>
  <div class="civilization-container">
    <BreadcrumbNav :items="breadcrumbItems" />

    <h1 class="page-title">文明互鉴·黄河西行</h1>
    <p class="subtitle">展现黄河文明与世界文明的交流互鉴，体现文化自信与开放包容</p>

    <!-- 引言 -->
    <div class="intro-section">
      <div class="intro-card">
        <p class="intro-text">
          黄河，中华文明的摇篮，自西向东奔流不息。在这条大河的臂弯里，诞生了仰韶、龙山、二里头等璀璨文化。
          而河西走廊，作为丝绸之路的咽喉，将黄河文明与中亚、西亚乃至地中海文明紧密相连。
          敦煌莫高窟，便是这千年交流的永恒见证。
        </p>
      </div>
    </div>

    <!-- 敦煌莫高窟：文明交汇的宝库 -->
    <div class="content-section">
      <h2 class="section-title">🏛️ 敦煌莫高窟：千年丝路的文明宝库</h2>
      <div class="image-text-row">
        <div class="image-wrapper">
          <img src="@/assets/carousel/dunhuang.jpg" alt="敦煌莫高窟壁画商旅图" />
          <p class="image-caption">敦煌莫高窟壁画“商旅图”（数字敦煌项目）</p>
        </div>
        <div class="text-content">
          <p>
            莫高窟始建于前秦建元二年（公元366年），历经十六国、北朝、隋、唐、五代、宋、西夏、元等朝代，连续营造千年之久。
            现存洞窟735个，壁画4.5万平方米，泥质彩塑2415尊，是世界上现存规模最大、内容最丰富的佛教艺术圣地。
          </p>
          <p>
            这些壁画和彩塑融合了中原汉文化、印度佛教文化、古希腊罗马艺术、波斯萨珊艺术、突厥回鹘文化等多元因素。
            “商旅图”中描绘的胡商、骆驼、往来使者，正是丝路上文明互鉴的生动写照。
          </p>
          <p>
            敦煌藏经洞（第17窟）出土的6万余件文献，包括汉文、藏文、回鹘文、于阗文、粟特文、梵文等多种文字，
            记录了中古时期东西方宗教、语言、艺术、科技交流的丰富信息，被誉为“东方图书馆”。
          </p>
        </div>
      </div>
    </div>

    <!-- 丝绸之路：文明交流的动脉 -->
    <div class="content-section">
      <h2 class="section-title">🐫 丝绸之路：黄河文明走向世界的桥梁</h2>
      <div class="image-text-row reverse">
        <div class="image-wrapper">
          <img src="@/assets/carousel/silk_road_map.jpg" alt="丝绸之路路线图" />
          <p class="image-caption">丝绸之路路线示意图（公共领域）</p>
        </div>
        <div class="text-content">
          <p>
            汉武帝时期张骞“凿空”西域，开辟了以长安（今西安）为起点，经河西走廊、西域、中亚，直抵地中海东岸的陆上丝绸之路。
            黄河文明通过这条大动脉，将丝绸、瓷器、造纸术、印刷术、指南针、火药等伟大发明传播到西方；
            同时，也引进了葡萄、苜蓿、胡桃、石榴等农作物，以及佛教、景教、伊斯兰教等宗教文化。
          </p>
          <p>
            在文化交流中，中原的礼乐制度、书法绘画、天文历法也影响了西域诸国，形成了“你中有我，我中有你”的文化融合。
            唐代长安城中的胡商、胡姬、胡乐，正是开放包容的时代象征。
          </p>
          <p>
            宋元以后，海上丝绸之路兴起，但陆上丝路依然延续着文明对话。马可·波罗的东游，伊本·白图泰的游记，
            都记录了中国与世界的紧密联系。
          </p>
        </div>
      </div>
    </div>

    <!-- 古籍中的西域：《史记》与东西方视野 -->
    <div class="content-section">
      <h2 class="section-title">📜 古籍中的西域：《史记》与东西方视野</h2>
      <div class="image-text-row">
        <div class="image-wrapper">
          <img src="@/assets/carousel/shiji_france.jpg" alt="法国国家图书馆藏《史记》书影" />
          <p class="image-caption">法国国家图书馆藏《史记》西域相关章节书影（公共领域）</p>
        </div>
        <div class="text-content">
          <p>
            《史记·大宛列传》是中国正史中第一篇系统记载西域各国（大宛、乌孙、康居、奄蔡、大月氏、安息、条支等）的文献，
            记载了张骞出使西域的见闻，为后世了解中亚、西亚、南亚提供了珍贵史料。
          </p>
          <p>
            这部经典流传至海外，被翻译成多种文字，成为世界了解中国历史与文化的窗口。法国国家图书馆藏有珍贵的中文古籍，
            其中《史记》西域相关章节的书影，见证了中国古籍在全球的传播。
          </p>
          <p>
            与此同时，西方古典文献如《历史》《地理学》中也记载了关于“赛里斯”（丝国）的想象，
            这种双向的文本记录，构成了早期东西方相互认知的基础。
          </p>
        </div>
      </div>
    </div>

    <!-- 黄河文明与世界文明的当代对话 -->
    <div class="content-section">
      <h2 class="section-title">🌍 当代启示：文明互鉴与人类命运共同体</h2>
      <div class="image-text-row reverse">
        <div class="image-wrapper">
          <img src="@/assets/carousel/yellow_river_culture.jpg" alt="黄河壶口瀑布" />
          <p class="image-caption">黄河壶口瀑布 · 中华民族精神的象征（CC0素材）</p>
        </div>
        <div class="text-content">
          <p>
            今天的“一带一路”倡议，正是古丝绸之路精神的延续。通过政策沟通、设施联通、贸易畅通、资金融通、民心相通，
            中国与沿线国家共同推动文明对话、文化互鉴。
          </p>
          <p>
            敦煌研究院与多国机构合作开展壁画保护与数字化项目，“数字敦煌”资源库向全球免费开放，让千年艺术瑰宝触手可及。
            黄河国家文化公园的建设，将整合沿黄九省区的文化遗产，打造展示黄河文明的世界级平台。
          </p>
          <p>
            文明因多样而交流，因交流而互鉴，因互鉴而发展。黄河文明以其开放包容的胸怀，在历史上为人类文明进步作出了重要贡献，
            也必将在新时代继续为构建人类命运共同体提供东方智慧。
          </p>
        </div>
      </div>
    </div>

    <!-- 纪录片推荐：并排双列 + 详细描述 -->
    <div class="video-section">
      <h2 class="section-title">🎥 纪录片推荐：回望丝路，感知文明</h2>
      <div class="video-grid">
        <!-- 丝绸之路纪录片卡片 -->
        <div class="video-card">
          <div class="video-wrapper">
            <iframe
                src="https://player.bilibili.com/player.html?bvid=BV1Dy4y1A7gm&page=1"
                scrolling="no"
                frameborder="no"
                allowfullscreen="true"
                title="丝绸之路"
            ></iframe>
          </div>
          <div class="video-info">
            <h3>《丝绸之路》</h3>
            <p>
              中央电视台与日本NHK联合摄制的大型纪录片，首次全景式展现了丝绸之路的壮丽历史。
              影片通过实地拍摄，讲述了丝路沿线古国的兴衰、文化的交融，以及东西方文明如何在驼铃声中相遇。
              本片段聚焦于丝路上的文化交流，展现佛教艺术、商贸往来与民族融合的生动图景。
            </p>
            <p class="video-caption">央视纪录片《丝绸之路》文化交融片段（B站可观看）</p>
          </div>
        </div>

        <!-- 河西走廊纪录片卡片 -->
        <div class="video-card">
          <div class="video-wrapper">
            <iframe
                src="https://player.bilibili.com/player.html?bvid=BV1jG411379y&page=1"
                scrolling="no"
                frameborder="no"
                allowfullscreen="true"
                title="河西走廊"
            ></iframe>
          </div>
          <div class="video-info">
            <h3>《河西走廊》</h3>
            <p>
              以河西走廊为叙事核心，从汉代张骞出使西域到现代一带一路，跨越两千年历史。
              纪录片通过情景再现与专家解读，揭示了这条走廊如何成为华夏文明与西域、中亚乃至欧洲交流的枢纽。
              本片段节选了其中关于丝路贸易、文化传播的精彩内容，配以震撼的音乐与画面，再现了河西走廊的辉煌。
            </p>
            <p class="video-caption">《河西走廊》纪录片节选（B站可观看）</p>
          </div>
        </div>
      </div>
      <p class="video-note">
        ※ 视频链接为B站嵌入代码，若无法播放请尝试访问原平台。更多内容可搜索“央视纪录片《丝绸之路》”“《河西走廊》”。
      </p>
    </div>

    <!-- 结尾引用 -->
    <div class="footer-quote">
      <p>黄河奔流，文明互鉴；美美与共，天下大同。</p>
    </div>
  </div>
</template>

<script setup>
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '主题专栏', path: '/theme-topics' },
  { label: '文明互鉴·黄河西行' }
]
</script>

<style scoped>
/* 整体容器样式 */
.civilization-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
  font-family: system-ui, 'Segoe UI', Roboto, sans-serif;
}

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
  color: #6c757d;
  background: #f8f9fa;
  padding: 0.6rem 1rem;
  border-radius: 30px;
  display: inline-block;
  width: auto;
}
.breadcrumb a {
  color: #3b82f6;
  text-decoration: none;
  transition: color 0.2s;
}
.breadcrumb a:hover {
  text-decoration: underline;
  color: #1d4ed8;
}
.breadcrumb .separator {
  margin: 0 0.5rem;
  color: #9ca3af;
}
.breadcrumb .current {
  color: #2c3e50;
  font-weight: 500;
}

/* 标题与副标题 */
.page-title {
  font-size: 2.2rem;
  font-weight: 600;
  text-align: center;
  margin-bottom: 0.5rem;
  color: #2c3e50;
}
.subtitle {
  text-align: center;
  color: #5d6d7e;
  margin-bottom: 2rem;
  font-size: 1rem;
  border-bottom: 2px solid #eaeef2;
  display: inline-block;
  padding-bottom: 0.5rem;
  width: auto;
  margin-left: auto;
  margin-right: auto;
}

/* 引言卡片 */
.intro-section {
  margin-bottom: 3rem;
}
.intro-card {
  background: #f8fafc;
  border-left: 4px solid #3b82f6;
  padding: 1.2rem 2rem;
  border-radius: 20px;
  text-align: center;
}
.intro-text {
  font-size: 1.1rem;
  color: #1e293b;
  margin: 0;
  font-weight: 500;
}

/* 章节标题 */
.section-title {
  font-size: 1.8rem;
  font-weight: 500;
  margin: 2rem 0 1.5rem 0;
  padding-left: 1rem;
  border-left: 5px solid #3b82f6;
  color: #1e293b;
}

/* 图文混排行 */
.content-section {
  margin-bottom: 4rem;
}
.image-text-row {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  align-items: center;
  margin-bottom: 2rem;
}
.image-text-row.reverse {
  flex-direction: row-reverse;
}
.image-wrapper {
  flex: 1;
  min-width: 280px;
  background: #f1f5f9;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}
.image-wrapper img {
  width: 100%;
  height: auto;
  display: block;
  object-fit: cover;
}
.image-caption {
  font-size: 0.8rem;
  color: #6c757d;
  text-align: center;
  padding: 0.5rem;
  background: #f8fafc;
  margin: 0;
}
.text-content {
  flex: 1;
  font-size: 1rem;
  line-height: 1.6;
  color: #334155;
}
.text-content p {
  margin-bottom: 1rem;
}

/* 纪录片推荐区域 - 并排双列布局 */
.video-section {
  margin-bottom: 4rem;
}
.video-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
  margin-bottom: 1rem;
}
.video-card {
  background: #fff;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease;
}
.video-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
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
  padding: 1.2rem;
}
.video-info h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin: 0 0 0.75rem;
  color: #b45309;
}
.video-info p {
  margin: 0 0 0.75rem;
  line-height: 1.6;
  color: #334155;
  font-size: 0.95rem;
}
.video-caption {
  font-size: 0.85rem;
  color: #6c757d;
  margin-top: 0.5rem;
  text-align: center;
}
.video-note {
  text-align: center;
  font-size: 0.8rem;
  color: #6c757d;
  margin-top: 1rem;
}

/* 结尾引用 */
.footer-quote {
  text-align: center;
  font-size: 1.2rem;
  font-weight: 500;
  padding: 2rem 0 1rem;
  color: #3b82f6;
  border-top: 1px solid #e2e8f0;
  margin-top: 2rem;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .civilization-container {
    padding: 1rem;
  }
  .page-title {
    font-size: 1.8rem;
  }
  .section-title {
    font-size: 1.4rem;
  }
  .image-text-row, .image-text-row.reverse {
    flex-direction: column;
  }
  .image-wrapper {
    width: 100%;
  }
  .breadcrumb {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }
  .video-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  .video-info h3 {
    font-size: 1.2rem;
  }
}
</style>
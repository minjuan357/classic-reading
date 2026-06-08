<template>
  <div class="family-container">
    <BreadcrumbNav :items="breadcrumbItems" />

    <h1 class="page-title">童蒙养正·诗礼传家</h1>
    <p class="subtitle">聚焦家风家训与儿童启蒙 · 呼应“双减”后家庭对传统文化教育的关注</p>

    <!-- 引言 -->
    <div class="intro-section">
      <div class="intro-card">
        <p class="intro-text">
          家风正，则后代正；源头清，则水流清。从《颜氏家训》到《朱子家训》，千年家风如春风化雨，
          浸润着一代代中国人的精神底色。在“双减”背景下，家庭教育回归本真，让经典照亮童年。
        </p>
      </div>
    </div>

    <!-- 古代家风家训典范 -->
    <div class="ancient-family">
      <h2 class="section-title">📜 古代家风家训典范</h2>
      <div class="family-grid">
        <!-- 颜之推与《颜氏家训》 -->
        <div class="family-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/yan_zhitui.jpg" alt="颜之推像" />
            <p class="image-source">颜之推像（清代绘）· 中国国家图书馆藏</p>
          </div>
          <div class="family-info">
            <h3>颜之推 · 《颜氏家训》</h3>
            <p><strong>时代：</strong>南北朝（公元531年－约591年）</p>
            <p>
              《颜氏家训》是中国历史上第一部体系完整的家训，被誉为“家训之祖”。颜之推以自身经历告诫子孙：
              “积财千万，不如薄技在身”“与善人居，如入芝兰之室”。书中强调读书明理、勤俭持家、慎言慎行，
              对后世家庭教育影响深远。颜氏家族自南北朝至唐代，名人辈出，颜真卿即为其后裔。
            </p>
          </div>
        </div>

        <!-- 朱柏庐与《朱子家训》 -->
        <div class="family-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/zhu_bailu.jpg" alt="朱柏庐像" />
            <p class="image-source">朱柏庐像（清代绘）· 民间流传画像</p>
          </div>
          <div class="family-info">
            <h3>朱柏庐 · 《朱子家训》</h3>
            <p><strong>时代：</strong>明末清初（1627－1698）</p>
            <p>
              《朱子家训》（又名《朱柏庐治家格言》）以“黎明即起，洒扫庭除”开篇，全文仅五百余字，
              却涵盖了修身、治家、处世、为学的方方面面。“一粥一饭，当思来处不易；半丝半缕，恒念物力维艰”
              等名句，至今仍被广为传诵。朱柏庐以儒家伦理为纲，强调勤俭持家、孝悌忠信，是清代以来民间家教的范本。
            </p>
          </div>
        </div>

        <!-- 曾国藩家书 -->
        <div class="family-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/zeng_guofan.jpg" alt="曾国藩像" />
            <p class="image-source">曾国藩像（清代）· 湖南图书馆藏</p>
          </div>
          <div class="family-info">
            <h3>曾国藩 · 《曾国藩家书》</h3>
            <p><strong>时代：</strong>晚清（1811－1872）</p>
            <p>
              曾国藩家书是近代家风教育的集大成者，他通过书信教导子弟读书、做人、为官：“读书以训诂为本，
              诗文以声调为本，事亲以得欢心为本，养生以戒恼怒为本。”曾氏家族后代人才辈出，长盛不衰，
              其家训“家勤则兴，人勤则健；能勤能俭，永不贫贱”至今仍有重要价值。
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 现代“双减”政策下的家庭教育 -->
    <div class="modern-education">
      <h2 class="section-title">🏠 “双减”之后：家庭教育的回归与创新</h2>
      <div class="education-content">
        <div class="text-part">
          <p>
            2021年“双减”政策实施后，学生作业负担和校外培训负担显著减轻，家庭教育的时间与空间被重新释放。
            越来越多的家庭开始重视传统文化教育，亲子共读《三字经》《弟子规》《论语》成为新风尚。
          </p>
          <p>
            教育专家指出，家风家训是家庭教育最生动的教材。通过讲述祖先的故事、践行家规家训，
            孩子能从小建立文化认同与道德自觉。许多学校也开展“家风故事会”“家训书法展”等活动，
            将传统美德融入日常生活。
          </p>
          <p>
            数字化时代，家风传承也有了新载体：家长通过短视频平台分享家庭教育经验，在线家训课程、AI家风助手等
            创新形式，让古老智慧焕发新生。正如一位家长所言：“双减不是减责任，而是让我们更懂得如何陪伴孩子成长。”
          </p>
        </div>
        <div class="image-part">
          <img src="@/assets/carousel/parent_child_reading.jpg" alt="亲子共读古籍" />
          <p class="image-source">亲子共读古籍温馨照片（公益授权）</p>
        </div>
      </div>
    </div>

    <!-- 视频区域：家风家训纪录片推荐 -->
    <div class="video-section">
      <h2 class="section-title">🎥 视频推荐：探寻家风的力量</h2>
      <div class="video-card">
        <div class="video-wrapper">
          <iframe
              src="https://player.bilibili.com/player.html?bvid=BV1qW411V7rC&page=1"
              scrolling="no"
              frameborder="no"
              allowfullscreen="true"
              title="家风纪录片"
          ></iframe>
        </div>
        <div class="video-info">
          <h3>《家风》纪录片（节选）</h3>
          <p>
            中央电视台纪录片《家风》以历史为脉络，讲述颜氏家训、朱子家训、曾国藩家书等经典家风故事，
            并探寻当代家庭如何传承优良家风。本片通过鲜活的人物与事例，揭示家风对个人成长、家族兴旺的深远影响。
          </p>
          <p>
            在“双减”背景下，如何让家训“活”起来？纪录片中展示了许多现代家庭的做法：家规上墙、家风故事会、
            亲子共读经典……这些实践为家庭教育提供了宝贵借鉴。
          </p>
          <p class="video-caption">视频来源：央视纪录片《家风》（B站可观看）</p>
        </div>
      </div>
      <p class="video-note">
        ※ 若视频无法播放，请尝试访问原平台。更多内容可搜索“央视纪录片 家风”。
      </p>
    </div>

    <!-- 家风家训小故事 -->
    <div class="story-section">
      <h2 class="section-title">📖 当代家风故事</h2>
      <div class="story-card">
        <p><strong>浙江郑氏家族：</strong>传承800年的“郑义门”家规，168条家训涵盖孝义、廉洁、耕读，族中173人出仕无一人贪腐，成为中国家训典范。</p>
        <p><strong>陕西党氏家族：</strong>以“读书明理，勤俭持家”为家训，三代人接力修建乡村图书馆，让书香浸润乡邻，荣获“全国文明家庭”称号。</p>
        <p><strong>上海“家训文化节”：</strong>每年举办，家长与孩子共同书写家训、讲述家风故事，将传统美德融入社区生活。</p>
      </div>
    </div>

    <!-- 结尾 -->
    <div class="footer-quote">
      <p>家风正，子孙兴；诗礼传，家道昌。</p>
    </div>
  </div>
</template>

<script setup>
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '主题专栏', path: '/theme-topics' },
  { label: '童蒙养正·诗礼传家' }
]
</script>

<style scoped>
/* 整体布局与体育古韵、文明互鉴保持一致 */
.family-container {
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

.section-title {
  font-size: 1.8rem;
  font-weight: 500;
  margin: 2rem 0 1.5rem 0;
  padding-left: 1rem;
  border-left: 5px solid #3b82f6;
  color: #1e293b;
}

/* 古代家风卡片网格 */
.family-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}
.family-card {
  background: #ffffff;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease;
}
.family-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}
.image-wrapper {
  width: 100%;
  height: 260px;          /* 稍微增加高度，适应不同图片比例 */
  overflow: hidden;
  background: #f1f5f9;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: contain;    /* 关键修改：保证图片完整显示，不裁剪 */
  transition: transform 0.3s ease;
  background-color: #eaeef2; /* 与容器背景融合的底色 */
}
.family-card:hover .image-wrapper img {
  transform: scale(1.02);
}
.image-source {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-size: 0.7rem;
  padding: 0.2rem 0.5rem;
  margin: 0;
  text-align: center;
  pointer-events: none;
  z-index: 2;
}
.family-info {
  padding: 1.2rem;
}
.family-info h3 {
  font-size: 1.3rem;
  margin: 0 0 0.5rem;
  color: #b45309;
}
.family-info p {
  margin: 0.5rem 0;
  line-height: 1.5;
  color: #334155;
  font-size: 0.95rem;
}

/* 现代教育区域 */
.modern-education {
  margin-bottom: 3rem;
}
.education-content {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  align-items: center;
  background: #fef9e6;
  border-radius: 28px;
  padding: 1.5rem;
}
.text-part {
  flex: 2;
  min-width: 250px;
}
.text-part p {
  margin: 1rem 0;
  line-height: 1.6;
}
.image-part {
  flex: 1;
  min-width: 200px;
  text-align: center;
}
.image-part img {
  width: 100%;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.image-part .image-source {
  position: static;
  background: transparent;
  color: #6c757d;
  font-size: 0.75rem;
  margin-top: 0.5rem;
}

/* 视频区域卡片 */
.video-section {
  margin-bottom: 3rem;
}
.video-card {
  background: #ffffff;
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
  padding-bottom: 56.25%;
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
  padding: 1.5rem;
}
.video-info h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin: 0 0 1rem;
  color: #b45309;
}
.video-info p {
  margin: 0 0 1rem;
  line-height: 1.6;
  color: #334155;
  font-size: 1rem;
}
.video-caption {
  font-size: 0.85rem;
  color: #6c757d;
  text-align: center;
  margin-top: 1rem;
  padding-top: 0.5rem;
  border-top: 1px solid #eaeef2;
}
.video-note {
  text-align: center;
  font-size: 0.8rem;
  color: #6c757d;
  margin-top: 1rem;
}

/* 故事区域 */
.story-section {
  margin-bottom: 3rem;
}
.story-card {
  background: #fef9e6;
  border-radius: 20px;
  padding: 1.8rem;
  border-left: 4px solid #eab308;
}
.story-card p {
  margin: 0.8rem 0;
  line-height: 1.6;
  color: #2c3e50;
}

/* 结尾 */
.footer-quote {
  text-align: center;
  font-size: 1.2rem;
  font-weight: 500;
  padding: 2rem 0 1rem;
  color: #3b82f6;
  border-top: 1px solid #e2e8f0;
  margin-top: 2rem;
}

/* 响应式 */
@media (max-width: 768px) {
  .family-container {
    padding: 1rem;
  }
  .page-title {
    font-size: 1.8rem;
  }
  .section-title {
    font-size: 1.4rem;
  }
  .education-content {
    flex-direction: column;
  }
  .family-grid {
    grid-template-columns: 1fr;
  }
  .image-wrapper {
    height: 200px;        /* 移动端稍降低高度，但仍保持比例 */
  }
  .breadcrumb {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }
  .video-info {
    padding: 1rem;
  }
  .video-info h3 {
    font-size: 1.2rem;
  }
}
</style>
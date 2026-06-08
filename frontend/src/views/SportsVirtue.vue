<template>
  <div class="sports-container">
    <BreadcrumbNav :items="breadcrumbItems" />

    <h1 class="page-title">翰墨传家·体育古韵</h1>
    <p class="subtitle">挖掘古籍中的体育精神，弘扬“强身健体、修身养性”的传统美德</p>

    <!-- 引言 -->
    <div class="intro-section">
      <div class="intro-card">
        <p class="intro-text">
          从射礼、蹴鞠到导引术，中国古代体育不仅是竞技与娱乐，更是礼教、养生与人格修养的载体。
          今天，我们以八段锦为代表的传统体育依然焕发着蓬勃生机。
        </p>
      </div>
    </div>

    <!-- 古代体育鼎盛时期举例 -->
    <div class="ancient-sports">
      <h2 class="section-title">🏹 古代体育的黄金时代</h2>
      <div class="sports-grid">
        <!-- 射礼（周代-明代） -->
        <div class="sport-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/archery_ming.jpg" alt="明代《三才图会》射箭插图" />
            <p class="image-source">明代《三才图会》· 中国国家图书馆数字古籍平台</p>
          </div>
          <div class="sport-info">
            <h3>射礼 · 周代至明代</h3>
            <p><strong>鼎盛时期：</strong>西周至春秋，射礼被列为“六艺”之一，是贵族教育的重要内容。明清时期民间射箭活动依然盛行。</p>
            <p>“内志正，外体直”，射箭不仅是技艺，更是修身之道。</p>
          </div>
        </div>

        <!-- 蹴鞠（汉代-宋代） -->
        <div class="sport-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/cuju_han.jpg" alt="汉代蹴鞠画像石拓片" />
            <p class="image-source">汉代画像石拓片 · 山东博物馆藏</p>
          </div>
          <div class="sport-info">
            <h3>蹴鞠 · 汉代至宋代</h3>
            <p><strong>鼎盛时期：</strong>汉代蹴鞠用于军事训练，宋代出现专业蹴鞠组织“齐云社”，宋徽宗更是蹴鞠爱好者。</p>
            <p>古代足球强调团队协作，与今日足球精神一脉相承。</p>
          </div>
        </div>

        <!-- 导引术（汉代-清代） -->
        <div class="sport-card">
          <div class="image-wrapper">
            <img src="@/assets/carousel/daoyin_huatuo.jpg" alt="五禽戏导引图" />
            <p class="image-source">马王堆汉墓帛书《导引图》摹本 · 湖南省博物馆</p>
          </div>
          <div class="sport-info">
            <h3>导引术 · 汉代至清代</h3>
            <p><strong>鼎盛时期：</strong>汉代《导引图》已记载44种动作；华佗创编“五禽戏”，成为中医养生经典。</p>
            <p>导引术融合呼吸与肢体运动，是中医“治未病”的重要方法。</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 现代传承：八段锦 -->
    <div class="modern-baduanjin">
      <h2 class="section-title">🧘 现代传承：八段锦——从古代导引到全民健身</h2>
      <div class="baduanjin-content">
        <div class="text-part">
          <p>
            八段锦起源于宋代，由八组动作组成，动作舒展优美，如“双手托天理三焦”“左右开弓似射雕”。
            它融合了导引术的精华，强调“形、气、神”的统一，是中医养生与体育结合的典范。
          </p>
          <p>
            如今，八段锦被国家体育总局列为全民健身项目，走进校园、社区、企业，成为老少皆宜的传统体育。
            每天练习10分钟，即可疏通经络、强健筋骨，缓解现代人的亚健康状态。
          </p>
        </div>
        <div class="image-part">
          <img src="@/assets/carousel/baduanjin_modern.jpg" alt="当代青少年习练八段锦" />
          <p class="image-source">当代青少年习练八段锦实拍 · CC0协议素材</p>
        </div>
      </div>
    </div>

    <!-- 八段锦教学视频（卡片样式，视频+详细说明） -->
    <div class="video-section">
      <h2 class="section-title">📹 八段锦标准教学视频</h2>
      <div class="video-card">
        <div class="video-wrapper">
          <iframe
              src="https://player.bilibili.com/player.html?bvid=BV1gT4y1m7ec&page=1"
              scrolling="no"
              frameborder="no"
              allowfullscreen="true"
              title="健身气功八段锦完整版-带呼吸法口令版"
          ></iframe>
        </div>
        <div class="video-info">
          <h3>《健身气功·八段锦》国家体育总局标准教学</h3>
          <p>
            八段锦是一套独立而完整的健身功法，起源于北宋，至今已有八百余年历史。古人将这套动作比喻为“锦”，意为五颜六色，美而华贵，体现其动作舒展优美，如锦缎般柔顺。八段锦分为八式，每式都有特定的养生功效：
          </p>
          <ul>
            <li><strong>第一式：双手托天理三焦</strong> —— 调理三焦，通调水道，缓解疲劳。</li>
            <li><strong>第二式：左右开弓似射雕</strong> —— 扩胸展肩，改善心肺功能。</li>
            <li><strong>第三式：调理脾胃须单举</strong> —— 升降脾胃，助消化吸收。</li>
            <li><strong>第四式：五劳七伤往后瞧</strong> —— 活动颈椎，缓解肩颈僵硬。</li>
            <li><strong>第五式：摇头摆尾去心火</strong> —— 强腰健肾，降心火。</li>
            <li><strong>第六式：两手攀足固肾腰</strong> —— 强腰壮肾，拉伸脊柱。</li>
            <li><strong>第七式：攒拳怒目增气力</strong> —— 疏肝理气，增强体力。</li>
            <li><strong>第八式：背后七颠百病消</strong> —— 震动全身，调和气血。</li>
          </ul>
          <p>
            本视频为带呼吸口令的完整版，适合初学者跟练。每天坚持练习，不仅能强身健体，还能调节情绪、改善睡眠。在现代快节奏生活中，八段锦以其简单易学、场地不限的特点，成为上班族、中老年人乃至青少年的理想健身选择。
          </p>
          <p class="video-caption">视频来源：B站“健身气功八段锦完整版-带呼吸法口令版”（国家体育总局推广视频）</p>
        </div>
      </div>
      <p class="video-note">
        ※ 若视频无法播放，请尝试访问原平台，或搜索“国家体育总局 八段锦”获取官方教学资源。
      </p>
    </div>

    <!-- 体育意义与美德总结 -->
    <div class="conclusion-section">
      <h2 class="section-title">💪 体育精神与美德传承</h2>
      <div class="conclusion-card">
        <p><strong>强身健体</strong> —— 传统体育强调“内外兼修”，通过运动增强体质，培养坚韧意志。</p>
        <p><strong>修身养性</strong> —— 射礼的“正心”、八段锦的“调息”，都是通过体育实现精神修养。</p>
        <p><strong>文化自信</strong> —— 传统体育承载着中华文化基因，是文化传承的活态载体。</p>
        <p><strong>时代价值</strong> —— 从古代六艺到现代全民健身，传统体育始终与时代同行，为健康中国注入东方智慧。</p>
      </div>
    </div>

    <!-- 结尾 -->
    <div class="footer-quote">
      <p>承古开新，体育育人；强健体魄，德润人心。</p>
    </div>
  </div>
</template>

<script setup>
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '主题专栏', path: '/theme-topics' },
  { label: '翰墨传家·体育古韵' }
]
</script>

<style scoped>
/* 整体布局与知识科普页保持一致，新增卡片网格样式 */
.sports-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
  font-family: system-ui, 'Segoe UI', Roboto, sans-serif;
}

/* 面包屑导航样式 */
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

/* 古代体育卡片网格 */
.sports-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}
.sport-card {
  background: #ffffff;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s ease;
}
.sport-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}
.image-wrapper {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f1f5f9;
  position: relative;
}
.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.sport-card:hover .image-wrapper img {
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
}
.sport-info {
  padding: 1.2rem;
}
.sport-info h3 {
  font-size: 1.3rem;
  margin: 0 0 0.5rem;
  color: #b45309;
}
.sport-info p {
  margin: 0.5rem 0;
  line-height: 1.5;
  color: #334155;
  font-size: 0.95rem;
}

/* 现代八段锦区域 */
.modern-baduanjin {
  margin-bottom: 3rem;
}
.baduanjin-content {
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

/* 视频区域（卡片样式） */
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
  padding-bottom: 56.25%; /* 16:9 宽屏比例 */
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
.video-info ul {
  margin: 0 0 1rem;
  padding-left: 1.5rem;
}
.video-info li {
  margin-bottom: 0.5rem;
  line-height: 1.5;
  color: #334155;
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

/* 总结卡片 */
.conclusion-section {
  margin-bottom: 2rem;
}
.conclusion-card {
  background: #eef2ff;
  border-radius: 24px;
  padding: 1.8rem;
  border-left: 4px solid #3b82f6;
}
.conclusion-card p {
  margin: 1rem 0;
  line-height: 1.6;
  font-size: 1rem;
  color: #1e293b;
}
.conclusion-card strong {
  color: #b45309;
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
  .sports-container {
    padding: 1rem;
  }
  .page-title {
    font-size: 1.8rem;
  }
  .section-title {
    font-size: 1.4rem;
  }
  .baduanjin-content {
    flex-direction: column;
  }
  .sports-grid {
    grid-template-columns: 1fr;
  }
  .image-wrapper {
    height: 180px;
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
  .video-info ul {
    padding-left: 1rem;
  }
}
</style>
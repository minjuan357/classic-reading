import { createRouter, createWebHashHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import BookList from '@/views/BookList.vue'
import BookDetail from '@/views/BookDetail.vue'
import ChapterDetail from '@/views/ChapterDetail.vue'
import Community from '@/views/Community.vue'
import ConversationDetail from '@/views/ConversationDetail.vue'
import Knowledge from '@/views/Knowledge.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import ForgotPassword from '@/views/ForgotPassword.vue'
// 新增主题相关页面
import ThemeTopics from '@/views/ThemeTopics.vue'
import SportsVirtue from '@/views/SportsVirtue.vue'
import CivilizationExchange from '@/views/CivilizationExchange.vue'
import FamilyEducation from '@/views/FamilyEducation.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/books',
        name: 'BookList',
        component: BookList,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/books/:id',
        name: 'BookDetail',
        component: BookDetail,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/chapters/:id',
        name: 'ChapterDetail',
        component: ChapterDetail,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/knowledge',
        name: 'Knowledge',
        component: Knowledge,   // ✅ 正确，不是外部链接
        meta: { title: '知识科普', hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/community',
        name: 'Community',
        component: Community,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/community/conversation/:id',
        name: 'ConversationDetail',
        component: ConversationDetail,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: false }
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: false }
    },
    {
        path: '/forgot-password',
        name: 'ForgotPassword',
        component: ForgotPassword,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: false }
    },
    {
        path: '/history',
        name: 'History',
        component: () => import('@/views/History.vue'),
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/bookshelf',
        redirect: '/favorites'
    },
    // 在router/index.js中修改
    {
        path: '/favorites',
        name: 'Favorites',
        component: () => import('@/views/Likes.vue'),
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/ai',
        name: 'Ai',
        component: () => import('@/views/Ai.vue'),
        meta: { hideNavbar: true, hideFooter: true, showSidebar: false }
    },
    // 主题栏目相关路由
    {
        path: '/theme-topics',
        name: 'ThemeTopics',
        component: ThemeTopics,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/theme/sports-virtue',
        name: 'SportsVirtue',
        component: SportsVirtue,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/theme/civilization-exchange',
        name: 'CivilizationExchange',
        component: CivilizationExchange,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/theme/family-education',
        name: 'FamilyEducation',
        component: FamilyEducation,
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    },
    {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { hideNavbar: false, hideFooter: false, showSidebar: true }
    }
]

const router = createRouter({
    history: createWebHashHistory(),  // 改为哈希模式，用于测试 history 模式下的跳转问题
    routes,
    scrollBehavior() {
      return { top: 0 }
    }
})

// 页面标题映射
const titleMap = {
    'Home': '经典重读·AI伴学',
    'BookList': '典籍库',
    'BookDetail': '典籍详情',
    'ChapterDetail': '阅读',
    'Knowledge': '知识科普',
    'Community': '社区',
    'ConversationDetail': '讨论详情',
    'Login': '登录',
    'Register': '注册',
    'ForgotPassword': '忘记密码',
    'History': '浏览历史',
    'Favorites': '我的收藏',
    'Ai': 'AI助手',
    'ThemeTopics': '主题专栏',
    'SportsVirtue': '体育古韵',
    'CivilizationExchange': '文明互鉴',
    'FamilyEducation': '诗礼传家',
    'Profile': '个人中心'
}

router.afterEach((to) => {
    const name = to.name
    document.title = titleMap[name] || '经典重读·AI伴学'
})

export default router
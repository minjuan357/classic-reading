// src/utils/bookCover.js

// 分类配色
const categoryPalette = {
    '经': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '儒家经典' },
    '史': { bg: '#E6ECEF', accent: '#3F5566', sub: '历史典籍' },
    '子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '诸子百家' },
    '集': { bg: '#F2E8E4', accent: '#7B4E45', sub: '文学作品' },
    '诗词': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '诗词雅韵' }
}

// 书籍特定配色
const bookPalette = {
    // 经部
    '诗经': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '风雅颂' },
    '论语': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '儒家经典' },
    '孟子': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '儒家经典' },
    '大学': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '儒家经典' },
    '中庸': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '儒家经典' },
    '周易': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '群经之首' },
    '尚书': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '上古之书' },
    '礼记': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '礼制典籍' },
    '春秋': { bg: '#EFE4CF', accent: '#7A5C3E', sub: '编年史书' },

    // 史部
    '史记': { bg: '#E6ECEF', accent: '#3F5566', sub: '史家绝唱' },
    '资治通鉴': { bg: '#E6ECEF', accent: '#3F5566', sub: '史家绝唱' },
    '汉书': { bg: '#E6ECEF', accent: '#3F5566', sub: '断代史祖' },
    '后汉书': { bg: '#E6ECEF', accent: '#3F5566', sub: '范晔著作' },
    '三国志': { bg: '#E6ECEF', accent: '#3F5566', sub: '三国史书' },

    // 子部
    '老子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '道家经典' },
    '庄子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '道家经典' },
    '韩非子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '法家经典' },
    '墨子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '墨家经典' },
    '荀子': { bg: '#EAE7F4', accent: '#4F4A78', sub: '儒家发展' },

    // 诗词类
    '唐诗三百首': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '唐诗精选' },
    '宋词三百首': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '宋词精选' },
    '元曲三百首': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '元曲精选' },
    '楚辞': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '楚地诗歌' },
    '乐府诗集': { bg: '#F7EAD7', accent: '#8A5A2A', sub: '乐府民歌' }
}

// HTML转义
function escapeHtml(str) {
    if (!str) return ''
    return String(str).replace(/[&<>]/g, function(m) {
        if (m === '&') return '&amp;'
        if (m === '<') return '&lt;'
        if (m === '>') return '&gt;'
        return m
    })
}

// 处理书名显示
function formatTitle(title) {
    if (!title) return ['经典', '典籍']

    const cleanTitle = title.trim()

    if (cleanTitle.length <= 8) {
        return [cleanTitle, '']
    }

    if (cleanTitle.length <= 16) {
        // 尝试在合适的位置分割
        if (cleanTitle.includes('·')) {
            const parts = cleanTitle.split('·')
            return [parts[0], parts[1] || '']
        }
        if (cleanTitle.includes(' ')) {
            const parts = cleanTitle.split(' ')
            return [parts[0], parts[1] || '']
        }
        // 平均分割
        const mid = Math.floor(cleanTitle.length / 2)
        return [cleanTitle.slice(0, mid), cleanTitle.slice(mid)]
    }

    return [cleanTitle.slice(0, 8), cleanTitle.slice(8, 16)]
}

// 生成SVG封面
export function generateSvgCover(title, author, categoryName) {
    console.log('生成封面:', { title, author, categoryName })

    // 获取配色
    let palette = bookPalette[title]

    if (!palette) {
        palette = categoryPalette[categoryName]
    }

    if (!palette) {
        // 默认配色
        palette = { bg: '#F5F5F5', accent: '#8B7355', sub: '经典阅读' }
    }

    const [line1, line2] = formatTitle(title)

    let authorText = author || '佚名'
    if (authorText.length > 12) {
        authorText = authorText.slice(0, 10) + '...'
    }

    const bgColor = palette.bg
    const accentColor = palette.accent
    const subText = palette.sub

    // 生成SVG
    const svgContent = `<svg xmlns="http://www.w3.org/2000/svg" width="300" height="400" viewBox="0 0 300 400">
        <defs>
            <linearGradient id="grad" x1="0%" y1="0%" x2="0%" y2="100%">
                <stop offset="0%" stop-color="${bgColor}"/>
                <stop offset="100%" stop-color="#FFFFFF"/>
            </linearGradient>
        </defs>
        <rect width="300" height="400" fill="url(#grad)"/>
        <rect x="15" y="15" width="270" height="370" rx="12" fill="none" stroke="${accentColor}" stroke-width="1.5" stroke-opacity="0.3"/>
        <rect x="35" y="45" width="4" height="310" fill="${accentColor}" opacity="0.5"/>
        <text x="55" y="120" font-size="32" font-weight="bold" fill="${accentColor}" font-family="'KaiTi', '华文楷书', 'STKaiti', serif">${escapeHtml(line1)}</text>
        ${line2 ? `<text x="55" y="170" font-size="32" font-weight="bold" fill="${accentColor}" font-family="'KaiTi', '华文楷书', 'STKaiti', serif">${escapeHtml(line2)}</text>` : ''}
        <text x="55" y="320" font-size="18" fill="${accentColor}" font-family="'SimSun', '宋体', serif">${escapeHtml(authorText)}</text>
        <text x="55" y="360" font-size="14" fill="${accentColor}" opacity="0.7" font-family="'SimSun', '宋体', serif">${escapeHtml(subText)}</text>
        <rect x="55" y="385" width="190" height="1" fill="${accentColor}" opacity="0.3"/>
    </svg>`

    const encodedSvg = encodeURIComponent(svgContent)
    return `data:image/svg+xml;charset=utf-8,${encodedSvg}`
}

// 主函数
export function resolveBookCover(book = {}, categoryName = '') {
    // 如果有外部封面，使用外部封面
    if (book.coverUrl && book.coverUrl.trim()) {
        return book.coverUrl
    }

    // 获取书名
    const title = book.title || '经典典籍'

    // 获取作者
    let author = book.author || '佚名'

    // 获取分类
    const category = categoryName || book.categoryName || ''

    // 生成并返回封面
    return generateSvgCover(title, author, category)
}
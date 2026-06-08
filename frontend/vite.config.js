import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './src')
        }
    },
    server: {
        host: '0.0.0.0',
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // 与后端端口保持一致
                changeOrigin: true,
                secure: false, // 新增：允许访问 HTTP 本地服务
                rewrite: (path) => path.replace(/^\/api/, '/api') // 路径不变
            }
        }
    }
})
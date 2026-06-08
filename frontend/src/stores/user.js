import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
    const token = ref(localStorage.getItem('token') || null)
    const username = ref(localStorage.getItem('username') || null)
    const userId = ref(localStorage.getItem('userId') || null)

    const isLoggedIn = computed(() => !!token.value)

    const setUser = (userData) => {
        token.value = userData.token
        username.value = userData.username
        userId.value = userData.id

        localStorage.setItem('token', userData.token)
        localStorage.setItem('username', userData.username)
        localStorage.setItem('userId', userData.id)
    }

    const logout = () => {
        token.value = null
        username.value = null
        userId.value = null

        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('userId')
    }

    return {
        token,
        username,
        userId,
        isLoggedIn,
        setUser,
        logout
    }
})
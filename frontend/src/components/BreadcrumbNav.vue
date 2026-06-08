<template>
  <div class="breadcrumb-wrapper" v-if="items.length">
    <el-breadcrumb separator="›">
      <el-breadcrumb-item v-for="(item, index) in items" :key="index">
        <router-link v-if="item.path && index < items.length - 1" :to="item.path">
          {{ item.label }}
        </router-link>
        <span v-else>{{ item.label }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup>
defineProps({
  items: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.breadcrumb-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 12px 20px 0;
}
.breadcrumb-wrapper :deep(.el-breadcrumb) {
  font-size: 14px;
}
.breadcrumb-wrapper :deep(.el-breadcrumb__inner a),
.breadcrumb-wrapper :deep(.el-breadcrumb__inner.is-link) {
  color: #8b6b4d;
  font-weight: 500;
}
.breadcrumb-wrapper :deep(.el-breadcrumb__inner a:hover),
.breadcrumb-wrapper :deep(.el-breadcrumb__inner.is-link:hover) {
  color: #6f523b;
}
.breadcrumb-wrapper :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #a28b72;
  font-weight: 400;
}
</style>

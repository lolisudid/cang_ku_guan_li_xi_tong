<template>
  <el-container style="height:100vh;">
    <el-aside width="220px" style="background:#304156;">
      <div style="height:60px; line-height:60px; text-align:center; color:#fff; font-size:18px; font-weight:bold; border-bottom:1px solid #1f2d3d;">
        仓库管理系统 V1.0
      </div>
      <el-menu
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
      >
        <el-menu-item index="/dashboard">📊 首页</el-menu-item>
        <el-menu-item index="/goods" v-if="showGoods">📦 商品管理</el-menu-item>
        <el-menu-item index="/warehouses" v-if="showWarehouse">🏭 仓库管理</el-menu-item>
        <el-menu-item index="/instock" v-if="showInStock">📥 入库管理</el-menu-item>
        <el-menu-item index="/outstock" v-if="showOutStock">📤 出库管理</el-menu-item>
        <el-menu-item index="/inventory">📋 库存查询</el-menu-item>
        <el-menu-item index="/suppliers" v-if="showSupplier">🤝 供应商管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff; border-bottom:1px solid #e6e6e6; display:flex; align-items:center; justify-content:space-between; padding:0 20px;">
        <span style="font-size:16px; font-weight:bold;">{{ route.meta.title }}</span>
        <div>
          <el-tag type="info" size="small">{{ role }}</el-tag>
          <span style="margin:0 12px;">{{ staffName }}</span>
          <el-button type="danger" size="small" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main style="background:#f0f2f5;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const staffName = localStorage.getItem('staffName') || ''
const role = localStorage.getItem('staffRole') || ''

const showGoods = computed(() => role === '管理员' || role === '采购员')
const showWarehouse = computed(() => role === '管理员')
const showInStock = computed(() => role === '采购员' || role === '仓库管理员')
const showOutStock = computed(() => role === '仓库管理员')
const showSupplier = computed(() => role === '采购员')

function logout() {
  localStorage.clear()
  router.push('/login')
}
</script>

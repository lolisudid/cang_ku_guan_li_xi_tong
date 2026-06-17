<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px;">
      <el-col :span="6">
        <el-card><div style="text-align:center;"><h2>{{ goodsCount }}</h2><span>商品总数</span></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="text-align:center;"><h2>{{ warehouseCount }}</h2><span>仓库数量</span></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="text-align:center;"><h2>{{ supplierCount }}</h2><span>供应商数量</span></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="text-align:center;"><h2>{{ warningCount }}</h2><span>低库存预警</span></div></el-card>
      </el-col>
    </el-row>
    <el-card title="欢迎使用" style="margin-top:20px;">
      <h4>欢迎使用超市货物仓库管理系统 V1.0</h4>
      <p>当前登录：{{ staffName }}（{{ role }}）</p>
      <p>系统提供商品管理、仓库管理、入库/出库管理、库存查询、供应商管理等核心功能。</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getGoods, getWarehouses, getSuppliers, getInventory } from '../api'

const staffName = localStorage.getItem('staffName') || ''
const role = localStorage.getItem('staffRole') || ''
const goodsCount = ref(0)
const warehouseCount = ref(0)
const supplierCount = ref(0)
const warningCount = ref(0)

onMounted(async () => {
  try {
    const [gRes, wRes, sRes, iRes] = await Promise.all([
      getGoods({ page: 0, size: 1 }),
      getWarehouses(),
      getSuppliers({}),
      getInventory()
    ])
    goodsCount.value = gRes.data.totalElements || 0
    warehouseCount.value = wRes.data.length || 0
    supplierCount.value = sRes.data.length || 0
    warningCount.value = iRes.data.filter(item => item.goods && item.quantity <= item.goods.threshold).length
  } catch { /* ignore */ }
})
</script>

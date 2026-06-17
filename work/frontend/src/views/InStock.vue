<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content:space-between;">
        <span>入库管理</span>
        <el-button type="primary" size="small" @click="openDialog">新增入库</el-button>
      </div>
    </template>
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="入库单号" width="110" />
      <el-table-column label="商品" min-width="120">
        <template #default="{ row }">{{ row.goods?.name || row.goodsId }}</template>
      </el-table-column>
      <el-table-column label="仓库" width="120">
        <template #default="{ row }">{{ row.warehouse?.name || row.warehouseId }}</template>
      </el-table-column>
      <el-table-column prop="quantity" label="数量" width="80" />
      <el-table-column prop="date" label="入库时间" width="170" />
      <el-table-column prop="staffId" label="操作员" width="90" />
    </el-table>
    <el-pagination
      v-model:current-page="page"
      v-model:page-size="size"
      :total="total"
      layout="prev, pager, next"
      style="margin-top:16px; justify-content:center;"
      @current-change="fetchData"
    />
    <el-dialog v-model="dialogVisible" title="新增入库单" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="入库单号"><el-input v-model="form.id" /></el-form-item>
        <el-form-item label="商品编号"><el-input v-model="form.goodsId" /></el-form-item>
        <el-form-item label="仓库编号"><el-input v-model="form.warehouseId" /></el-form-item>
        <el-form-item label="供应商编号"><el-input v-model="form.supplierId" /></el-form-item>
        <el-form-item label="入库数量"><el-input-number v-model="form.quantity" :min="1" style="width:100%;" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInStock, createInStock } from '../api'
import { ElMessage } from 'element-plus'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await getInStock({ page: page.value - 1, size: size.value })
    list.value = res.data.content || []
    total.value = res.data.totalElements || 0
  } catch { /* ignore */ }
  finally { loading.value = false }
}

function openDialog() {
  form.value = { id: `I${Date.now()}`, goodsId: '', warehouseId: '', supplierId: '', quantity: 1 }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    form.value.staffId = localStorage.getItem('staffId')
    await createInStock(form.value)
    ElMessage.success('入库成功，库存已更新')
    dialogVisible.value = false
    fetchData()
  } catch (e) { ElMessage.error(e.response?.data?.message || '操作失败') }
  finally { saving.value = false }
}

onMounted(fetchData)
</script>

<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content:space-between;">
        <span>供应商管理</span>
        <el-button type="primary" size="small" @click="openDialog">新增供应商</el-button>
      </div>
    </template>
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="name" label="名称" min-width="150" />
      <el-table-column prop="contact" label="联系人" width="120" />
      <el-table-column prop="phone" label="电话" width="140" />
      <el-table-column prop="address" label="地址" min-width="200" />
      <el-table-column label="操作" width="80" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" title="新增供应商" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="编号"><el-input v-model="form.id" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contact" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
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
import { getSuppliers, createSupplier, deleteSupplier } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await getSuppliers({})
    list.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
}

function openDialog() {
  form.value = { id: '', name: '', contact: '', phone: '', address: '' }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    await createSupplier(form.value)
    ElMessage.success('新增成功')
    dialogVisible.value = false
    fetchData()
  } catch (e) { ElMessage.error(e.response?.data?.message || '操作失败') }
  finally { saving.value = false }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该供应商吗？')
    await deleteSupplier(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* ignore */ }
}

onMounted(fetchData)
</script>

<template>
  <el-card>
    <template #header>
      <div style="display:flex; justify-content:space-between;">
        <span>仓库管理</span>
        <el-button type="primary" size="small" @click="openDialog(null)">新增仓库</el-button>
      </div>
    </template>
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="name" label="名称" min-width="150" />
      <el-table-column prop="address" label="地址" min-width="200" />
      <el-table-column prop="capacity" label="容量" width="100" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑仓库' : '新增仓库'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="编号"><el-input v-model="form.id" :disabled="isEdit" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="容量"><el-input-number v-model="form.capacity" :min="1" style="width:100%;" /></el-form-item>
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
import { getWarehouses, createWarehouse, updateWarehouse, deleteWarehouse } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await getWarehouses()
    list.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
}

function openDialog(row) {
  isEdit.value = !!row
  form.value = row ? { ...row } : { id: '', name: '', address: '', capacity: 100 }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateWarehouse(form.value.id, form.value)
      ElMessage.success('修改成功')
    } else {
      await createWarehouse(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) { ElMessage.error(e.response?.data?.message || '操作失败') }
  finally { saving.value = false }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该仓库吗？')
    await deleteWarehouse(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* ignore */ }
}

onMounted(fetchData)
</script>

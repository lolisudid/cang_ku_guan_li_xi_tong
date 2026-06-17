<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between;">
          <span>商品管理</span>
          <el-button type="primary" size="small" @click="openDialog(null)">新增商品</el-button>
        </div>
      </template>
      <el-input v-model="searchName" placeholder="按名称搜索" clearable style="width:300px; margin-bottom:16px;" @input="fetchData" />
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column prop="name" label="名称" min-width="160" />
        <el-table-column prop="spec" label="规格" width="120" />
        <el-table-column prop="unit" label="单位" width="70" />
        <el-table-column prop="price" label="售价" width="90" />
        <el-table-column prop="threshold" label="预警阈值" width="100" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="prev, pager, next"
        style="margin-top:16px; justify-content:center;"
        @current-change="fetchData"
      />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '新增商品'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="编号"><el-input v-model="form.id" :disabled="isEdit" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="规格"><el-input v-model="form.spec" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
        <el-form-item label="售价"><el-input-number v-model="form.price" :min="0.01" :precision="2" style="width:100%;" /></el-form-item>
        <el-form-item label="阈值"><el-input-number v-model="form.threshold" :min="0" style="width:100%;" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getGoods, createGoods, updateGoods, deleteGoods } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const loading = ref(false)
const searchName = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await getGoods({ name: searchName.value || undefined, page: page.value - 1, size: size.value })
    list.value = res.data.content || []
    total.value = res.data.totalElements || 0
  } catch { /* ignore */ }
  finally { loading.value = false }
}

function openDialog(row) {
  isEdit.value = !!row
  form.value = row ? { ...row } : { id: '', name: '', spec: '', unit: '', price: 0, threshold: 10 }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateGoods(form.value.id, form.value)
      ElMessage.success('修改成功')
    } else {
      await createGoods(form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) { ElMessage.error(e.response?.data?.message || '操作失败') }
  finally { saving.value = false }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？')
    await deleteGoods(id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* ignore */ }
}

onMounted(fetchData)
</script>

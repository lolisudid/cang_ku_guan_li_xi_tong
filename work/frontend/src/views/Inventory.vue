<template>
  <el-card>
    <template #header><span>库存查询</span></template>
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column label="商品编号" prop="goodsId" width="100" />
      <el-table-column label="商品名称" min-width="150">
        <template #default="{ row }">{{ row.goods?.name || row.goodsId }}</template>
      </el-table-column>
      <el-table-column label="仓库名称" width="120">
        <template #default="{ row }">{{ row.warehouse?.name || row.warehouseId }}</template>
      </el-table-column>
      <el-table-column label="库存数量" width="100">
        <template #default="{ row }">
          <el-tag :type="isWarning(row) ? 'danger' : 'success'" effect="plain">
            {{ row.quantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警阈值" width="100">
        <template #default="{ row }">{{ row.goods?.threshold ?? '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="isWarning(row) ? 'danger' : 'success'" size="small">
            {{ isWarning(row) ? '预警' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInventory } from '../api'

const list = ref([])
const loading = ref(false)

function isWarning(row) {
  return row.goods && row.quantity <= row.goods.threshold
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getInventory()
    list.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
}

onMounted(fetchData)
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 style="text-align:center; margin-bottom:24px;">超市货物仓库管理系统</h2>
      <el-form @submit.prevent="handleLogin" label-width="80px">
        <el-form-item label="工号">
          <el-input v-model="staffId" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" style="width:100%" :loading="loading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const staffId = ref('')
const password = ref('')
const loading = ref(false)

async function handleLogin() {
  if (!staffId.value || !password.value) {
    ElMessage.warning('请输入工号和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(staffId.value, password.value)
    localStorage.setItem('staffId', res.data.staffId)
    localStorage.setItem('staffName', res.data.staffName)
    localStorage.setItem('staffRole', res.data.role)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch {
    ElMessage.error('工号或密码错误')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}
.login-card { width: 400px; }
</style>

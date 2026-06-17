import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

// 请求拦截器：自动添加 token
api.interceptors.request.use(config => {
  const staffId = localStorage.getItem('staffId')
  if (staffId) {
    config.headers.Authorization = `Bearer ${staffId}`
  }
  return config
})

// 响应拦截器：401 跳转登录
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('staffId')
      localStorage.removeItem('staffName')
      localStorage.removeItem('staffRole')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export function login(staffId, password) {
  return api.post('/auth/login', { staffId, password })
}

export function getGoods(params) { return api.get('/goods', { params }) }
export function createGoods(data) { return api.post('/goods', data) }
export function updateGoods(id, data) { return api.put(`/goods/${id}`, data) }
export function deleteGoods(id) { return api.delete(`/goods/${id}`) }

export function getWarehouses() { return api.get('/warehouses') }
export function createWarehouse(data) { return api.post('/warehouses', data) }
export function updateWarehouse(id, data) { return api.put(`/warehouses/${id}`, data) }
export function deleteWarehouse(id) { return api.delete(`/warehouses/${id}`) }

export function getInStock(params) { return api.get('/instock', { params }) }
export function createInStock(data) { return api.post('/instock', data) }

export function getOutStock(params) { return api.get('/outstock', { params }) }
export function createOutStock(data) { return api.post('/outstock', data) }

export function getInventory() { return api.get('/inventory') }

export function getSuppliers(params) { return api.get('/suppliers', { params }) }
export function createSupplier(data) { return api.post('/suppliers', data) }
export function deleteSupplier(id) { return api.delete(`/suppliers/${id}`) }

export default api

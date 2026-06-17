import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import HomeDashboard from '../views/HomeDashboard.vue'
import Goods from '../views/Goods.vue'
import Warehouse from '../views/Warehouse.vue'
import InStock from '../views/InStock.vue'
import OutStock from '../views/OutStock.vue'
import Inventory from '../views/Inventory.vue'
import Supplier from '../views/Supplier.vue'

const routes = [
  { path: '/login', component: Login },
  {
    path: '/',
    component: Dashboard,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: HomeDashboard, meta: { title: '首页' } },
      { path: 'goods', component: Goods, meta: { title: '商品管理' } },
      { path: 'warehouses', component: Warehouse, meta: { title: '仓库管理' } },
      { path: 'instock', component: InStock, meta: { title: '入库管理' } },
      { path: 'outstock', component: OutStock, meta: { title: '出库管理' } },
      { path: 'inventory', component: Inventory, meta: { title: '库存查询' } },
      { path: 'suppliers', component: Supplier, meta: { title: '供应商管理' } },
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const staffId = localStorage.getItem('staffId')
  if (to.path !== '/login' && !staffId) {
    next('/login')
  } else {
    next()
  }
})

export default router

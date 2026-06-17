---
name: supermarket-warehouse
description: >
  超市货物仓库管理系统 V1.0 的专业技能，涵盖商品库存管理、出入库流程、
  供应商管理、仓库盘点、报表统计等核心业务。适用于 SpringBoot + Vue + MySQL
  技术栈的数据库课程设计项目。包含需求分析指南、数据库设计规范、功能模块定义、
  前后端开发模式和课程报告撰写指导。
license: Apache-2.0
version: 1.0.0
metadata:
  author: DB Course Design Team
---

# 超市货物仓库管理系统 V1.0

## 角色与背景

你是一个拥有 10 年以上超市仓库管理经验的高级仓储主管，管理着 3-5 个仓库、500-2000 种商品、
20-50 家供应商。系统服务于超市实体门店的仓储管理场景。

## 何时使用

- 设计超市货物仓库管理系统的数据库结构
- 开发商品入库/出库/库存管理功能模块
- 设计供应商管理和采购流程
- 生成库存盘点报表和出入库统计
- 撰写数据库课程设计报告

## 核心技术原则

### 数据库设计规范（按课程设计要求）

必须严格遵循以下数据库设计步骤：

1. **需求分析** — 明确用户角色、业务规则、数据字典
2. **概念结构设计** — 绘制 E-R 图，标识实体、属性和关系
3. **逻辑结构设计** — 将 E-R 图转换为关系模式，进行规范化处理（至少达到 3NF）
4. **物理结构设计** — 确定存储结构、索引策略、存取方法

### 实体关系（E-R 模型）

核心实体（7 个）及关系：

- 商品（Goods）— 仓库（Warehouse）：M:N → 通过库存表（Inventory）关联
- 商品（Goods）— 供应商（Supplier）：M:N
- 入库单（InStock）关联商品、仓库、供应商、员工
- 出库单（OutStock）关联商品、仓库、员工

### 数据完整性约束

- CHECK 约束：数量 > 0，价格 > 0，库存 >= 0，角色枚举限制
- FOREIGN KEY 约束：入库/出库/库存表正确引用商品、仓库、供应商、员工表
- 主码唯一约束、复合主键约束

### 数据安全性

- 用户鉴别：账号 + 密码登录
- 权限分离：管理员/仓库管理员/采购员三种角色不同操作权限
- 操作追溯：记录操作员和操作时间

## 数据库模式设计

### 关系模式（7 个表）

```sql
-- 商品表
goods(g_id, g_name, g_spec, g_unit, g_price, g_threshold)

-- 仓库表
warehouse(w_id, w_name, w_address, w_capacity)

-- 供应商表
supplier(s_id, s_name, s_contact, s_phone, s_address)

-- 员工表
staff(staff_id, staff_name, staff_role, staff_pwd)

-- 入库表
instock(i_id, g_id, w_id, s_id, staff_id, i_quantity, i_date)

-- 出库表
outstock(o_id, g_id, w_id, staff_id, o_quantity, o_date)

-- 库存表
inventory(g_id, w_id, inv_quantity)
```

### 外模式（视图）

```sql
-- 库存视图：展示商品名称、仓库名称、库存数量
-- 入库统计视图：按日期统计入库数量
-- 出库统计视图：按日期统计出库数量
-- 低库存预警视图：库存低于阈值的商品
```

### 内模式（索引）

建议至少建立 7 个索引：
- 商品名称索引
- 仓库名称索引
- 入库单日期索引
- 出库单日期索引
- 入库单商品编号索引
- 出库单商品编号索引

## 功能模块设计

| 模块 | 功能点 | 操作角色 |
|------|--------|----------|
| 登录/权限模块 | 身份核验、角色路由、密码修改 | 全部 |
| 商品管理 | 商品增删改查、按名称/规格搜索 | 管理员、采购员 |
| 仓库管理 | 仓库信息维护、容量查看 | 管理员 |
| 入库管理 | 入库单登记、修改、查询、自动更新库存 | 采购员、仓库管理员 |
| 出库管理 | 出库单登记、修改、查询、库存校验、自动扣减 | 仓库管理员 |
| 库存查询 | 实时库存查看、低库存预警（阈值标红） | 全部 |
| 供应商管理 | 供应商增删改查 | 采购员 |
| 报表统计 | 出入库趋势统计、库存盘点报表 | 管理员、仓库管理员 |

## Spring Boot 后端模式

### 分层架构
Controller → Service → Repository → Entity

### REST API 设计
- GET /api/goods — 分页查询商品
- POST /api/goods — 新增商品
- PUT /api/goods/{id} — 修改商品
- DELETE /api/goods/{id} — 删除商品
- GET /api/inventory — 查询库存（含低库存预警）
- POST /api/instock — 入库登记（自动更新库存）
- POST /api/outstock — 出库登记（校验 + 自动扣减）

### 关键实现要点
- @Transactional 确保出入库与库存更新的原子性
- 出库时校验库存是否充足
- 按角色拦截请求（Spring Security 或拦截器）
- 全局异常处理（@ControllerAdvice）
- 分页查询（Pageable）

## Vue 前端模式

### 页面结构
- 登录页（Login.vue）
- 首页仪表盘（Dashboard.vue）
- 商品管理页（Goods.vue）
- 仓库管理页（Warehouse.vue）
- 入库管理页（InStock.vue）
- 出库管理页（OutStock.vue）
- 库存查询页（Inventory.vue）
- 供应商管理页（Supplier.vue）
- 报表统计页（Report.vue）

### 技术要点
- Vue Router 实现路由和权限守卫
- Axios 封装 HTTP 请求
- Element Plus 组件库
- 角色路由：不同角色登录后显示不同菜单

## 课程报告撰写指导

### 报告结构（8 章）
0. 小组分工与合作说明
1. 需求分析（背景、角色、业务规则、流程、数据字典）
2. 概念结构设计（E-R 模型）
3. 逻辑结构设计（关系模式转换、规范化）
4. 物理结构设计（存储安排、索引）
5. 功能模块概要设计
6. 参考文献
7. 总结与体会

### 评分要点
- 需求分析完整性：30 分
- 数据库设计规范性（E-R 模型、规范化理论、索引设计）：40 分
- 功能模块设计合理性：30 分
- 小组分工和沟通协作：50 分

## 常见问题与解决方案

1. **库存并发问题：** 使用数据库行级锁（SELECT ... FOR UPDATE）或乐观锁
2. **出入库操作原子性：** 使用 @Transactional 保证事务一致性
3. **删除约束冲突：** 级联删除（ON DELETE CASCADE）或软删除标记
4. **大量数据查询性能：** 合理建立索引 + 分页查询
5. **密码安全性：** Spring Security BCryptPasswordEncoder 加密存储

# 超市货物仓库管理系统 V1.0

## 项目概述

超市货物仓库管理系统 V1.0 是一个**数据库课程设计**项目，采用 **B/S 架构**，使用 **SpringBoot + Vue + MySQL** 技术栈开发，运行于浏览器，无需安装客户端。

## 技术栈

| 层次 | 技术 | 说明 |
|------|------|------|
| 前端 | Vue 3 + Element Plus + Axios + Vue Router | 浏览器运行 |
| 后端 | Spring Boot 3.2 + Spring Data JPA + Spring Security | REST API |
| 数据库 | MySQL 8.0 | 关系型数据库 |

## 项目结构

```
work/
├── skills/supermarket-warehouse/    # 适配 skill（基于 inventory-demand-planning）
│   └── SKILL.md
├── database/
│   └── init.sql                     # 数据库完整建库脚本
├── backend/                         # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/java/com/warehouse/
│       ├── WarehouseApplication.java
│       ├── entity/                  # JPA 实体
│       ├── repository/              # 数据访问层
│       ├── service/                 # 业务逻辑层
│       ├── controller/              # REST 控制器
│       ├── config/                  # 配置（CORS、鉴权、异常处理）
│       └── dto/                     # 数据传输对象
├── frontend/                        # Vue 前端
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── api/index.js             # Axios 封装
│       ├── router/index.js          # 路由配置
│       ├── App.vue                  # 根组件（含侧边栏布局）
│       └── views/                   # 页面组件
└── README.md
```

## 数据库设计

### 核心实体（7 个表）

| 表名 | 说明 | 主键 |
|------|------|------|
| goods | 商品信息 | g_id |
| warehouse | 仓库信息 | w_id |
| supplier | 供应商信息 | s_id |
| staff | 员工用户 | staff_id |
| instock | 入库记录 | i_id |
| outstock | 出库记录 | o_id |
| inventory | 库存（商品-仓库） | (g_id, w_id) |

### 约束
- CHECK：数量 > 0，价格 > 0，库存 >= 0，角色枚举
- FOREIGN KEY：入库/出库/库存正确引用关联表
- 复合主键：inventory 表

### 视图（2 个）
- v_inventory_overview：库存总览（含预警状态）
- v_instock_monthly：月度入库统计

### 索引（7 个）
- goods(g_name), warehouse(w_name)
- instock(g_id, i_date, s_id)
- outstock(g_id, o_date)

## 功能模块

| 模块 | 功能 | 操作角色 |
|------|------|----------|
| 登录 | 身份核验、角色路由 | 全部 |
| 商品管理 | 增删改查、按名称搜索 | 管理员（增删改），仓库管理员/员工（只读） |
| 仓库管理 | 维护仓库信息 | 管理员（增删改），仓库管理员（只读） |
| 入库管理 | 登记入库、自动更新库存 | 管理员/仓库管理员（增删），员工（只读） |
| 出库管理 | 登记出库、库存校验、扣减库存 | 管理员/仓库管理员（增删） |
| 库存查询 | 实时库存、低库存预警标红 | 全部 |
| 供应商管理 | 增删、按名称搜索 | 管理员（增删改），员工（只读） |

## 快速开始

### 1. 启动后端（内置 H2 内存数据库 + 前端页面）
```bash
cd backend
mvn spring-boot:run
```
或者直接运行 JAR：
```bash
java -jar backend/target/supermarket-warehouse-1.0.0.jar --server.port=9090
```
访问 http://localhost:9090 即可使用完整系统。

### 2. 前端开发模式（可选）
```bash
cd frontend
npm install
npm run dev
```
前端运行在 http://localhost:5173（开发时 API 代理到 localhost:8080）

> 生产环境使用 MySQL 时，先执行 `database/init.sql` 初始化数据库，再修改 `application.yml` 中的数据源配置。

### 测试账号

| 工号 | 姓名 | 角色 | 密码 |
|------|------|------|------|
| 001 | 系统管理员 | 管理员 | 111111 |
| 002 | 张仓管 | 仓库管理员 | 111111 |
| 003 | 赵采购 | 员工 | 111111 |

> 开发环境使用 H2 内存数据库（明文密码），生产部署请改用 BCrypt 加密。

## 课程报告章节

1. 需求分析
2. 概念结构设计（E-R 模型）
3. 逻辑结构设计（关系模式转换、规范化）
4. 物理结构设计（索引、存储）
5. 功能模块概要设计
6. 参考文献
7. 总结与体会

## 许可证

Apache-2.0

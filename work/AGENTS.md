# AGENTS.md — 超市货物仓库管理系统 V1.0

## 项目总览

- **项目名称：** 超市货物仓库管理系统 V1.0
- **类型：** 纯实体店业务场景（V1.0），教师推荐题目
- **技术架构：** B/S 结构 + PC 端 Web 开发（浏览器运行，无需安装）
- **技术栈：** 后端 SpringBoot，前端 Vue，数据库 MySQL
- **组队：** 3-5 人，分工合作，每位组员必须有明确的分工任务
- **时间周期：** 1 周（20 课时/周），地点：逸夫楼
- **设计方法：** 按规范化设计方法的前 4 个步骤迭代完成（需求分析 → 概念结构 → 逻辑结构 → 物理结构）
- **课程目标：**
  - **D1（分析能力）：** 能够规范、科学地完成数据库设计，从可持续发展角度分析解决方案，支撑毕业要求指标点 2.1
  - **D2（沟通能力）：** 通过设计报告、E-R 图、口头汇报等形式阐述设计思路，回应质疑，支撑毕业要求指标点 9.1
- **样例参考：** `request/qinstusys26-数据库数据.txt`（学生成绩管理系统完整 SQL，C/S / VB / SQL Server）
- **讲义参考：** `request/第1版-24计科数据库课设讲义.pdf`（完整设计流程）

## 教学进度安排（5 次课 / 20 学时）

| 课次 | 学时 | 任务性质 | 内容 |
|:---|:---|:---|:---|
| 第1次课 | 4 | 个人任务 | 学习案例《学生成绩管理系统》数据库设计 |
| 第2次课 | 2 | 个人任务 | 学习案例《学生成绩管理系统》功能模块实现 |
| 第3次课 | 4 | 小组任务 | 自选题目，进行 DBAS 项目规划及需求分析 |
| 第4次课 | 6 | 小组任务 | 进行自选 DBAS 项目数据库设计 |
| 第5次课 | 4 | 小组任务 | 撰写课程报告 |
| **合计** | **20** | | |

## 报告结构（按顺序撰写，共 8 个章节）

### 0. 小组分工与合作说明

列出每位组员的：
- 姓名、学号
- 具体任务分工（需求分析 / E-R 图绘制 / SQL 表设计 / 索引设计 / 前端页面 / 报告统稿等）
- 合作方式说明

### 1. 需求分析

#### 1.1 应用系统开发背景
描述传统人工仓库管理的痛点（数据记录杂乱、库存更新滞后、出入库追溯困难、供应商信息管理混乱、统计盘点效率低下），引出系统开发的必要性，需从**可持续发展角度**分析解决方案。

#### 1.2 用户角色

| 角色 | 说明 |
|------|------|
| 系统管理员 | 拥有全量数据增删改查权限，管理所有基础信息和业务单据 |
| 仓库管理员 | 负责入库/出库的增删改查操作，商品/仓库信息只读 |
| 员工 | 负责商品、供应商、入库信息的查询和筛选（只读） |

#### 1.3 业务规则
- 一个仓库可存放多种商品，一种商品可存放在多个仓库（M:N → 通过库存表关联）
- 一个供应商可供应多种商品，一种商品可有多个供应商
- 每次入库操作生成一条入库记录，关联商品、仓库、供应商、操作员
- 每次出库操作生成一条出库记录，关联商品、仓库、操作员
- 出库数量不能超过当前库存数量
- 系统记录操作时间和操作员，实现可追溯

#### 1.4 系统业务流程
1. **系统启用初期：** 管理员统一录入仓库信息、员工信息、商品基础信息
2. **日常入库：** 管理员或仓库管理员登记入库单，更新库存；员工可查询筛选
3. **日常出库：** 管理员或仓库管理员登记出库单，校验库存是否充足，扣减库存
4. **库存盘点：** 仓库管理员定期核对系统库存与实际库存
5. **数据查询：** 各角色按权限查询库存、出入库记录、供应商信息（支持关键词搜索筛选）

#### 1.5 系统用户需求

**信息要求：**
- 管理员：所有数据表信息 + 库存视图 + 出入库统计
- 仓库管理员：商品/仓库信息（只读），入库/出库/库存信息（增删改查）
- 员工：商品/供应商/入库/库存信息（只读+筛选）

**处理要求：**
- 管理员：对所有表拥有增删改查权限
- 仓库管理员：入库/出库有完整增删改查权限，商品/仓库/库存只有查询权限，供应商不可见
- 员工：所有可访问的表只有查询和筛选权限，无增删改

#### 1.6 数据字典
概述主要数据项及其约束（价格 > 0，数量为正整数，日期格式，角色枚举等）

### 2. 概念结构设计（E-R 模型）

#### 2.1 核心实体（7 个）

| 实体 | 英文名 | 主键 | 主要属性 |
|------|--------|------|----------|
| 商品 | Goods | g_id | 商品编号、名称、规格、单位、售价 |
| 仓库 | Warehouse | w_id | 仓库编号、名称、地址、容量 |
| 供应商 | Supplier | s_id | 供应商编号、名称、联系人、电话、地址 |
| 员工 | Staff | staff_id | 员工编号、姓名、角色、密码 |
| 入库单 | InStock | i_id | 入库编号、商品编号、仓库编号、供应商编号、数量、日期、操作员 |
| 出库单 | OutStock | o_id | 出库编号、商品编号、仓库编号、数量、日期、操作员、用途 |
| 库存 | Inventory | (g_id, w_id) | 商品编号、仓库编号、当前库存数量 |

#### 2.2 实体间联系
- Goods — InStock：1:N（一种商品有多条入库记录）
- Goods — OutStock：1:N（一种商品有多条出库记录）
- Warehouse — InStock：1:N
- Supplier — InStock：1:N
- Staff — InStock：1:N
- Staff — OutStock：1:N
- Goods — Inventory — Warehouse：M:N 转化为 Inventory 关联表

#### 2.3 E-R 图
绘制完整的 E-R 图，标注：
- 所有实体及属性（主码加下划线）
- 实体间联系类型（1:1 / 1:N / M:N）
- 联系的属性（如入库数量、入库日期等）

### 3. 逻辑结构设计（关系模式）

#### 3.1 基本表设计（7 张表）

参照样例 `request/qinstusys26-数据库数据.txt` 风格，使用 **MySQL 语法**：

```sql
create database warehouse_db;
use warehouse_db;

-- 1. 员工表（管理员 + 仓库管理员 + 员工）
create table staff (
  staff_id char(6) primary key,
  staff_name varchar(20),
  staff_role varchar(10) constraint ck_role check(staff_role in('管理员','仓库管理员','员工')),
  staff_pwd varchar(64)
);

-- 2. 商品表
create table goods (
  g_id char(9) primary key,
  g_name char(30),
  g_spec char(20),
  g_unit char(10),
  g_price decimal(10,2) constraint ck_price check(g_price > 0)
);

-- 3. 仓库表
create table warehouse (
  w_id char(9) primary key,
  w_name char(20),
  w_address char(50),
  w_capacity int constraint ck_capacity check(w_capacity > 0)
);

-- 4. 供应商表
create table supplier (
  s_id char(9) primary key,
  s_name char(30),
  s_contact char(20),
  s_phone char(20),
  s_address char(50)
);

-- 5. 入库单表
create table instock (
  i_id char(9) primary key,
  g_id char(9),
  w_id char(9),
  s_id char(9),
  staff_id char(9),
  i_quantity int constraint ck_i_qty check(i_quantity > 0),
  i_date datetime default now(),
  constraint fk_i_goods foreign key(g_id) references goods(g_id),
  constraint fk_i_ware foreign key(w_id) references warehouse(w_id),
  constraint fk_i_supp foreign key(s_id) references supplier(s_id),
  constraint fk_i_staff foreign key(staff_id) references staff(staff_id)
);

-- 6. 出库单表
create table outstock (
  o_id char(9) primary key,
  g_id char(9),
  w_id char(9),
  staff_id char(9),
  o_quantity int constraint ck_o_qty check(o_quantity > 0),
  o_date datetime default now(),
  o_purpose char(50),
  constraint fk_o_goods foreign key(g_id) references goods(g_id),
  constraint fk_o_ware foreign key(w_id) references warehouse(w_id),
  constraint fk_o_staff foreign key(staff_id) references staff(staff_id)
);

-- 7. 库存表（记录每种商品在每个仓库的实时库存）
create table inventory (
  g_id char(9),
  w_id char(9),
  inv_quantity int default 0 constraint ck_inv check(inv_quantity >= 0),
  primary key(g_id, w_id),
  constraint fk_inv_goods foreign key(g_id) references goods(g_id),
  constraint fk_inv_ware foreign key(w_id) references warehouse(w_id)
);
```

#### 3.2 视图（至少 2 个）

```sql
-- 视图1：库存明细（商品名 + 仓库名 + 数量）
create view v_inventory_detail as
select g.g_name as 商品名, w.w_name as 仓库名, inv.inv_quantity as 库存量
from inventory inv, goods g, warehouse w
where inv.g_id = g.g_id and inv.w_id = w.w_id;

-- 视图2：出入库统计（按商品汇总）
create view v_stock_summary as
select g.g_id as 商品编号, g.g_name as 商品名,
  ifnull((select sum(i_quantity) from instock where g_id = g.g_id), 0) as 总入库量,
  ifnull((select sum(o_quantity) from outstock where g_id = g.g_id), 0) as 总出库量
from goods g;
```

### 4. 物理结构设计（索引与存储）

#### 4.1 原则
- 主码由数据库自动生成唯一索引，不重复创建
- 优先为高频查询字段和外键添加普通索引
- 低区分度字段（如密码、性别）不建索引
- 兼顾查询效率与索引维护成本

#### 4.2 各表索引设计

| 表名 | 索引字段 | 索引类型 | 说明 |
|------|----------|----------|------|
| goods | g_name | 普通索引 | 按商品名查询频繁 |
| warehouse | w_name | 普通索引 | 按仓库名查询 |
| instock | g_id | 普通索引 | 外键，多表连接查询 |
| instock | i_date | 普通索引 | 按入库日期统计 |
| instock | s_id | 普通索引 | 外键，按供应商查询 |
| outstock | g_id | 普通索引 | 外键，多表连接查询 |
| outstock | o_date | 普通索引 | 按出库日期统计 |
| inventory | g_id, w_id | 主码唯一索引（自动） | 复合主键 |

#### 4.3 SQL 语句
```sql
create index idx_goods_name on goods(g_name);
create index idx_ware_name on warehouse(w_name);
create index idx_instock_gid on instock(g_id);
create index idx_instock_date on instock(i_date);
create index idx_instock_sid on instock(s_id);
create index idx_outstock_gid on outstock(g_id);
create index idx_outstock_date on outstock(o_date);
```

### 5. 功能模块概要设计

| 模块 | 功能点 | 操作角色 |
|------|--------|----------|
| 登录/权限模块 | 身份核验、角色路由 | 全部 |
| 商品管理 | 增删改查、按名称搜索 | 管理员（增删改），仓库管理员/员工（只读+搜索） |
| 仓库管理 | 仓库信息维护 | 管理员（增删改），仓库管理员（只读+搜索） |
| 入库管理 | 入库单登记、自动更新库存 | 管理员/仓库管理员（增删），员工（只读+搜索） |
| 出库管理 | 出库单登记、库存校验、扣减库存 | 管理员/仓库管理员（增删） |
| 库存查询 | 实时库存、低库存预警标红 | 全部（含搜索筛选） |
| 供应商管理 | 供应商增删、按名称/联系人搜索 | 管理员（增删改），员工（只读+搜索） |

### 6. 参考文献

- 王珊, 萨师煊. 《数据库系统概论》(第5版). 高等教育出版社
- MySQL 官方文档
- 课程讲义：《数据库系统原理课程设计指导讲义》

### 7. 总结与体会

- 每位组员总结个人收获
- 项目开发中遇到的问题及解决方案
- 对数据库设计过程的体会
- 改进方向与未来扩展设想（如 V2.0 线上网店对接）

---

## 成绩评定与考核要求

### 成绩构成

| 类别 | 项目 | 占比 | 支撑课程目标 |
|:---|:---|:---:|:---|
| 平时成绩（60%） | 个人检查 | 30% | D1 |
| | 小组检查-分析问题 | 15% | D1 |
| | 小组检查-沟通 | 15% | D2 |
| 期末成绩（40%） | 课程报告-分析问题 | 25% | D1 |
| | 课程报告-沟通 | 15% | D2 |
| **合计** | | **100%** | |

### 考核方式支撑占比

| 课程目标 | 支撑毕业要求 | 个人检查 | 小组检查1(分析) | 小组检查2(沟通) | 报告-分析 | 报告-团队合作 | 合计 |
|:---|:---|:---:|:---:|:---:|:---:|:---:|:---:|
| D1 | 指标点 2.1 | 30 | 15 | — | 25 | — | 70 |
| D2 | 指标点 9.1 | — | — | 15 | — | 15 | 30 |
| **合计** | | **30** | **15** | **15** | **25** | **15** | **100** |

### 个人检查评分标准（100 分，支撑 D1）

| 评分项目 | 分值 | 考核要点 |
|:---|:---:|:---|
| 对小组作品需求分析的理解与分析 | 30 分 | 项目背景、用户需求、功能/非功能需求、必要性、合理性、可行性、需求关联 |
| 对小组作品数据库设计的理解与分析 | 40 分 | E-R 模型（实体/属性/关系）、规范化理论、物理结构设计（存储/索引） |
| 对小组作品功能模块设计的理解与分析 | 30 分 | 模块划分依据、核心功能、接口定义、交互逻辑、优化空间 |

### 小组检查评分标准（100 分，分优/良/中/差四档）

| 评分维度 | 分值 | 支撑目标 | 评价要点 |
|:---|:---:|:---|:---|
| 小组作品质量 | 50 分 | D1 | 需求分析完整性、数据库设计规范性、功能模块合理性、文档规范性 |
| 小组沟通协作 | 50 分 | D2 | 分工合理性、团队精神、沟通效率、问题解决、进度把控 |

---

## 关键技术约束

### 数据完整性
- CHECK 约束：数量 > 0，价格 > 0，库存 >= 0，角色枚举限制
- FOREIGN KEY 约束：入库/出库/库存表正确引用商品、仓库、供应商、员工表
- 主码唯一约束

### 数据安全性
- 用户鉴别：账号 + 密码登录
- 权限分离：三种角色拥有不同操作权限
- 操作追溯：记录操作员和操作时间

### 数据库方言注意
- 使用 MySQL 语法：`NOW()` 替代 `getdate()`，`IFNULL()` 替代 `isnull()`

---

## 交付清单
1. 数据库设计报告（Word 文档，含以上全部 0-7 章内容）
2. E-R 图（可用 draw.io / Visio / PowerDesigner 绘制）
3. SQL 建库脚本（包含建表、视图、索引、测试数据插入）
4. 前端 Web 页面（B/S 架构，至少完成核心功能页面）
5. PPT 汇报材料

## 开发建议
1. **先 SQL 后页面：** 先在 MySQL 中完成全部建表、约束、视图、索引，插入测试数据并验证
2. **对照样例：** 每个设计步骤参照 `request/qinstusys26-数据库数据.txt` 的格式和完整性
3. **分工明确：** 需求分析 / E-R 图 / SQL / 前端 / 报告 各由专人负责，并行推进
4. **迭代完善：** 先完成核心表结构和基本功能，再逐步添加约束、索引、视图、报表
5. **边界控制：** 选择熟悉项目，做好需求及边界划分，合理规划功能复杂度

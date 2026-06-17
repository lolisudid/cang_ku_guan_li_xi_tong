-- ============================================================
-- 超市货物仓库管理系统 V1.0 — 数据库建库脚本
-- 技术栈：MySQL
-- 参照样例：request/qinstusys26-数据库数据.txt
-- ============================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS supermarket_warehouse
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE supermarket_warehouse;

-- ============================================================
-- 2. 建立 7 个关系模式（二维表）
-- 综合完整性要求：主键、外键、CHECK 约束、NOT NULL、UNIQUE
-- ============================================================

-- 2.1 商品表
CREATE TABLE goods (
  g_id        CHAR(6)     PRIMARY KEY,
  g_name      VARCHAR(50) NOT NULL,
  g_spec      VARCHAR(50)             COMMENT '规格，如 500ml/瓶',
  g_unit      VARCHAR(10) NOT NULL    COMMENT '单位，如 箱/袋/瓶',
  g_price     DECIMAL(10,2) NOT NULL  COMMENT '售价',
  g_threshold INT         NOT NULL DEFAULT 10 COMMENT '库存预警阈值',
  CONSTRAINT ck_goods_price CHECK (g_price > 0),
  CONSTRAINT ck_goods_threshold CHECK (g_threshold >= 0)
) COMMENT '商品信息表';

-- 2.2 仓库表
CREATE TABLE warehouse (
  w_id       CHAR(4)     PRIMARY KEY,
  w_name     VARCHAR(50) NOT NULL,
  w_address  VARCHAR(100)            COMMENT '仓库地址',
  w_capacity INT         NOT NULL    COMMENT '容量（单位：存储位）',
  CONSTRAINT ck_warehouse_capacity CHECK (w_capacity > 0)
) COMMENT '仓库信息表';

-- 2.3 供应商表
CREATE TABLE supplier (
  s_id      CHAR(6)     PRIMARY KEY,
  s_name    VARCHAR(50) NOT NULL,
  s_contact VARCHAR(20)             COMMENT '联系人',
  s_phone   VARCHAR(20)             COMMENT '联系电话',
  s_address VARCHAR(100)            COMMENT '地址'
) COMMENT '供应商信息表';

-- 2.4 员工表（含角色）
CREATE TABLE staff (
  staff_id   CHAR(6)     PRIMARY KEY,
  staff_name VARCHAR(20) NOT NULL,
  staff_role VARCHAR(10) NOT NULL COMMENT '角色：管理员/仓库管理员/员工',
  staff_pwd  VARCHAR(64) NOT NULL COMMENT '登录密码（BCrypt 加密）',
  CONSTRAINT ck_staff_role CHECK (staff_role IN ('管理员','仓库管理员','员工'))
) COMMENT '员工用户表';

-- 2.5 入库表
CREATE TABLE instock (
  i_id       CHAR(8)     PRIMARY KEY,
  g_id       CHAR(6)     NOT NULL,
  w_id       CHAR(4)     NOT NULL,
  s_id       CHAR(6)     NOT NULL,
  staff_id   CHAR(6)     NOT NULL,
  i_quantity INT         NOT NULL   COMMENT '入库数量',
  i_date     DATETIME    NOT NULL DEFAULT NOW() COMMENT '入库时间',
  CONSTRAINT ck_instock_quantity CHECK (i_quantity > 0),
  CONSTRAINT fk_instock_goods  FOREIGN KEY (g_id)     REFERENCES goods(g_id)
    ON UPDATE CASCADE,
  CONSTRAINT fk_instock_ware   FOREIGN KEY (w_id)     REFERENCES warehouse(w_id)
    ON UPDATE CASCADE,
  CONSTRAINT fk_instock_supp   FOREIGN KEY (s_id)     REFERENCES supplier(s_id)
    ON UPDATE CASCADE,
  CONSTRAINT fk_instock_staff  FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
    ON UPDATE CASCADE
) COMMENT '入库登记表';

-- 2.6 出库表
CREATE TABLE outstock (
  o_id       CHAR(8)     PRIMARY KEY,
  g_id       CHAR(6)     NOT NULL,
  w_id       CHAR(4)     NOT NULL,
  staff_id   CHAR(6)     NOT NULL,
  o_quantity INT         NOT NULL   COMMENT '出库数量',
  o_date     DATETIME    NOT NULL DEFAULT NOW() COMMENT '出库时间',
  CONSTRAINT ck_outstock_quantity CHECK (o_quantity > 0),
  CONSTRAINT fk_outstock_goods FOREIGN KEY (g_id) REFERENCES goods(g_id)
    ON UPDATE CASCADE,
  CONSTRAINT fk_outstock_ware  FOREIGN KEY (w_id) REFERENCES warehouse(w_id)
    ON UPDATE CASCADE,
  CONSTRAINT fk_outstock_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
    ON UPDATE CASCADE
) COMMENT '出库登记表';

-- 2.7 库存表（复合主键，关联商品与仓库）
CREATE TABLE inventory (
  g_id         CHAR(6)  NOT NULL,
  w_id         CHAR(4)  NOT NULL,
  inv_quantity INT      NOT NULL DEFAULT 0 COMMENT '当前库存数量',
  CONSTRAINT pk_inventory PRIMARY KEY (g_id, w_id),
  CONSTRAINT ck_inventory_quantity CHECK (inv_quantity >= 0),
  CONSTRAINT fk_inventory_goods FOREIGN KEY (g_id) REFERENCES goods(g_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_inventory_ware  FOREIGN KEY (w_id) REFERENCES warehouse(w_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '库存表（商品-仓库对应关系）';

-- ============================================================
-- 3. 建立 2 个外模式（视图）
-- ============================================================

-- 3.1 库存总览视图：展示商品名、仓库名、库存数量、预警状态
CREATE VIEW v_inventory_overview (g_name, w_name, inv_quantity, g_threshold, warning) AS
  SELECT
    g.g_name,
    w.w_name,
    i.inv_quantity,
    g.g_threshold,
    CASE
      WHEN i.inv_quantity <= g.g_threshold THEN '预警'
      ELSE '正常'
    END AS warning
  FROM inventory i
  JOIN goods g ON i.g_id = g.g_id
  JOIN warehouse w ON i.w_id = w.w_id;

-- 3.2 入库统计视图：按月统计每类商品的入库数量
CREATE VIEW v_instock_monthly (g_name, year_month, total_quantity) AS
  SELECT
    g.g_name,
    DATE_FORMAT(i.i_date, '%Y-%m') AS year_month,
    SUM(i.i_quantity) AS total_quantity
  FROM instock i
  JOIN goods g ON i.g_id = g.g_id
  GROUP BY g.g_name, DATE_FORMAT(i.i_date, '%Y-%m');

-- ============================================================
-- 4. 建立 7 个内模式（索引）
-- ============================================================

CREATE INDEX idx_goods_name      ON goods(g_name);
CREATE INDEX idx_ware_name       ON warehouse(w_name);
CREATE INDEX idx_instock_gid     ON instock(g_id);
CREATE INDEX idx_instock_date    ON instock(i_date);
CREATE INDEX idx_instock_sid     ON instock(s_id);
CREATE INDEX idx_outstock_gid    ON outstock(g_id);
CREATE INDEX idx_outstock_date   ON outstock(o_date);

-- ============================================================
-- 5. 插入测试数据
-- ============================================================

-- 5.1 仓库数据
INSERT INTO warehouse VALUES
('W001', '主仓库-1号库',   '超市后院左侧', 2000),
('W002', '主仓库-2号库',   '超市后院右侧', 1500),
('W003', '冷藏仓库',       '超市后院冷藏区', 800),
('W004', '日用百货仓库',   '超市二楼库房', 1200);

-- 5.2 供应商数据
INSERT INTO supplier VALUES
('S001', '旺旺食品有限公司',     '张经理', '13800138001', '广州市开发区路1号'),
('S002', '康师傅饮品有限公司',   '李经理', '13800138002', '天津市开发区路2号'),
('S003', '宝洁日化有限公司',     '王经理', '13800138003', '上海市开发区路3号'),
('S004', '伊利乳业有限公司',     '赵经理', '13800138004', '呼和浩特市开发区路4号'),
('S005', '双汇食品有限公司',     '刘经理', '13800138005', '漯河市开发区路5号'),
('S006', '农夫山泉有限公司',     '陈经理', '13800138006', '杭州市开发区路6号');

-- 5.3 员工数据（密码统一 111111，BCrypt 加密）
INSERT INTO staff VALUES
('001', '系统管理员', '管理员',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('002', '张仓管',     '仓库管理员', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('003', '赵采购',     '员工',       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- 5.4 商品数据
INSERT INTO goods VALUES
('G00001', '康师傅红烧牛肉面',   '袋装 100g',    '袋',  3.50,  50),
('G00002', '康师傅冰红茶',       '瓶装 500ml',   '瓶',  3.00,  100),
('G00003', '旺旺仙贝',           '袋装 52g',     '袋',  5.00,  30),
('G00004', '旺旺雪饼',           '袋装 84g',     '袋',  5.50,  30),
('G00005', '伊利纯牛奶',         '盒装 250ml',   '盒',  3.00,  200),
('G00006', '伊利安慕希酸奶',     '瓶装 205g',    '瓶',  6.50,  100),
('G00007', '双汇火腿肠',         '根装 35g',     '根',  1.50,  200),
('G00008', '双汇玉米热狗',       '根装 75g',     '根',  2.50,  100),
('G00009', '农夫山泉矿泉水',     '瓶装 550ml',   '瓶',  2.00,  200),
('G00010', '农夫山泉矿泉水',     '桶装 4L',      '桶',  8.00,  50),
('G00011', '海飞丝去屑洗发露',   '瓶装 400ml',   '瓶', 39.90,  20),
('G00012', '飘柔精华护理洗发露', '瓶装 500ml',   '瓶', 35.00,  20),
('G00013', '舒肤佳香皂',         '块装 115g',    '块',  5.50,  80),
('G00014', '奥利奥饼干',         '盒装 97g',     '盒',  9.90,  40),
('G00015', '乐事薯片',           '袋装 75g',     '袋',  7.00,  60);

-- 5.5 库存初始数据
INSERT INTO inventory VALUES
('G00001', 'W001', 500),
('G00002', 'W001', 300),
('G00003', 'W001', 200),
('G00004', 'W001', 150),
('G00005', 'W003', 600),
('G00006', 'W003', 400),
('G00007', 'W001', 800),
('G00008', 'W001', 300),
('G00009', 'W001', 500),
('G00010', 'W001', 100),
('G00011', 'W004', 80),
('G00012', 'W004', 60),
('G00013', 'W004', 200),
('G00014', 'W001', 120),
('G00015', 'W001', 180);

-- 5.6 入库样例数据
INSERT INTO instock VALUES
('I0000001', 'G00001', 'W001', 'S002', '003', 200, '2026-06-01 08:30:00'),
('I0000002', 'G00002', 'W001', 'S002', '003', 150, '2026-06-01 08:35:00'),
('I0000003', 'G00005', 'W003', 'S004', '003', 300, '2026-06-02 09:00:00'),
('I0000004', 'G00006', 'W003', 'S004', '003', 200, '2026-06-02 09:10:00'),
('I0000005', 'G00007', 'W001', 'S005', '003', 400, '2026-06-03 10:00:00'),
('I0000006', 'G00009', 'W001', 'S006', '003', 250, '2026-06-03 10:30:00'),
('I0000007', 'G00011', 'W004', 'S003', '003', 50,  '2026-06-04 11:00:00'),
('I0000008', 'G00013', 'W004', 'S003', '003', 100, '2026-06-04 11:20:00'),
('I0000009', 'G00003', 'W001', 'S001', '003', 100, '2026-06-05 14:00:00'),
('I0000010', 'G00014', 'W001', 'S001', '003', 80,  '2026-06-05 14:30:00');

-- 5.7 出库样例数据
INSERT INTO outstock VALUES
('O0000001', 'G00001', 'W001', '002', 80,  '2026-06-02 07:00:00'),
('O0000002', 'G00002', 'W001', '002', 50,  '2026-06-02 07:10:00'),
('O0000003', 'G00005', 'W003', '002', 100, '2026-06-03 07:00:00'),
('O0000004', 'G00007', 'W001', '002', 120, '2026-06-03 07:30:00'),
('O0000005', 'G00009', 'W001', '002', 80,  '2026-06-04 08:00:00'),
('O0000006', 'G00001', 'W001', '002', 60,  '2026-06-05 07:00:00'),
('O0000007', 'G00003', 'W001', '002', 30,  '2026-06-05 07:20:00'),
('O0000008', 'G00006', 'W003', '002', 50,  '2026-06-05 08:00:00'),
('O0000009', 'G00013', 'W004', '002', 40,  '2026-06-06 09:00:00'),
('O0000010', 'G00011', 'W004', '002', 20,  '2026-06-06 09:30:00');

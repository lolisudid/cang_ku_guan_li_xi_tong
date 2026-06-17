-- ============================================================
-- 超市货物仓库管理系统 V1.0 — H2 数据库建表脚本
-- 兼容模式：MODE=MySQL
-- ============================================================

-- 2.1 商品表
CREATE TABLE IF NOT EXISTS goods (
  g_id        VARCHAR(6)   PRIMARY KEY,
  g_name      VARCHAR(50)  NOT NULL,
  g_spec      VARCHAR(50),
  g_unit      VARCHAR(10)  NOT NULL,
  g_price     DECIMAL(10,2) NOT NULL,
  g_threshold INT          NOT NULL DEFAULT 10,
  CONSTRAINT ck_goods_price CHECK (g_price > 0),
  CONSTRAINT ck_goods_threshold CHECK (g_threshold >= 0)
);

-- 2.2 仓库表
CREATE TABLE IF NOT EXISTS warehouse (
  w_id       VARCHAR(4)  PRIMARY KEY,
  w_name     VARCHAR(50) NOT NULL,
  w_address  VARCHAR(100),
  w_capacity INT         NOT NULL,
  CONSTRAINT ck_warehouse_capacity CHECK (w_capacity > 0)
);

-- 2.3 供应商表
CREATE TABLE IF NOT EXISTS supplier (
  s_id      VARCHAR(6)  PRIMARY KEY,
  s_name    VARCHAR(50) NOT NULL,
  s_contact VARCHAR(20),
  s_phone   VARCHAR(20),
  s_address VARCHAR(100)
);

-- 2.4 员工表
CREATE TABLE IF NOT EXISTS staff (
  staff_id   VARCHAR(6)  PRIMARY KEY,
  staff_name VARCHAR(20) NOT NULL,
  staff_role VARCHAR(10) NOT NULL,
  staff_pwd  VARCHAR(64) NOT NULL,
  CONSTRAINT ck_staff_role CHECK (staff_role IN ('管理员','仓库管理员','员工'))
);

-- 2.5 入库表
CREATE TABLE IF NOT EXISTS instock (
  i_id       VARCHAR(8)  PRIMARY KEY,
  g_id       VARCHAR(6)  NOT NULL,
  w_id       VARCHAR(4)  NOT NULL,
  s_id       VARCHAR(6)  NOT NULL,
  staff_id   VARCHAR(6)  NOT NULL,
  i_quantity INT         NOT NULL,
  i_date     TIMESTAMP   NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_instock_quantity CHECK (i_quantity > 0),
  CONSTRAINT fk_instock_goods  FOREIGN KEY (g_id)     REFERENCES goods(g_id)     ON UPDATE CASCADE,
  CONSTRAINT fk_instock_ware   FOREIGN KEY (w_id)     REFERENCES warehouse(w_id) ON UPDATE CASCADE,
  CONSTRAINT fk_instock_supp   FOREIGN KEY (s_id)     REFERENCES supplier(s_id)  ON UPDATE CASCADE,
  CONSTRAINT fk_instock_staff  FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE
);

-- 2.6 出库表
CREATE TABLE IF NOT EXISTS outstock (
  o_id       VARCHAR(8)  PRIMARY KEY,
  g_id       VARCHAR(6)  NOT NULL,
  w_id       VARCHAR(4)  NOT NULL,
  staff_id   VARCHAR(6)  NOT NULL,
  o_quantity INT         NOT NULL,
  o_date     TIMESTAMP   NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_outstock_quantity CHECK (o_quantity > 0),
  CONSTRAINT fk_outstock_goods FOREIGN KEY (g_id) REFERENCES goods(g_id)     ON UPDATE CASCADE,
  CONSTRAINT fk_outstock_ware  FOREIGN KEY (w_id) REFERENCES warehouse(w_id) ON UPDATE CASCADE,
  CONSTRAINT fk_outstock_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE
);

-- 2.7 库存表（复合主键）
CREATE TABLE IF NOT EXISTS inventory (
  g_id         VARCHAR(6) NOT NULL,
  w_id         VARCHAR(4) NOT NULL,
  inv_quantity INT        NOT NULL DEFAULT 0,
  CONSTRAINT pk_inventory PRIMARY KEY (g_id, w_id),
  CONSTRAINT ck_inventory_quantity CHECK (inv_quantity >= 0),
  CONSTRAINT fk_inventory_goods FOREIGN KEY (g_id) REFERENCES goods(g_id)     ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_inventory_ware  FOREIGN KEY (w_id) REFERENCES warehouse(w_id) ON DELETE CASCADE ON UPDATE CASCADE
);

package com.warehouse.dao;

import com.warehouse.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InventoryDao {
  private final JdbcTemplate jdbc;
  private final GoodsDao goodsDao;
  private final WarehouseDao warehouseDao;

  public InventoryDao(JdbcTemplate jdbc, GoodsDao goodsDao, WarehouseDao warehouseDao) {
    this.jdbc = jdbc;
    this.goodsDao = goodsDao;
    this.warehouseDao = warehouseDao;
  }

  private final RowMapper<Inventory> mapper = (rs, rowNum) -> {
    Inventory i = new Inventory();
    i.setGoodsId(rs.getString("g_id"));
    i.setWarehouseId(rs.getString("w_id"));
    i.setQuantity(rs.getInt("inv_quantity"));
    return i;
  };

  public List<Inventory> findAll() {
    List<Inventory> list = jdbc.query("SELECT * FROM inventory ORDER BY g_id, w_id", mapper);
    enrich(list);
    return list;
  }

  public Inventory findByGoodsAndWarehouse(String goodsId, String warehouseId) {
    List<Inventory> list = jdbc.query("SELECT * FROM inventory WHERE g_id=? AND w_id=?", mapper, goodsId, warehouseId);
    if (list.isEmpty()) return null;
    enrich(list);
    return list.get(0);
  }

  public int upsert(Inventory i) {
    Inventory existing = findByGoodsAndWarehouse(i.getGoodsId(), i.getWarehouseId());
    if (existing != null) {
      return jdbc.update("UPDATE inventory SET inv_quantity=? WHERE g_id=? AND w_id=?",
          i.getQuantity(), i.getGoodsId(), i.getWarehouseId());
    }
    return jdbc.update("INSERT INTO inventory(g_id,w_id,inv_quantity) VALUES(?,?,?)",
        i.getGoodsId(), i.getWarehouseId(), i.getQuantity());
  }

  private void enrich(List<Inventory> list) {
    for (Inventory i : list) {
      i.setGoods(goodsDao.findById(i.getGoodsId()));
      i.setWarehouse(warehouseDao.findById(i.getWarehouseId()));
    }
  }
}

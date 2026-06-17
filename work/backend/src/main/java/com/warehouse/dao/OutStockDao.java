package com.warehouse.dao;

import com.warehouse.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OutStockDao {
  private final JdbcTemplate jdbc;
  private final GoodsDao goodsDao;
  private final WarehouseDao warehouseDao;

  public OutStockDao(JdbcTemplate jdbc, GoodsDao goodsDao, WarehouseDao warehouseDao) {
    this.jdbc = jdbc;
    this.goodsDao = goodsDao;
    this.warehouseDao = warehouseDao;
  }

  private final RowMapper<OutStock> mapper = (rs, rowNum) -> {
    OutStock s = new OutStock();
    s.setId(rs.getString("o_id"));
    s.setGoodsId(rs.getString("g_id"));
    s.setWarehouseId(rs.getString("w_id"));
    s.setStaffId(rs.getString("staff_id"));
    s.setQuantity(rs.getInt("o_quantity"));
    Timestamp ts = rs.getTimestamp("o_date");
    if (ts != null) s.setDate(ts.toLocalDateTime());
    return s;
  };

  public List<OutStock> findAll(int offset, int limit) {
    List<OutStock> list = jdbc.query("SELECT * FROM outstock ORDER BY o_date DESC LIMIT ? OFFSET ?", mapper, limit, offset);
    enrich(list);
    return list;
  }

  public int countAll() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM outstock", Integer.class);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM outstock WHERE o_id = ?", Integer.class, id) > 0;
  }

  public int insert(OutStock s) {
    return jdbc.update("INSERT INTO outstock(o_id,g_id,w_id,staff_id,o_quantity,o_date) VALUES(?,?,?,?,?,?)",
        s.getId(), s.getGoodsId(), s.getWarehouseId(), s.getStaffId(),
        s.getQuantity(), Timestamp.valueOf(s.getDate()));
  }

  private void enrich(List<OutStock> list) {
    for (OutStock s : list) {
      s.setGoods(goodsDao.findById(s.getGoodsId()));
      s.setWarehouse(warehouseDao.findById(s.getWarehouseId()));
    }
  }
}

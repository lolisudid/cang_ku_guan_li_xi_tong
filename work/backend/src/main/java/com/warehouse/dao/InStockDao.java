package com.warehouse.dao;

import com.warehouse.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class InStockDao {
  private final JdbcTemplate jdbc;
  private final GoodsDao goodsDao;
  private final WarehouseDao warehouseDao;

  public InStockDao(JdbcTemplate jdbc, GoodsDao goodsDao, WarehouseDao warehouseDao) {
    this.jdbc = jdbc;
    this.goodsDao = goodsDao;
    this.warehouseDao = warehouseDao;
  }

  private final RowMapper<InStock> mapper = (rs, rowNum) -> {
    InStock s = new InStock();
    s.setId(rs.getString("i_id"));
    s.setGoodsId(rs.getString("g_id"));
    s.setWarehouseId(rs.getString("w_id"));
    s.setSupplierId(rs.getString("s_id"));
    s.setStaffId(rs.getString("staff_id"));
    s.setQuantity(rs.getInt("i_quantity"));
    Timestamp ts = rs.getTimestamp("i_date");
    if (ts != null) s.setDate(ts.toLocalDateTime());
    return s;
  };

  public List<InStock> findAll(int offset, int limit) {
    List<InStock> list = jdbc.query("SELECT * FROM instock ORDER BY i_date DESC LIMIT ? OFFSET ?", mapper, limit, offset);
    enrich(list);
    return list;
  }

  public int countAll() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM instock", Integer.class);
  }

  public InStock findById(String id) {
    List<InStock> list = jdbc.query("SELECT * FROM instock WHERE i_id = ?", mapper, id);
    if (list.isEmpty()) return null;
    enrich(list);
    return list.get(0);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM instock WHERE i_id = ?", Integer.class, id) > 0;
  }

  public int insert(InStock s) {
    return jdbc.update("INSERT INTO instock(i_id,g_id,w_id,s_id,staff_id,i_quantity,i_date) VALUES(?,?,?,?,?,?,?)",
        s.getId(), s.getGoodsId(), s.getWarehouseId(), s.getSupplierId(),
        s.getStaffId(), s.getQuantity(), Timestamp.valueOf(s.getDate()));
  }

  private void enrich(List<InStock> list) {
    for (InStock s : list) {
      s.setGoods(goodsDao.findById(s.getGoodsId()));
      s.setWarehouse(warehouseDao.findById(s.getWarehouseId()));
    }
  }
}

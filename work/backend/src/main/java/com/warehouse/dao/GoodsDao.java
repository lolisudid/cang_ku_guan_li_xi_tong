package com.warehouse.dao;

import com.warehouse.entity.Goods;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class GoodsDao {
  private final JdbcTemplate jdbc;
  private final RowMapper<Goods> mapper = (rs, rowNum) -> {
    Goods g = new Goods();
    g.setId(rs.getString("g_id"));
    g.setName(rs.getString("g_name"));
    g.setSpec(rs.getString("g_spec"));
    g.setUnit(rs.getString("g_unit"));
    g.setPrice(rs.getBigDecimal("g_price"));
    g.setThreshold(rs.getInt("g_threshold"));
    return g;
  };

  public GoodsDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public List<Goods> findAll(int offset, int limit) {
    return jdbc.query("SELECT * FROM goods ORDER BY g_id LIMIT ? OFFSET ?", mapper, limit, offset);
  }

  public int countAll() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM goods", Integer.class);
  }

  public List<Goods> findByName(String name, int offset, int limit) {
    return jdbc.query("SELECT * FROM goods WHERE g_name LIKE ? ORDER BY g_id LIMIT ? OFFSET ?",
        mapper, "%" + name + "%", limit, offset);
  }

  public int countByName(String name) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM goods WHERE g_name LIKE ?", Integer.class, "%" + name + "%");
  }

  public Goods findById(String id) {
    List<Goods> list = jdbc.query("SELECT * FROM goods WHERE g_id = ?", mapper, id);
    return list.isEmpty() ? null : list.get(0);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM goods WHERE g_id = ?", Integer.class, id) > 0;
  }

  public int insert(Goods g) {
    return jdbc.update("INSERT INTO goods(g_id,g_name,g_spec,g_unit,g_price,g_threshold) VALUES(?,?,?,?,?,?)",
        g.getId(), g.getName(), g.getSpec(), g.getUnit(), g.getPrice(), g.getThreshold());
  }

  public int update(Goods g) {
    return jdbc.update("UPDATE goods SET g_name=?,g_spec=?,g_unit=?,g_price=?,g_threshold=? WHERE g_id=?",
        g.getName(), g.getSpec(), g.getUnit(), g.getPrice(), g.getThreshold(), g.getId());
  }

  public int deleteById(String id) {
    return jdbc.update("DELETE FROM goods WHERE g_id = ?", id);
  }
}

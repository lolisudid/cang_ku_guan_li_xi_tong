package com.warehouse.dao;

import com.warehouse.entity.Warehouse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class WarehouseDao {
  private final JdbcTemplate jdbc;
  private final RowMapper<Warehouse> mapper = (rs, rowNum) -> {
    Warehouse w = new Warehouse();
    w.setId(rs.getString("w_id"));
    w.setName(rs.getString("w_name"));
    w.setAddress(rs.getString("w_address"));
    w.setCapacity(rs.getInt("w_capacity"));
    return w;
  };

  public WarehouseDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public List<Warehouse> findAll() {
    return jdbc.query("SELECT * FROM warehouse ORDER BY w_id", mapper);
  }

  public Warehouse findById(String id) {
    List<Warehouse> list = jdbc.query("SELECT * FROM warehouse WHERE w_id = ?", mapper, id);
    return list.isEmpty() ? null : list.get(0);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM warehouse WHERE w_id = ?", Integer.class, id) > 0;
  }

  public int insert(Warehouse w) {
    return jdbc.update("INSERT INTO warehouse(w_id,w_name,w_address,w_capacity) VALUES(?,?,?,?)",
        w.getId(), w.getName(), w.getAddress(), w.getCapacity());
  }

  public int update(Warehouse w) {
    return jdbc.update("UPDATE warehouse SET w_name=?,w_address=?,w_capacity=? WHERE w_id=?",
        w.getName(), w.getAddress(), w.getCapacity(), w.getId());
  }

  public int deleteById(String id) {
    return jdbc.update("DELETE FROM warehouse WHERE w_id = ?", id);
  }
}

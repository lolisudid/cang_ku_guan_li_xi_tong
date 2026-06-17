package com.warehouse.dao;

import com.warehouse.entity.Supplier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SupplierDao {
  private final JdbcTemplate jdbc;
  private final RowMapper<Supplier> mapper = (rs, rowNum) -> {
    Supplier s = new Supplier();
    s.setId(rs.getString("s_id"));
    s.setName(rs.getString("s_name"));
    s.setContact(rs.getString("s_contact"));
    s.setPhone(rs.getString("s_phone"));
    s.setAddress(rs.getString("s_address"));
    return s;
  };

  public SupplierDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public List<Supplier> findAll() {
    return jdbc.query("SELECT * FROM supplier ORDER BY s_id", mapper);
  }

  public List<Supplier> findByName(String name) {
    return jdbc.query("SELECT * FROM supplier WHERE s_name LIKE ? ORDER BY s_id", mapper, "%" + name + "%");
  }

  public Supplier findById(String id) {
    List<Supplier> list = jdbc.query("SELECT * FROM supplier WHERE s_id = ?", mapper, id);
    return list.isEmpty() ? null : list.get(0);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM supplier WHERE s_id = ?", Integer.class, id) > 0;
  }

  public int insert(Supplier s) {
    return jdbc.update("INSERT INTO supplier(s_id,s_name,s_contact,s_phone,s_address) VALUES(?,?,?,?,?)",
        s.getId(), s.getName(), s.getContact(), s.getPhone(), s.getAddress());
  }

  public int deleteById(String id) {
    return jdbc.update("DELETE FROM supplier WHERE s_id = ?", id);
  }
}

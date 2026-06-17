package com.warehouse.dao;

import com.warehouse.entity.Staff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StaffDao {
  private final JdbcTemplate jdbc;
  private final RowMapper<Staff> mapper = (rs, rowNum) -> {
    Staff s = new Staff();
    s.setId(rs.getString("staff_id"));
    s.setName(rs.getString("staff_name"));
    s.setRole(rs.getString("staff_role"));
    s.setPassword(rs.getString("staff_pwd"));
    return s;
  };

  public StaffDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public Staff findByIdAndPassword(String id, String password) {
    List<Staff> list = jdbc.query("SELECT * FROM staff WHERE staff_id=? AND staff_pwd=?", mapper, id, password);
    return list.isEmpty() ? null : list.get(0);
  }

  public boolean existsById(String id) {
    return jdbc.queryForObject("SELECT COUNT(*) FROM staff WHERE staff_id = ?", Integer.class, id) > 0;
  }
}

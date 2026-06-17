package com.warehouse.service;

import com.warehouse.dao.StaffDao;
import com.warehouse.entity.Staff;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final StaffDao staffDao;

  public AuthService(StaffDao staffDao) { this.staffDao = staffDao; }

  public Staff login(String staffId, String password) {
    return staffDao.findByIdAndPassword(staffId, password);
  }
}

package com.warehouse.service;

import com.warehouse.dao.SupplierDao;
import com.warehouse.entity.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SupplierService {
  private final SupplierDao supplierDao;

  public SupplierService(SupplierDao supplierDao) { this.supplierDao = supplierDao; }

  public List<Supplier> listAll() { return supplierDao.findAll(); }

  public List<Supplier> searchByName(String name) { return supplierDao.findByName(name); }

  public Supplier getById(String id) {
    Supplier s = supplierDao.findById(id);
    if (s == null) throw new RuntimeException("供应商不存在: " + id);
    return s;
  }

  @Transactional
  public Supplier create(Supplier supplier) {
    if (supplierDao.existsById(supplier.getId())) {
      throw new RuntimeException("供应商编号已存在: " + supplier.getId());
    }
    supplierDao.insert(supplier);
    return supplier;
  }

  @Transactional
  public void delete(String id) {
    if (!supplierDao.existsById(id)) {
      throw new RuntimeException("供应商不存在: " + id);
    }
    supplierDao.deleteById(id);
  }
}

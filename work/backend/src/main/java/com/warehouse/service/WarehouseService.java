package com.warehouse.service;

import com.warehouse.dao.WarehouseDao;
import com.warehouse.entity.Warehouse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WarehouseService {
  private final WarehouseDao warehouseDao;

  public WarehouseService(WarehouseDao warehouseDao) { this.warehouseDao = warehouseDao; }

  public List<Warehouse> listAll() { return warehouseDao.findAll(); }

  public Warehouse getById(String id) {
    Warehouse w = warehouseDao.findById(id);
    if (w == null) throw new RuntimeException("仓库不存在: " + id);
    return w;
  }

  @Transactional
  public Warehouse create(Warehouse warehouse) {
    if (warehouseDao.existsById(warehouse.getId())) {
      throw new RuntimeException("仓库编号已存在: " + warehouse.getId());
    }
    warehouseDao.insert(warehouse);
    return warehouse;
  }

  @Transactional
  public Warehouse update(String id, Warehouse warehouse) {
    Warehouse existing = getById(id);
    existing.setName(warehouse.getName());
    existing.setAddress(warehouse.getAddress());
    existing.setCapacity(warehouse.getCapacity());
    warehouseDao.update(existing);
    return existing;
  }

  @Transactional
  public void delete(String id) {
    if (!warehouseDao.existsById(id)) {
      throw new RuntimeException("仓库不存在: " + id);
    }
    warehouseDao.deleteById(id);
  }
}

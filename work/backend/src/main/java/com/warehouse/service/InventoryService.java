package com.warehouse.service;

import com.warehouse.dao.InventoryDao;
import com.warehouse.entity.Inventory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {
  private final InventoryDao inventoryDao;

  public InventoryService(InventoryDao inventoryDao) { this.inventoryDao = inventoryDao; }

  public List<Inventory> listAll() { return inventoryDao.findAll(); }

  public Inventory getByGoodsAndWarehouse(String goodsId, String warehouseId) {
    Inventory inv = inventoryDao.findByGoodsAndWarehouse(goodsId, warehouseId);
    if (inv == null) throw new RuntimeException("库存记录不存在");
    return inv;
  }
}

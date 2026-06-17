package com.warehouse.service;

import com.warehouse.dao.InStockDao;
import com.warehouse.dao.InventoryDao;
import com.warehouse.entity.InStock;
import com.warehouse.entity.Inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InStockService {
  private final InStockDao inStockDao;
  private final InventoryDao inventoryDao;

  public InStockService(InStockDao inStockDao, InventoryDao inventoryDao) {
    this.inStockDao = inStockDao;
    this.inventoryDao = inventoryDao;
  }

  public List<InStock> list(int page, int size) {
    return inStockDao.findAll(page * size, size);
  }

  public int count() { return inStockDao.countAll(); }

  @Transactional
  public InStock create(InStock inStock) {
    if (inStockDao.existsById(inStock.getId())) {
      throw new RuntimeException("入库单编号已存在: " + inStock.getId());
    }
    inStockDao.insert(inStock);

    // 自动更新库存
    Inventory inv = inventoryDao.findByGoodsAndWarehouse(inStock.getGoodsId(), inStock.getWarehouseId());
    if (inv == null) {
      inv = new Inventory();
      inv.setGoodsId(inStock.getGoodsId());
      inv.setWarehouseId(inStock.getWarehouseId());
      inv.setQuantity(0);
    }
    inv.setQuantity(inv.getQuantity() + inStock.getQuantity());
    inventoryDao.upsert(inv);

    return inStock;
  }
}

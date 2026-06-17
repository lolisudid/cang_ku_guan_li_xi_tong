package com.warehouse.service;

import com.warehouse.dao.OutStockDao;
import com.warehouse.dao.InventoryDao;
import com.warehouse.entity.OutStock;
import com.warehouse.entity.Inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OutStockService {
  private final OutStockDao outStockDao;
  private final InventoryDao inventoryDao;

  public OutStockService(OutStockDao outStockDao, InventoryDao inventoryDao) {
    this.outStockDao = outStockDao;
    this.inventoryDao = inventoryDao;
  }

  public List<OutStock> list(int page, int size) {
    return outStockDao.findAll(page * size, size);
  }

  public int count() { return outStockDao.countAll(); }

  @Transactional
  public OutStock create(OutStock outStock) {
    if (outStockDao.existsById(outStock.getId())) {
      throw new RuntimeException("出库单编号已存在: " + outStock.getId());
    }

    Inventory inv = inventoryDao.findByGoodsAndWarehouse(outStock.getGoodsId(), outStock.getWarehouseId());
    if (inv == null) {
      throw new RuntimeException("该商品在此仓库无库存记录");
    }
    if (inv.getQuantity() < outStock.getQuantity()) {
      throw new RuntimeException("库存不足！当前库存: " + inv.getQuantity() + ", 需要出库: " + outStock.getQuantity());
    }

    outStockDao.insert(outStock);

    inv.setQuantity(inv.getQuantity() - outStock.getQuantity());
    inventoryDao.upsert(inv);

    return outStock;
  }
}

package com.warehouse.controller;

import com.warehouse.entity.Inventory;
import com.warehouse.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping
  public ResponseEntity<List<Inventory>> list() {
    return ResponseEntity.ok(inventoryService.listAll());
  }

  @GetMapping("/{goodsId}/{warehouseId}")
  public ResponseEntity<Inventory> get(@PathVariable String goodsId, @PathVariable String warehouseId) {
    return ResponseEntity.ok(inventoryService.getByGoodsAndWarehouse(goodsId, warehouseId));
  }
}

package com.warehouse.controller;

import com.warehouse.entity.Warehouse;
import com.warehouse.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
  private final WarehouseService warehouseService;

  public WarehouseController(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  @GetMapping
  public ResponseEntity<List<Warehouse>> list() {
    return ResponseEntity.ok(warehouseService.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Warehouse> getById(@PathVariable String id) {
    return ResponseEntity.ok(warehouseService.getById(id));
  }

  @PostMapping
  public ResponseEntity<Warehouse> create(@RequestBody Warehouse warehouse) {
    return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.create(warehouse));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Warehouse> update(@PathVariable String id, @RequestBody Warehouse warehouse) {
    return ResponseEntity.ok(warehouseService.update(id, warehouse));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    warehouseService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

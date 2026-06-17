package com.warehouse.controller;

import com.warehouse.entity.Supplier;
import com.warehouse.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final SupplierService supplierService;

  public SupplierController(SupplierService supplierService) {
    this.supplierService = supplierService;
  }

  @GetMapping
  public ResponseEntity<List<Supplier>> list(
      @RequestParam(required = false) String name) {
    if (name != null && !name.isBlank()) {
      return ResponseEntity.ok(supplierService.searchByName(name));
    }
    return ResponseEntity.ok(supplierService.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Supplier> getById(@PathVariable String id) {
    return ResponseEntity.ok(supplierService.getById(id));
  }

  @PostMapping
  public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {
    return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(supplier));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    supplierService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

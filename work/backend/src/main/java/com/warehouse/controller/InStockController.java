package com.warehouse.controller;

import com.warehouse.entity.InStock;
import com.warehouse.dto.InStockRequest;
import com.warehouse.service.InStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/instock")
public class InStockController {
  private final InStockService inStockService;

  public InStockController(InStockService inStockService) { this.inStockService = inStockService; }

  @GetMapping
  public ResponseEntity<Map<String, Object>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Map<String, Object> result = new HashMap<>();
    result.put("content", inStockService.list(page, size));
    result.put("totalElements", inStockService.count());
    result.put("number", page);
    result.put("size", size);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<InStock> create(@RequestBody InStockRequest req) {
    InStock s = new InStock();
    s.setId(req.id());
    s.setGoodsId(req.goodsId());
    s.setWarehouseId(req.warehouseId());
    s.setSupplierId(req.supplierId());
    s.setStaffId(req.staffId());
    s.setQuantity(req.quantity());
    s.setDate(LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(inStockService.create(s));
  }
}

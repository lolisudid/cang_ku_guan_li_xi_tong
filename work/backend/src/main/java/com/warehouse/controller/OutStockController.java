package com.warehouse.controller;

import com.warehouse.entity.OutStock;
import com.warehouse.dto.OutStockRequest;
import com.warehouse.service.OutStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/outstock")
public class OutStockController {
  private final OutStockService outStockService;

  public OutStockController(OutStockService outStockService) { this.outStockService = outStockService; }

  @GetMapping
  public ResponseEntity<Map<String, Object>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Map<String, Object> result = new HashMap<>();
    result.put("content", outStockService.list(page, size));
    result.put("totalElements", outStockService.count());
    result.put("number", page);
    result.put("size", size);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<OutStock> create(@RequestBody OutStockRequest req) {
    OutStock s = new OutStock();
    s.setId(req.id());
    s.setGoodsId(req.goodsId());
    s.setWarehouseId(req.warehouseId());
    s.setStaffId(req.staffId());
    s.setQuantity(req.quantity());
    s.setDate(LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(outStockService.create(s));
  }
}

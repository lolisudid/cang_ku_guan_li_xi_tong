package com.warehouse.controller;

import com.warehouse.dto.GoodsRequest;
import com.warehouse.entity.Goods;
import com.warehouse.service.GoodsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
  private final GoodsService goodsService;

  public GoodsController(GoodsService goodsService) { this.goodsService = goodsService; }

  @GetMapping
  public ResponseEntity<Map<String, Object>> list(
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    var list = goodsService.list(name, page, size);
    var total = goodsService.count(name);
    Map<String, Object> result = new HashMap<>();
    result.put("content", list);
    result.put("totalElements", total);
    result.put("number", page);
    result.put("size", size);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Goods> getById(@PathVariable String id) {
    return ResponseEntity.ok(goodsService.getById(id));
  }

  @PostMapping
  public ResponseEntity<Goods> create(@RequestBody GoodsRequest req) {
    Goods goods = new Goods();
    goods.setId(req.id());
    goods.setName(req.name());
    goods.setSpec(req.spec());
    goods.setUnit(req.unit());
    goods.setPrice(req.price());
    goods.setThreshold(req.threshold() != null ? req.threshold() : 10);
    return ResponseEntity.status(HttpStatus.CREATED).body(goodsService.create(goods));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Goods> update(@PathVariable String id, @RequestBody GoodsRequest req) {
    Goods goods = new Goods();
    goods.setName(req.name());
    goods.setSpec(req.spec());
    goods.setUnit(req.unit());
    goods.setPrice(req.price());
    goods.setThreshold(req.threshold() != null ? req.threshold() : 10);
    return ResponseEntity.ok(goodsService.update(id, goods));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    goodsService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

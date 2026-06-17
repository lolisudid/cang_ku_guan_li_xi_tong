package com.warehouse.entity;

import java.time.LocalDateTime;

public class OutStock {
  private String id;
  private String goodsId;
  private String warehouseId;
  private String staffId;
  private Integer quantity;
  private LocalDateTime date;
  private Goods goods;
  private Warehouse warehouse;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getGoodsId() { return goodsId; }
  public void setGoodsId(String goodsId) { this.goodsId = goodsId; }
  public String getWarehouseId() { return warehouseId; }
  public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
  public String getStaffId() { return staffId; }
  public void setStaffId(String staffId) { this.staffId = staffId; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
  public LocalDateTime getDate() { return date; }
  public void setDate(LocalDateTime date) { this.date = date; }
  public Goods getGoods() { return goods; }
  public void setGoods(Goods goods) { this.goods = goods; }
  public Warehouse getWarehouse() { return warehouse; }
  public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
}

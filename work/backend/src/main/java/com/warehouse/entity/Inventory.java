package com.warehouse.entity;

public class Inventory {
  private String goodsId;
  private String warehouseId;
  private Integer quantity;
  private Goods goods;
  private Warehouse warehouse;

  public String getGoodsId() { return goodsId; }
  public void setGoodsId(String goodsId) { this.goodsId = goodsId; }
  public String getWarehouseId() { return warehouseId; }
  public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
  public Goods getGoods() { return goods; }
  public void setGoods(Goods goods) { this.goods = goods; }
  public Warehouse getWarehouse() { return warehouse; }
  public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
}

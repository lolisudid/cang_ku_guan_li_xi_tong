package com.warehouse.entity;

import java.math.BigDecimal;

public class Goods {
  private String id;
  private String name;
  private String spec;
  private String unit;
  private BigDecimal price;
  private Integer threshold = 10;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getSpec() { return spec; }
  public void setSpec(String spec) { this.spec = spec; }
  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }
  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public Integer getThreshold() { return threshold; }
  public void setThreshold(Integer threshold) { this.threshold = threshold; }
}

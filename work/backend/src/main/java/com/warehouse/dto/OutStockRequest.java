package com.warehouse.dto;

public record OutStockRequest(
    String id,
    String goodsId,
    String warehouseId,
    String staffId,
    Integer quantity
) {}

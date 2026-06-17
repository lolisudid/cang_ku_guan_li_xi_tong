package com.warehouse.dto;

public record InStockRequest(
    String id,
    String goodsId,
    String warehouseId,
    String supplierId,
    String staffId,
    Integer quantity
) {}

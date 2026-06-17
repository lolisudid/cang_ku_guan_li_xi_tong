package com.warehouse.dto;

import java.math.BigDecimal;

public record GoodsRequest(
    String id,
    String name,
    String spec,
    String unit,
    BigDecimal price,
    Integer threshold
) {}

package com.cs.orderservice.external.product.dto;

import java.util.List;

public record ProductOrderResponse(List<ProductOrderItem> items) {}

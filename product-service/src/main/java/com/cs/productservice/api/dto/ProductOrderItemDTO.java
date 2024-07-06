package com.cs.productservice.api.dto;

import jakarta.validation.constraints.Min;

public record ProductOrderItemDTO(@Min(1) long productId, @Min(1) long quantity) {}

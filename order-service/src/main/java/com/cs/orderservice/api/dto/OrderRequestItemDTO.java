package com.cs.orderservice.api.dto;

import jakarta.validation.constraints.Min;

public record OrderRequestItemDTO(@Min(1) long productId, @Min(1) long amount) {

}

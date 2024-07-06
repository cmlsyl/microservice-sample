package com.cs.orderservice.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record OrderRequestDTO(@NotEmpty List<OrderRequestItemDTO> items) {}

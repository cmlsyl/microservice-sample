package com.cs.productservice.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record ProductOrderDTO(@NotEmpty List<ProductOrderItemDTO> items) {}

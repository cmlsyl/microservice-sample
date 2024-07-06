package com.cs.orderservice.api.dto;

import java.util.List;

public record OrderDTO(long id, int status, double totalPrice, List<OrderItemDTO> items) {}

package com.cs.orderservice.external.product.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.external.product.dto.ProductOrderResponse;

@FeignClient(name = "product-service")
public interface ProductService {

	@PostMapping("/orders/request")
    ResponseEntity<ProductOrderResponse> requestOrder(OrderRequestDTO orderRequestDTO);

	@PostMapping("/orders/cancel")
    ResponseEntity<String> cancelOrder(OrderRequestDTO orderRequestDTO);
}

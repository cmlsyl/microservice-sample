package com.cs.orderservice.external.product.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.external.product.dto.ProductOrderResponse;

@FeignClient(name = "product-service", path = "api/products/")
public interface ProductService {

	@PostMapping("order/request")
    ResponseEntity<ProductOrderResponse> requestOrder(@RequestBody OrderRequestDTO orderRequestDTO);

	@PostMapping("order/cancel")
    ResponseEntity<String> cancelOrder(@RequestBody OrderRequestDTO orderRequestDTO);
}

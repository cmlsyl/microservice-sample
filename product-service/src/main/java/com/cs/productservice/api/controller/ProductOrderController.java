package com.cs.productservice.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.productservice.api.dto.ProductOrderDTO;
import com.cs.productservice.api.dto.ProductOrderResponseDTO;
import com.cs.productservice.api.mapper.ProductMapper;
import com.cs.productservice.entity.Product;
import com.cs.productservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/products/order")
public class ProductOrderController {
	private final ProductService productService;
	private final ProductMapper productMapper;

	@PostMapping("request")
	public ResponseEntity<ProductOrderResponseDTO> requestOrder(@Valid @RequestBody ProductOrderDTO orderDTO) {
		try {
			log.info("Order request: {}", orderDTO);

			List<Product> products = productService.decrementStock(orderDTO.items());

			log.info("Order request is successfully processed: {}", orderDTO);

			return ResponseEntity.ok(new ProductOrderResponseDTO(productMapper.mapToProductDTOList(products)));
		} catch (Exception e) {
			log.error("Order request error", e);

			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("cancel")
	public ResponseEntity<String> cancelRequestedOrder(@Valid @RequestBody ProductOrderDTO orderDTO) {
		try {
			log.info("Order cancellation request: {}", orderDTO);

			productService.incrementStock(orderDTO.items());

			log.info("Order cancelled successfully: {}", orderDTO);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("Order cancel request error", e);

			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}

package com.cs.productservice.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.productservice.api.dto.ProductOrderDTO;
import com.cs.productservice.api.dto.ProductOrderResponseDTO;
import com.cs.productservice.api.dto.ProductOrderResponseItemDTO;
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

	@PostMapping("request")
	public ResponseEntity<ProductOrderResponseDTO> requestOrder(@Valid @RequestBody ProductOrderDTO orderDTO) {
		log.info("Order request: {}", orderDTO);

		List<Product> products = productService.decrementStock(orderDTO.items());

		log.info("Order request is successfully processed: {}", orderDTO);

		List<ProductOrderResponseItemDTO> responseDTOList = products.stream().map(product -> {
			long quantity = orderDTO.items().stream()
					.filter(item -> item.productId() == product.getId())
					.map(item -> item.quantity()).findFirst().get();
			return new ProductOrderResponseItemDTO(product.getId(), product.getPrice(), quantity);
		}).toList();

		return ResponseEntity.ok(new ProductOrderResponseDTO(responseDTOList));
	}

	@PostMapping("cancel")
	public ResponseEntity<String> cancelRequestedOrder(@Valid @RequestBody ProductOrderDTO orderDTO) {
		log.info("Order cancellation request: {}", orderDTO);

		productService.incrementStock(orderDTO.items());

		log.info("Order cancelled successfully: {}", orderDTO);

		return ResponseEntity.ok().build();
	}
}

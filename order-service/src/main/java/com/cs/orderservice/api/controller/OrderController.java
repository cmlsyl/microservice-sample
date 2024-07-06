package com.cs.orderservice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.orderservice.api.dto.OrderDTO;
import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.entity.Order;
import com.cs.orderservice.entity.OrderStatus;
import com.cs.orderservice.service.OrderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
		log.info("Order create request: {}", orderRequestDTO);

		OrderDTO orderDTO = orderService.create(orderRequestDTO);

		log.info("Order created, id: {}", orderDTO.id());

		return ResponseEntity.ok(orderDTO);
	}

	@PutMapping("/{id}/complete")
	public ResponseEntity<String> complete(@PathVariable("id") Long orderId) {
		log.info("Order complete request, id: {}", orderId);

		Order order = orderService.findById(orderId);

		if (order == null) {
			return ResponseEntity.internalServerError().body("Can not found order with given id: " + orderId);
		}

		order.setStatus(OrderStatus.COMPLETED);
		orderService.save(order);

		log.info("Order completed, id: {}", orderId);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<String> cancel(@PathVariable("id") Long orderId) {
		log.info("Order cancel request, id: {}", orderId);

		Order order = orderService.findById(orderId);

		if (order == null) {
			return ResponseEntity.internalServerError().body("Can not found order with given id: " + orderId);
		}

		orderService.cancel(order);

		log.info("Order cancelled, id: {}", orderId);

		return ResponseEntity.ok().build();
	}
}

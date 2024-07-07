package com.cs.orderservice.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.orderservice.api.dto.OrderDTO;
import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.api.mapper.OrderMapper;
import com.cs.orderservice.entity.Order;
import com.cs.orderservice.entity.OrderStatus;
import com.cs.orderservice.service.OrderService;
import com.cs.orderservice.util.exception.OrderServiceException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {
	private final OrderService orderService;
	private final OrderMapper orderMapper;

	@GetMapping
	public ResponseEntity<Page<OrderDTO>> get(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		log.info("Order list request, page: {}, size: {}", page, size);

		// TODO this block needs to get current user from authentication
		// TODO generate required specification and send to findAll

		Page<Order> data = orderService.findAll(PageRequest.of(page.orElse(0), size.orElse(10)));

		log.info("Order list request success, page: {}, size: {}, total elements: {}, ", page, size, data.getTotalElements());

		return ResponseEntity.ok(data.map(o -> orderMapper.mapToOrderDTO(o)));
	}

	@PostMapping
	public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
		log.info("Order create request: {}", orderRequestDTO);

		// TODO this block needs to get current user id from authentication and set to order

		Order order = orderService.create(orderRequestDTO);

		log.info("Order created, id: {}", order.getId());

		return ResponseEntity.ok(orderMapper.mapToOrderDTO(order));
	}

	@PutMapping("/{id}/complete")
	public ResponseEntity<String> complete(@PathVariable("id") long orderId) {
		log.info("Order complete request, id: {}", orderId);

		Order order = orderService.findById(orderId);

		if (order == null) {
			throw new OrderServiceException("Can not found order with given id: " + orderId);
		}

		order.setStatus(OrderStatus.COMPLETED);
		orderService.save(order);

		log.info("Order completed, id: {}", orderId);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<String> cancel(@PathVariable("id") long orderId) {
		log.info("Order cancel request, id: {}", orderId);

		Order order = orderService.findById(orderId);

		if (order == null) {
			throw new OrderServiceException("Can not found order with given id: " + orderId);
		}

		orderService.cancel(order);

		log.info("Order cancelled, id: {}", orderId);

		return ResponseEntity.ok().build();
	}
}

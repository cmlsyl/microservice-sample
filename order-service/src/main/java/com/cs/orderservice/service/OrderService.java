package com.cs.orderservice.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs.orderservice.api.dto.OrderDTO;
import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.api.dto.OrderRequestItemDTO;
import com.cs.orderservice.api.mapper.OrderItemMapper;
import com.cs.orderservice.entity.Order;
import com.cs.orderservice.entity.OrderItem;
import com.cs.orderservice.entity.OrderStatus;
import com.cs.orderservice.external.product.dto.ProductOrderResponse;
import com.cs.orderservice.external.product.service.ProductService;
import com.cs.orderservice.repository.OrderItemRepository;
import com.cs.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductService productService;
	private final OrderItemMapper orderItemMapper;

	public Order save(Order entity) {
		return orderRepository.save(entity);
	}

	public Order findById(long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Transactional
	public OrderDTO create(OrderRequestDTO orderRequestDTO) {
		ResponseEntity<ProductOrderResponse> response = productService.requestOrder(orderRequestDTO);

		if (response.getStatusCode() != HttpStatus.OK) {
			
		}

		ProductOrderResponse productOrderResponse = response.getBody();

		Order order = new Order();
		order.setStatus(OrderStatus.WAITING);
		order.setTotalPrice(productOrderResponse.items().stream().mapToDouble(item -> item.quantity() * item.price()).sum());

		save(order);

		List<OrderItem> orderItems = productOrderResponse.items().stream().map(item -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProductId(item.id());
			orderItem.setQuantity(item.quantity());
			orderItem.setPrice(item.price());
			return orderItem;
		}).toList();

		orderItemRepository.saveAll(orderItems);

		return new OrderDTO(order.getId(), order.getStatus().ordinal(), order.getTotalPrice(), orderItemMapper.mapToOrderItemDTOList(orderItems));
	}

	public void cancel(Order order) {
		List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

		OrderRequestDTO payload = new OrderRequestDTO(orderItems.stream().map(item -> new OrderRequestItemDTO(item.getProductId(), item.getQuantity())).toList());

		ResponseEntity<String> response = productService.cancelOrder(payload);

		if (response.getStatusCode() != HttpStatus.OK) {
			
		}

		order.setStatus(OrderStatus.CANCELLED);

		save(order);
	}
}

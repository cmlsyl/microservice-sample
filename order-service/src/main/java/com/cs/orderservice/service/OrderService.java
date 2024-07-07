package com.cs.orderservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs.orderservice.api.dto.OrderRequestDTO;
import com.cs.orderservice.api.dto.OrderRequestItemDTO;
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

	public Order save(Order entity) {
		return orderRepository.save(entity);
	}

	public Page<Order> findAll(PageRequest pageRequest) {
		return orderRepository.findAll(pageRequest);
	}

	public Order findById(long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Transactional
	public Order create(OrderRequestDTO orderRequestDTO) {
		ResponseEntity<ProductOrderResponse> response = productService.requestOrder(orderRequestDTO);

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

		order.setItems(orderItems);

		return order;
	}

	public void cancel(Order order) {
		List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

		OrderRequestDTO payload = new OrderRequestDTO(orderItems.stream().map(item -> new OrderRequestItemDTO(item.getProductId(), item.getQuantity())).toList());

		productService.cancelOrder(payload);

		order.setStatus(OrderStatus.CANCELLED);
		save(order);
	}
}

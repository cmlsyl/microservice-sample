package com.cs.orderservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.orderservice.entity.Order;
import com.cs.orderservice.entity.OrderItem;
import com.cs.orderservice.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	public List<OrderItem> findByOrder(Order order) {
		return orderItemRepository.findByOrder(order);
	}
}

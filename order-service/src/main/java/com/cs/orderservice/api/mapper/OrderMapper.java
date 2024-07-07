package com.cs.orderservice.api.mapper;

import java.util.List;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.orderservice.api.dto.OrderDTO;
import com.cs.orderservice.entity.Order;
import com.cs.orderservice.entity.OrderItem;
import com.cs.orderservice.service.OrderItemService;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public abstract class OrderMapper {
	@Autowired
	private OrderItemService orderItemService;

	@Mapping(target = "status", expression = "java(source.getStatus().ordinal())")
	public abstract OrderDTO mapToOrderDTO(Order source);

	@BeforeMapping
	public void setItems(Order source, @TargetType Class<OrderDTO> targetType) {
		if (source.getItems() == null) {
			List<OrderItem> items = orderItemService.findByOrder(source);
			source.setItems(items);
		}
	}
}

package com.cs.orderservice.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.cs.orderservice.api.dto.OrderItemDTO;
import com.cs.orderservice.api.dto.OrderRequestItemDTO;
import com.cs.orderservice.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
	public OrderItem mapToOrderItem(OrderRequestItemDTO source);
	public List<OrderItem> mapToOrderItemList(List<OrderRequestItemDTO> source);

	public OrderItemDTO mapToOrderItemDTO(OrderItem source);
	public List<OrderItemDTO> mapToOrderItemDTOList(List<OrderItem> source);
}

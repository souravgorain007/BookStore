package com.nt.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nt.dto.OrderRequest;
import com.nt.entity.Order;
import com.nt.entity.OrderLineItems;
import com.nt.repository.IOrderRepository;
import com.nt.service.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService{
	
	private final IOrderRepository orderRepository;

	@Override
	public String placeOrder(OrderRequest orderRequest) {
		Order order = Order.builder()
				           .orderNumber(UUID.randomUUID().toString())
				           .orders(orderRequest.getOrderLineItemsDTOs().stream()
				        		   .map(orderItem -> OrderLineItems.builder()
				        				                  .skuCode(orderItem.getSkuCode())
				        				                  .price(orderItem.getPrice())
				        				                  .quantity(orderItem.getQuantity())
				        				                  .build())
				        		   .collect(Collectors.toList()))
				        		   .build();
		
		orderRepository.save(order);
		log.info("Order placed successfully with order number {}",order.getOrderNumber());
				        		    
		return "Order placed successfully.";
	}

}

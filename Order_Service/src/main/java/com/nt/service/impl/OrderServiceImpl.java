package com.nt.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nt.dto.InventoryResponse;
import com.nt.dto.OrderLineItemsDTO;
import com.nt.dto.OrderRequest;
import com.nt.entity.Order;
import com.nt.entity.OrderLineItems;
import com.nt.event.OrderPlacedEvent;
import com.nt.repository.IOrderRepository;
import com.nt.service.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService{
	
	private final IOrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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
				        		   .toList())
				        		   .build();
		
		List<String> strings = orderRequest.getOrderLineItemsDTOs().stream()
				                           .map(OrderLineItemsDTO :: getSkuCode)
				                           .toList();
		
		List<InventoryResponse> response = webClientBuilder.build().get()
				                             .uri("http://INVENTORY-SERVICE/inventory/stocks", 
				                            uriBuilder -> uriBuilder.queryParam("skuCode", strings).build())
				                             .retrieve()
				                             .bodyToMono(new ParameterizedTypeReference<List<InventoryResponse>>() {
											}).block();
		
		boolean inStock =  response.stream().allMatch(res -> res.getQuantity() > 0);
		
		if(inStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
			log.info("Order placed successfully with order number {}",order.getOrderNumber());
					        		    
			return "Order placed successfully.";
		}else {
			log.error("Product not in stock. please try later.");
			throw new IllegalArgumentException("Product not in stock. please try later.");
		}
		
	}

}

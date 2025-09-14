package com.nt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.dto.OrderLineItemsDTO;
import com.nt.dto.OrderRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");
	@Autowired
	private ObjectMapper mapper;
	
	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
		dynamicPropertyRegistry.add("spring.datasource.username", () -> mysql.getUsername());
		dynamicPropertyRegistry.add("spring.datasource.password", () -> mysql.getPassword());
		
	}
	
	@Test
	@Order(1)
	void placeOrder() throws Exception{
		OrderRequest orderRequest = new OrderRequest();
		List<OrderLineItemsDTO> items = getOrderLineItems();
		orderRequest.setOrderLineItemsDTOs(items);
		
		mockMvc.perform(post("/order/place-order")
				        .contentType(MediaType.APPLICATION_JSON)
				        .content(mapper.writeValueAsString(orderRequest)))
		                .andExpect(status().isCreated());
		
	}
	
	private List<OrderLineItemsDTO> getOrderLineItems(){
		
		                  return List.of(OrderLineItemsDTO.builder()
				                                           .skuCode("iPhone 12")
				                                           .quantity(1)
				                                           .price(52000.0).build(),
				                          OrderLineItemsDTO.builder()
				                                           .skuCode("macBook M2")
				                                           .quantity(1)
				                                           .price(71000.0).build());
		
	}

}

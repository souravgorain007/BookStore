package com.nt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.nt.dto.InventoryRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class InventoryServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");
	
	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
		dynamicPropertyRegistry.add("spring.datasource.username", () -> mysql.getUsername());
		dynamicPropertyRegistry.add("spring.datasource.password", () -> mysql.getPassword());
		
	}
	
	
	@Test
	@Order(1)
	void addStock() throws Exception{
		InventoryRequest request = getInventoryRequest();
		mockMvc.perform(post("/inventory/add-product")
				        .contentType(MediaType.APPLICATION_JSON)
				        .content(mapper.writeValueAsString(request)))
		                .andExpect(status().isCreated());
	}
	
	private InventoryRequest getInventoryRequest() {
		return InventoryRequest.builder()
				               .skuCode("iPhone 12")
				               .quantity(1)
				               .build();
	}
	
	@Test
	@Order(2)
	void shoudReturnFalseItemOutOfStock() throws Exception{
		mockMvc.perform(get("/inventory/stock/{skuCode}","IPhone_12")
				        .contentType(MediaType.APPLICATION_JSON))
		                .andExpect(status().isOk())
		                .andExpect(content().string("false"));
	}
	
	@Test
	@Order(3)
	void shoudReturnTrueItemInStock() throws Exception{
		mockMvc.perform(get("/inventory/stock/{skuCode}","iPhone 12")
				        .contentType(MediaType.APPLICATION_JSON))
		                .andExpect(status().isOk())
		                .andExpect(content().string("true"));
	}

}

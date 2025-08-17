package com.nt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
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
import com.nt.dto.ProductRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductCatalogueApplicationTests {
	
	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
		dynamicPropertyRegistry.add("spring.datasource.username", () -> mysql.getUsername());
		dynamicPropertyRegistry.add("spring.datasource.password", () -> mysql.getPassword());
		
	}


	@Test
	void shouldCreateProduct() throws Exception{
		ProductRequest productRequest = getProductRequest();
		
		mockMvc.perform(post("/product/create")
		       .contentType(MediaType.APPLICATION_JSON)
		       .content(objectMapper.writeValueAsString(productRequest)))
		       .andExpect(status().isCreated());
		
	}
	
	public ProductRequest getProductRequest() {
		return ProductRequest.builder()
				             .name("iPhone 12")
				             .description("Apple product.")
				             .price(52000.0)
				             .build();
	}

}

package com.nt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.dto.ProductRequest;
import com.nt.dto.ProductResponse;
import com.nt.entity.Product;
import com.nt.repository.IProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductCatalogueApplicationTests {
	
	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IProductRepository productRepository;
	
	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
		dynamicPropertyRegistry.add("spring.datasource.username", () -> mysql.getUsername());
		dynamicPropertyRegistry.add("spring.datasource.password", () -> mysql.getPassword());
		
	}


	@Test
	@Order(1)
	void shouldCreateProduct() throws Exception{
		ProductRequest productRequest = getProductRequest();
		
		mockMvc.perform(post("/product/create")
		       .contentType(MediaType.APPLICATION_JSON)
		       .content(objectMapper.writeValueAsString(productRequest)))
		       .andExpect(status().isCreated());
		
		List<Product> response = productRepository.findAll();
		assertThat(response).hasSize(1);
		
	}
	
	public ProductRequest getProductRequest() {
		return ProductRequest.builder()
				             .name("iPhone 12")
				             .description("Apple product.")
				             .price(52000.0)
				             .build();
	}
	
	@Test
	@Order(2)
    void testFindAllProduct() throws Exception{
	String jsonResponse = 	mockMvc.perform(get("/product/findAll")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
	
	List<ProductResponse> responseList = 
			objectMapper.readValue(jsonResponse, new TypeReference<List<ProductResponse>>() {});
	
	assertThat(responseList).hasSize(1);
	assertThat(responseList).extracting(ProductResponse :: getName)
	   .containsExactlyInAnyOrder("iPhone 12");
	
	
	}

}

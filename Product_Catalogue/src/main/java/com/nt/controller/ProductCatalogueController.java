package com.nt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.ProductRequest;
import com.nt.dto.ProductResponse;
import com.nt.service.IProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductCatalogueController {
	
	private final IProductService productService;
	
	@PostMapping(value = "/create")
	public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
		String res = productService.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	@GetMapping(value = "/findAll")
	public ResponseEntity<List<ProductResponse>> findAllProduct(){
		List<ProductResponse> response = productService.findAllProduct();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	

}

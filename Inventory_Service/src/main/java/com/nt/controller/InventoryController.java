package com.nt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.InventoryRequest;
import com.nt.dto.InventoryResponse;
import com.nt.service.IInventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final IInventoryService inventoryService;
	
	@GetMapping(value = "/stock/{skuCode}")
	public ResponseEntity<Boolean> isInStock(@PathVariable String skuCode){
		Boolean response = inventoryService.inStock(skuCode);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/add-product")
	public ResponseEntity<String> addProduct(@RequestBody InventoryRequest inventoryRequest){
		String response = inventoryService.addProduct(inventoryRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(value = "/stocks")
	public ResponseEntity<List<InventoryResponse>> stocks(@RequestParam("skuCode") List<String> skuCodes){
		List<InventoryResponse> response = inventoryService.inStocks(skuCodes);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}

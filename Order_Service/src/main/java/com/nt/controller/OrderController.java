package com.nt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.OrderRequest;
import com.nt.service.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final IOrderService orderService;
	
	@PostMapping(value = "/place-order")
	public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
		 String response= orderService.placeOrder(orderRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}

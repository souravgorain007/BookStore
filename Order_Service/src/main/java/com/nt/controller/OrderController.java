package com.nt.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.OrderRequest;
import com.nt.service.IOrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	private final IOrderService orderService;
	
	@PostMapping(value = "/place-order")
	@CircuitBreaker(name = "inventory" , fallbackMethod = "inventoryFallBackMethod")
//	@TimeLimiter(name = "inventory")
	@Retry(name = "inventory")
	public CompletableFuture<ResponseEntity<String>> placeOrder(@RequestBody OrderRequest orderRequest){
		 
	return CompletableFuture.supplyAsync(() -> {
				String result	= orderService.placeOrder(orderRequest);
					return ResponseEntity.status(HttpStatus.CREATED).body(result);
				 });
				 
	}
	
	public CompletableFuture<ResponseEntity<String>> inventoryFallBackMethod(OrderRequest orderRequest, Throwable throwable) {
		log.info("Issue with Order or Inventory service {}", throwable.getMessage());
		return CompletableFuture.supplyAsync(() -> 
		ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oops!, something went wrong, please try later."));
	}

}

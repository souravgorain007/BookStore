package com.nt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nt.dto.InventoryRequest;
import com.nt.dto.InventoryResponse;
import com.nt.entity.Inventory;
import com.nt.repository.IInventoryRepository;
import com.nt.service.IInventoryService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements IInventoryService {

	private final IInventoryRepository inventoryRepository;

	@Override
	public boolean inStock(String skuCode) {
		return inventoryRepository.findBySkuCode(skuCode).isPresent();
	}

	@Override
	public String addProduct(InventoryRequest inventoryRequest) {
		Inventory inventory = Inventory.builder().skuCode(inventoryRequest.getSkuCode())
				.quantity(inventoryRequest.getQuantity()).build();
		inventoryRepository.save(inventory);
		log.info("Products are added to inventory with skuCode {}", inventoryRequest.getSkuCode());
		return "Products are added to inventory";
	}

	@Override
//	@SneakyThrows
	public List<InventoryResponse> inStocks(List<String> skuCodes) {
//		log.info("Wait started");
//		Thread.sleep(10000);
//		log.info("Wait ended");
		List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
		
		return inventories.stream()
				          .map( inventory -> 
				                        InventoryResponse.builder()
				                          .skuCode(inventory.getSkuCode())
				                          .quantity(inventory.getQuantity())
				                          .build()).toList();
	}

}

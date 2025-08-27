package com.nt.service;

import com.nt.dto.InventoryRequest;

public interface IInventoryService {
	
	boolean inStock(String skuCode);
	String addProduct(InventoryRequest inventoryRequest);

}

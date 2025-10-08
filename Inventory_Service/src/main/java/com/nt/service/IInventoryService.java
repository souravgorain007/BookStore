package com.nt.service;

import java.util.List;

import com.nt.dto.InventoryRequest;
import com.nt.dto.InventoryResponse;

public interface IInventoryService {

	boolean inStock(String skuCode);

	String addProduct(InventoryRequest inventoryRequest);
	
	List<InventoryResponse> inStocks(List<String> skuCodes);

}

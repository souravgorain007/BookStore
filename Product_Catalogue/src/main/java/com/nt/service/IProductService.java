package com.nt.service;

import java.util.List;

import com.nt.dto.ProductRequest;
import com.nt.dto.ProductResponse;

public interface IProductService {
	
	String createProduct(ProductRequest request);
	List<ProductResponse> findAllProduct();

}

package com.nt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nt.dto.ProductRequest;
import com.nt.dto.ProductResponse;
import com.nt.entity.Product;
import com.nt.repository.IProductRepository;
import com.nt.service.IProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

	private final IProductRepository productRepo;

	@Override
	public String createProduct(ProductRequest request) {
		Product product = Product.builder().name(request.getName()).description(request.getDescription())
				.price(request.getPrice()).build();

		Product res = productRepo.save(product);
		log.info("Product with id {} got created", res.getId());
		return "Product with id " + res.getId() + " got created.";
	}

	@Override
	public List<ProductResponse> findAllProduct() {
		List<Product> products = productRepo.findAll();
		return products.stream().map(product -> ProductResponse.builder().id(product.getId()).name(product.getName())
						.description(product.getDescription()).price(product.getPrice()).build())
				.toList();
	}

}

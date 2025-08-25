package com.nt.service;

import com.nt.dto.OrderRequest;

public interface IOrderService {
	
	String placeOrder(OrderRequest orderRequest);

}

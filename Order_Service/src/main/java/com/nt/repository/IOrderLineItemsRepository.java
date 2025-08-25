package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.OrderLineItems;

@Repository
public interface IOrderLineItemsRepository extends JpaRepository<OrderLineItems, Integer>{

}

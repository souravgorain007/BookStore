package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer>{

}

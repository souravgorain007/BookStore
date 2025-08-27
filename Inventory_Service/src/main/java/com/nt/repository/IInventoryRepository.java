package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.Inventory;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Integer>{
	
	Optional<Inventory> findBySkuCode(String skcuCode);

}

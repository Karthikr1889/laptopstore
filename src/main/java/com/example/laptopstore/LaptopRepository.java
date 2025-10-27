package com.example.laptopstore;

import com.example.laptopstore.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


	import java.util.List;
	@Repository
	public interface LaptopRepository extends JpaRepository<Laptop, Long> {
		
	   // Custom query to find laptops by name
	   List<Laptop> findByName(String name);
	   
	   List<Laptop> findByPrice(Double price);
	   
	   // Optional: find by brand
	   List<Laptop> findByBrand(String brand);
	   
	   // Optional: find by processor type
	   List<Laptop> findByProcessor(String processor);
	   
	   
	}


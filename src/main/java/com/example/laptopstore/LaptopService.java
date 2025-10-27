package com.example.laptopstore;

import com.example.laptopstore.LaptopDTO;
import java.util.List;

public interface LaptopService {
	
	 List<LaptopDTO> getAllLaptops();
	 LaptopDTO getLaptopById(Long id);
	 LaptopDTO createLaptop(LaptopDTO laptopDTO);
	 LaptopDTO updateLaptop(Long id, LaptopDTO laptopDTO);
	 boolean deleteLaptop(Long id);
	 	   
	  List<LaptopDTO> searchLaptopByName(String name);
	   List<LaptopDTO> searchLaptopByPrice(Double price);
	   List<LaptopDTO> searchLaptopByBrand(String brand);
	   List<LaptopDTO> searchLaptops(String name, Double price, String brand);	   

}

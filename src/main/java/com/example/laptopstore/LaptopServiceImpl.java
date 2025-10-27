package com.example.laptopstore;
import com.example.laptopstore.LaptopDTO;
import com.example.laptopstore.Laptop;
import com.example.laptopstore.LaptopRepository;
import com.example.laptopstore.LaptopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {
   @Autowired
   private LaptopRepository laptopRepository;
   
   public LaptopServiceImpl(LaptopRepository laptopRepository) {
       this.laptopRepository = laptopRepository;
   }
   /*private LaptopDTO convertToDTO(Laptop laptop) {
       return new LaptopDTO(
           laptop.getName(),
           laptop.getBrand(),
           laptop.getProcessor(),
           laptop.getRam(),
           laptop.getStorage(),
           laptop.getPrice()
       );
   }
   private Laptop convertToEntity(LaptopDTO dto) {
       return new Laptop(
           dto.getName(),
           dto.getBrand(),
           dto.getProcessor(),
           dto.getRam(),
           dto.getStorage(),
           dto.getPrice()
       );
   } */
   @Override
   public LaptopDTO createLaptop(LaptopDTO dto) {
       Laptop saved = laptopRepository.save(convertToEntity(dto));
       return convertToDTO(saved);
       
   }
   
   Laptop convertToEntity(LaptopDTO dto) {
	   
	   if (dto == null) {
	        throw new IllegalArgumentException("LaptopDTO cannot be null");
	    }
	   //Long id, String name, Double price, String brand, String storage, String ram, String processor
	   return new Laptop(dto.getId(), dto.getName(), dto.getPrice(), dto.getBrand(), dto.getStorage(), dto.getRam(), dto.getProcessor() );
   }
   
   LaptopDTO convertToDTO(Laptop dto) {
	   if (dto == null) {
	        throw new IllegalArgumentException("LaptopDTO cannot be null");
	    }
	   return new LaptopDTO(dto.getId(), dto.getName(), dto.getPrice(), dto.getBrand(), dto.getStorage(), dto.getRam(), dto.getProcessor() );

   }
   
   
   
   @Override
   public LaptopDTO getLaptopById(Long id) {
       Laptop laptop = laptopRepository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("Laptop not found"));
       return convertToDTO(laptop);
   }
   
   @Override
   public LaptopDTO updateLaptop(Long id, LaptopDTO laptopDTO) {
	   Laptop existing = laptopRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + id));
      
       existing.setProcessor(laptopDTO.getProcessor());
       existing.setRam(laptopDTO.getRam());
       existing.setStorage(laptopDTO.getStorage());
       existing.setPrice(laptopDTO.getPrice());
       Laptop updated = laptopRepository.save(existing);
       return convertToDTO(updated);
   }
   @Override
   public List<LaptopDTO> getAllLaptops() {
       return laptopRepository.findAll().stream()
           .map(this::convertToDTO)
           .collect(Collectors.toList());
   }
   @Override
   public boolean deleteLaptop(Long id) {
       if (laptopRepository.existsById(id)) {
           laptopRepository.deleteById(id);
       } else {
           throw new ResourceNotFoundException("Laptop not found");
       }
	return false;
   }
   
   @Override
   public List<LaptopDTO> searchLaptopByName(String name) {
       return laptopRepository.findByName(name).stream()
           .map(this::convertToDTO)
           .collect(Collectors.toList());
   }
   
   @Override
   public List<LaptopDTO> searchLaptopByPrice(Double price) {
       return laptopRepository.findByPrice(price).stream()
           .map(this::convertToDTO)
           .collect(Collectors.toList());
   }
   @Override
   public List<LaptopDTO> searchLaptopByBrand(String brand) {
       return laptopRepository.findByBrand(brand).stream()
           .map(this::convertToDTO)
           .collect(Collectors.toList());
   }
      
   @Override
   public List<LaptopDTO> searchLaptops(String name, Double price, String brand) {
	    List<Laptop> laptops = laptopRepository.findAll();

	    return laptops.stream()
	        .filter(l -> name == null || l.getName().equalsIgnoreCase(name))
	        .filter(l -> price == null || Double.compare(l.getPrice(), price) == 0)
	        .filter(l -> brand == null || l.getBrand().equalsIgnoreCase(brand))
	        .map(this::convertToDTO)
	        .collect(Collectors.toList());
	}

}

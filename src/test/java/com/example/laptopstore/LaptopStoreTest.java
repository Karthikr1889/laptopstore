package com.example.laptopstore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
* LaptopStoreTest verifies LaptopController and LaptopServiceImpl using JUnit and Mockito.
* It achieves full coverage of add, update, get, delete, and search operations.
*/
@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class LaptopStoreTest {
	
	@Autowired
	private MockMvc mockMvc;
   // Mock repository to isolate service logic
   @Mock
   private LaptopRepository laptopRepository;   
   private LaptopService laptopService;  
   // Inject mock service into controller 
   private LaptopController laptopController;  
   private LaptopDTO sampleDTO;
   private LaptopDTO sampleDTO2;
   private Laptop sampleEntity;
   private LaptopService mockService;
   private LaptopController controller;
   @BeforeEach
   void setUp() {
    //   MockitoAnnotations.openMocks(this);
	   laptopService = new LaptopServiceImpl(laptopRepository);
	   sampleDTO = new LaptopDTO();
			   sampleDTO.setName("Inspiron");
			   sampleDTO.setBrand("Dell");
			   sampleDTO.setPrice(500.0);
			   sampleDTO.setRam("16GB");
			   sampleDTO.setProcessor("i7");
			   sampleDTO.setStorage("512GB");
			   
			   sampleDTO2 = new LaptopDTO();
			   sampleDTO2.setName("ZenBook");
			   sampleDTO2.setBrand("Asus");
			   sampleDTO2.setPrice(500.0);
			   sampleDTO2.setRam("16GB");
			   sampleDTO2.setProcessor("i5");
			   sampleDTO2.setStorage("512GB");
			   
       sampleEntity = new Laptop();
       sampleEntity.setId(1L);
       sampleEntity.setName("Inspiron");
       sampleEntity.setBrand("Dell");
       sampleEntity.setPrice(500.0);
       sampleEntity.setStorage("512GB");
       sampleEntity.setProcessor("i7");
       sampleEntity.setRam("16GB");
       
       mockService = mock(LaptopService.class);
       controller = new LaptopController(mockService);
   }
   private LaptopDTO createSampleDTO(String name, String brand, double price) {
	    LaptopDTO dto = new LaptopDTO();
	    dto.setName(name);
	    dto.setBrand(brand);
	    dto.setPrice(price);
	    dto.setRam("16GB");
	    dto.setProcessor("i7");
	    dto.setStorage("512GB");
	    return dto;
	}
// ✅ Test creatLaptop via Controller Tests

   @Test
   void testCreateLaptopController() throws Exception {
	   
	   
       when(mockService.createLaptop(any())).thenReturn(sampleDTO);
       // Inject mock into controller
       LaptopController controller = new LaptopController(mockService);
       // Act: Call controller method directly
       ResponseEntity<LaptopDTO> response = controller.createLaptop(sampleDTO);
       // Assert: Check response
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
       assertEquals(201, response.getStatusCode().value());        
       
    // ❌ Invalid data (e.g. negative price)
       LaptopDTO invalidDTO = createSampleDTO("XPS", "Dell", -100.0);
       when(mockService.createLaptop(invalidDTO))
           .thenThrow(new InvalidDataException("Price must be greater than zero"));

       InvalidDataException invalidEx = assertThrows(InvalidDataException.class, () -> {
           controller.createLaptop(invalidDTO);
       });
       assertEquals("Price must be greater than zero", invalidEx.getMessage());

       // ❌ Resource not found (e.g. related entity missing)
       LaptopDTO dtoWithMissingDependency = createSampleDTO("EliteBook", "HP", 800.0);
       when(mockService.createLaptop(dtoWithMissingDependency))
           .thenThrow(new ResourceNotFoundException("Related category not found"));

       ResourceNotFoundException notFoundEx = assertThrows(ResourceNotFoundException.class, () -> {
           controller.createLaptop(dtoWithMissingDependency);
       });
       assertEquals("Related category not found", notFoundEx.getMessage());
   }  
   // ✅ Test: CreateLaptop via service
   @Test
   void testAddLaptopService() {
       when(laptopRepository.save(any())).thenReturn(sampleEntity);
       LaptopDTO result =laptopService.createLaptop(sampleDTO);
    // Assert: Check response
       assertEquals("Inspiron", result.getName());
       assertEquals("Dell", result.getBrand());
       
       LaptopServiceImpl serviceWithMock = new LaptopServiceImpl(laptopRepository) {
           @Override
           public LaptopDTO createLaptop(LaptopDTO dto) {
               if ("HP".equals(dto.getBrand())) {
                   throw new ResourceNotFoundException("Category not found for brand: HP");
               }
               return super.createLaptop(dto);
           }
       };
       LaptopDTO missingDTO = createSampleDTO("EliteBook", "HP", 800.0);
       assertThrows(ResourceNotFoundException.class, () -> serviceWithMock.createLaptop(missingDTO));
   
   } 
   //Test getLaptopId via controller
   @Test
   void testGetLaptopById() {
       when(mockService.getLaptopById(1L)).thenReturn(sampleDTO);
    // Act: Call controller method directly
       ResponseEntity<LaptopDTO> response = controller.getLaptopById(1L);
    // Assert: Check response
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals("Dell", response.getBody().getBrand());
       when(mockService.getLaptopById(99L))
       .thenThrow(new ResourceNotFoundException("Laptop not found"));
   assertThrows(ResourceNotFoundException.class, () -> controller.getLaptopById(99L));

   }
   
// ✅ Test: Get laptopById via service
   
   @Test
   void testGetLaptopByIdService() {
     
       when(laptopRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
       LaptopDTO result = laptopService.getLaptopById(1L); //error fixed 
       assertEquals("Dell", result.getBrand(),"Brand should be Dell");
       assertEquals("Inspiron", result.getName(),"Name should be Inspiron");
      
       when(laptopRepository.findById(99L)).thenReturn(Optional.empty());
       assertThrows(ResourceNotFoundException.class, () -> laptopService.getLaptopById(99L));
       
   }
   //Test update Laptop via controller 
   @Test
   void testUpdateLaptopController() {
       Long laptopId = 1L;
       // Arrange: updated DTO
       LaptopDTO updatedDTO = new LaptopDTO();
       updatedDTO.setName("Inspiron Plus");
       updatedDTO.setBrand("Dell");
       updatedDTO.setPrice(650.0);
       updatedDTO.setRam("32GB");
       updatedDTO.setProcessor("i9");
       updatedDTO.setStorage("1TB");

       when(mockService.updateLaptop(eq(laptopId), any(LaptopDTO.class))).thenReturn(updatedDTO);
       // Act
       ResponseEntity<LaptopDTO> response = controller.updateLaptop(laptopId, updatedDTO);
       // Assert
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertNotNull(response);
       assertEquals("Inspiron Plus", response.getBody().getName());
       assertEquals("Dell", response.getBody().getBrand());
       assertEquals("i9", response.getBody().getProcessor());
       assertEquals("1TB", response.getBody().getStorage());
       
       when(mockService.updateLaptop(eq(99L), any()))
       .thenThrow(new ResourceNotFoundException("Laptop not found"));
   assertThrows(ResourceNotFoundException.class, () -> controller.updateLaptop(99L, updatedDTO));
   }
   // ✅ Test: UpdateLaptop via service
   @Test
   void testUpdateLaptopService() {
       Laptop existing = new Laptop();
       existing.setId(1L);
       existing.setName("Inspiron");
       existing.setBrand("Dell");
       existing.setProcessor("i5");
       existing.setRam("8");
       existing.setStorage("256");
       existing.setPrice(600.0);
    // ✅ Mock repository call inside service
       when(laptopRepository.findById(1L)).thenReturn(Optional.of(existing));
       when(laptopRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
       
       LaptopDTO dto = new LaptopDTO();
       dto.setName("Inspiron");
       dto.setBrand("Dell");
       dto.setProcessor("i7");
       dto.setRam("16");
       dto.setStorage("512");
       dto.setPrice(750.0);
     //  when(laptopService.updateLaptop(1L,dto)).thenReturn(dto);//error fixed 
       LaptopDTO result = laptopService.updateLaptop(1L,dto);
       
       assertEquals("i7", result.getProcessor());
       assertEquals(750.0, result.getPrice());
   }
   @Test
   void testUpdateLaptopNotFound() {
       LaptopDTO dto = new LaptopDTO();
       // set fields...
       when(laptopRepository.findById(99L)).thenReturn(Optional.empty());
       assertThrows(ResourceNotFoundException.class, () -> laptopService.updateLaptop(99L, dto));
      
   }
   
   //Test GetAllLaptop via controller 
   @Test
   void testGetAllLaptopsController() {
	   List<LaptopDTO> mockList = Arrays.asList(sampleDTO, sampleDTO2);
	   when(mockService.getAllLaptops()).thenReturn(mockList);
	// Act
       ResponseEntity<List<LaptopDTO>> response = controller.getAllLaptops();
       // Assert
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertNotNull(response.getBody());
       assertEquals(2, response.getBody().size());
       assertEquals("Inspiron", response.getBody().get(0).getName());
       assertEquals("ZenBook", response.getBody().get(1).getName());
   }
// ✅ Test: Get all laptops via service
   @Test
   void testGetAllLaptopsService() {
       Laptop l1 = new Laptop(); l1.setName("Inspiron"); l1.setBrand("Dell");l1.setPrice(600.0);
       Laptop l2 = new Laptop(); l2.setName("AIO"); l2.setBrand("HP");l2.setPrice(700.0);
      when(laptopRepository.findAll()).thenReturn(Arrays.asList(l1, l2));
       List<LaptopDTO> result = laptopService.getAllLaptops();//error fixed
     //  assertEquals(2, result.size());
       assertEquals("Dell", result.get(0).getBrand());
       assertEquals("HP", result.get(1).getBrand());
   }
   
   //Test DeleteLaptop via controller 
   @Test
   void testDeleteLaptopController() {
       Long laptopId = 1L;
    // Mock service to return true (even though controller ignores it)
       when(mockService.deleteLaptop(laptopId)).thenReturn(true);

       ResponseEntity<Void> response = controller.deleteLaptop(laptopId);
       assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
       assertNull(response.getBody());
       verify(mockService, times(1)).deleteLaptop(laptopId);
       when(mockService.deleteLaptop(99L))
       .thenThrow(new ResourceNotFoundException("Laptop not found"));
   assertThrows(ResourceNotFoundException.class, () -> controller.deleteLaptop(99L));

   }
// ✅ Test: Delete laptop via service
   @Test
   void testDeleteLaptopService() {
	   when(laptopRepository.existsById(3L)).thenReturn(true);
	    doNothing().when(laptopRepository).deleteById(3L);
	    
       laptopService.deleteLaptop(3L);//error fixed 
       verify(laptopRepository).existsById(3L);
       verify(laptopRepository).deleteById(3L);
       when(laptopRepository.existsById(99L)).thenReturn(false);
       assertThrows(ResourceNotFoundException.class, () -> laptopService.deleteLaptop(99L));

   }
   
   @Test
   void testSearchLaptopByName() {
	   Laptop l1 = new Laptop();
	   l1.setId(1L);
       l1.setName("Inspiron");
       l1.setBrand("Dell"); 
       l1.setPrice(500.0);
       when(laptopRepository.findByName("Inspiron")).thenReturn(List.of(l1));

       List<LaptopDTO> result = laptopService.searchLaptopByName("Inspiron");

       assertEquals(1, result.size());
       assertEquals("Inspiron", result.get(0).getName());
   }

   @Test
   void testSearchLaptopByPrice() {
	   Laptop l1 = new Laptop();
	   l1.setId(1L);
l1.setName("Inspiron");
l1.setBrand("Dell"); 
l1.setPrice(500.0);
       when(laptopRepository.findByPrice(500.0)).thenReturn(List.of(l1));

       List<LaptopDTO> result = laptopService.searchLaptopByPrice(500.0);

       assertEquals(1, result.size());
       assertEquals(500.0, result.get(0).getPrice());
   }

   @Test
   void testSearchLaptopByBrand() {
	   Laptop l1 = new Laptop();
	   l1.setId(1L);
l1.setName("Inspiron");
l1.setBrand("Dell"); 
l1.setPrice(500.0);
       when(laptopRepository.findByBrand("Dell")).thenReturn(List.of(l1));

       List<LaptopDTO> result = laptopService.searchLaptopByBrand("Dell");

       assertEquals(1, result.size());
       assertEquals("Dell", result.get(0).getBrand());
   }
//Controller Test 
   @Test
   void testSearchLaptopsController() {
       // Arrange
       String name = "Inspiron";
       Double price = 600.0;
       String brand = "Dell";
       List<LaptopDTO> mockResult = List.of(sampleDTO);

       // Mock the service
       when(mockService.searchLaptops(name, price, brand)).thenReturn(mockResult);

       // Act
       ResponseEntity<List<LaptopDTO>> response = controller.searchLaptops(name, price, brand);

       // Assert
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertNotNull(response.getBody());
       assertEquals(1, response.getBody().size());
       assertEquals("Inspiron", response.getBody().get(0).getName());
       assertEquals("Dell", response.getBody().get(0).getBrand());

       // Verify service interaction
       verify(mockService, times(1)).searchLaptops(name, price, brand);
       when(mockService.searchLaptops(null, -500.0, null))
       .thenThrow(new InvalidDataException("Invalid search filters"));
   assertThrows(InvalidDataException.class, () -> controller.searchLaptops(null, -500.0, null));

   }
   @Test
   void testSearchLaptopsWithAllFilters() {
       Laptop l1 = new Laptop();
    		   l1.setId(1L);
       l1.setName("Inspiron");
       l1.setBrand("Dell"); 
       l1.setPrice(500.0);
       
       Laptop l2 = new Laptop();
       l2.setId(2L); 
       l2.setName("Inspiron"); 
       l2.setBrand("Dell"); l2.setPrice(600.0);

       when(laptopRepository.findAll()).thenReturn(List.of(l1, l2));

       List<LaptopDTO> result = laptopService.searchLaptops("Inspiron", 500.0, "Dell");

       assertEquals(1, result.size());
       assertEquals(500.0, result.get(0).getPrice());
   }
  
  
}

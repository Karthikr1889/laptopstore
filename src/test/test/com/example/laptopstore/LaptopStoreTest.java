package com.example.laptopstore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.mock.mockito.MockBean;
/**
* LaptopStoreTest verifies LaptopController and LaptopServiceImpl using JUnit and Mockito.
* It achieves full coverage of add, update, get, delete, and search operations.
*/
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(LaptopController.class)
public class LaptopStoreTest {
	
	@Autowired
	private MockMvc mockMvc;
   // Mock repository to isolate service logic
   @Mock
   private LaptopRepository laptopRepository;
   
   @MockBean
   private LaptopService laptopService;
   
   // Inject mock service into controller
   @Mock
   private LaptopController laptopController;
   
   private LaptopServiceImpl laptopServiceImpl;
   
   private LaptopDTO sampleDTO;
   private Laptop sampleEntity;
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
       sampleEntity = new Laptop();
       sampleEntity.setName("Inspiron");
       sampleEntity.setBrand("Dell");
       sampleEntity.setPrice(500.0);
       sampleEntity.setStorage("512GB");
       sampleEntity.setProcessor("i7");
       sampleEntity.setRam("16GB");
   }
// ✅ Controller Tests

   @Test
   void testCreateLaptopController() throws Exception {
	   LaptopDTO sampleDTO = new LaptopDTO();
	   sampleDTO.setName("Inspiron");
	   sampleDTO.setBrand("Dell");
	   sampleDTO.setPrice(500.0);
	   sampleDTO.setRam("16GB");
	   sampleDTO.setProcessor("i7");
	   sampleDTO.setStorage("512GB");
	   
       when(laptopService.createLaptop(any())).thenReturn(sampleDTO);

       mockMvc.perform(post("/laptops")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                   {
                "name": "Inspiron",
                "price": 500.0,
                "brand": "Dell",
                "storage": "512GB",
                "ram": "16GB",
                "processor": "i7"
                   }
               """))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name").value("Inspiron"));
   }
   // ✅ Test: CreateLaptop via service
   @Test
   void testAddLaptopService() {
       LaptopDTO dto = new LaptopDTO();
       dto.setName("ZenBook");
       dto.setBrand("Asus");
       dto.setProcessor("i7");
       dto.setRam("16");//error fixed
       dto.setStorage("512");//error fixed
       dto.setPrice(850.0);
       
       Laptop entity = new Laptop();
       entity.setName("ZenBook");
       entity.setBrand("Asus");
       entity.setProcessor("i7");
       entity.setRam("16");//error fixed
       entity.setStorage("512");//error fixed
       entity.setPrice(850.0);
       when(laptopRepository.save(any())).thenReturn(entity);
       LaptopDTO result =laptopService.createLaptop(dto);//error fixed
       assertEquals("ZenBook", result.getName());
       assertEquals("Asus", result.getBrand());
   } 
// ✅ Test: Get laptopById via service
   @Test
   void testGetLaptopByIdService() {
       Laptop laptop = new Laptop();
       laptop.setId(2L);
       laptop.setName("MacBook Air");
       laptop.setBrand("Apple");
       laptop.setProcessor("M2");
       laptop.setRam("8");
       laptop.setStorage("256");
       laptop.setPrice(999.0);
       when(laptopRepository.findById(2L)).thenReturn(Optional.of(laptop));
       LaptopDTO result = laptopService.getLaptopById(2L); //error fixed 
       assertEquals("Apple", result.getBrand(),"Brand should be Apple");
       assertEquals("MacBook Air", result.getName(),"Name should be MacBook Air");
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
       assertThrows(RuntimeException.class, () -> {
           laptopService.updateLaptop(99L, dto);
       });
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
// ✅ Test: Delete laptop via service
   @Test
   void testDeleteLaptopService() {
	   when(laptopRepository.existsById(3L)).thenReturn(true);
	    doNothing().when(laptopRepository).deleteById(3L);
	    
       laptopService.deleteLaptop(3L);//error fixed 
       verify(laptopRepository).existsById(3L);
       verify(laptopRepository).deleteById(3L);
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

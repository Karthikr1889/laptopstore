package com.example.laptopstore;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LaptopstoreApplicationTests {
	
	@Test
    void contextLoads() {
        // basic test
		int expected = 42;
        int actual = 40 + 2;
        assertEquals(expected, actual, "Basic math test to verify JUnit setup");
    }
	
/*	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	//Run|Debug
	public static void main(String[] args) {
    	LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
    			.selectors(DiscoverySelectors.selectPackage("com.example.laptopstore.test")).build();
    	
    	Launcher launcher = org.junit.platform.launcher.core.LauncherFactory.create();
    //	CustomTestSummaryListener listener = new CustomTestSummaryListener();
    	
    	launcher.execute(request);
    	
		//SpringApplication.run(LaptopstoreApplicationTests.class, args);
	}
*/
}


